package com.myproject;

import com.myproject.model.Passenger;
import com.myproject.serviceApi.PassengerService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static com.myproject.constants.PassengerConstants.*;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath*:test-db.xml", "classpath*:test-service.xml", "classpath:dao.xml"})
public class PassengerServiceImplIT {

    @Autowired
    PassengerService passengerService;

    @Test
    public void shouldFindAllPassengers() {

        List<Passenger> passengers = passengerService.findAll();
        assertNotNull(passengers);
        assertTrue(passengers.size() > 0);
    }

    @Test
    public void shouldFindPassengerByPassengerId() {

        // given
        Passenger passenger = new Passenger();
        passenger.setFirstname(RandomStringUtils.randomAlphabetic(FIRST_NAME_SIZE));
        passenger.setLastname(RandomStringUtils.randomAlphabetic(LAST_NAME_SIZE));
        passenger.setFlightId(101);

        Integer id = passengerService.create(passenger);

        // when
        Optional<Passenger> optionalPassenger = passengerService.findById(id);

        // then
        Assertions.assertTrue(optionalPassenger.isPresent());
        assertEquals(optionalPassenger.get().getFirstname(), passenger.getFirstname());
        assertEquals(optionalPassenger.get().getLastname(), passenger.getLastname());
        assertEquals(optionalPassenger.get().getFlightId(), passenger.getFlightId());

    }

    @Test
    public void shouldCreatePassenger() {
        Passenger passenger = new Passenger();
        passenger.setFirstname(RandomStringUtils.randomAlphabetic(FIRST_NAME_SIZE));
        passenger.setLastname(RandomStringUtils.randomAlphabetic(LAST_NAME_SIZE));
        passenger.setFlightId(101);
        Integer id = passengerService.create(passenger);
        assertNotNull(id);
    }

    @Test
    public void shouldUpdatePassenger() {

        // given
        Passenger passenger = new Passenger();
        passenger.setFirstname(RandomStringUtils.randomAlphabetic(FIRST_NAME_SIZE));
        passenger.setLastname(RandomStringUtils.randomAlphabetic(LAST_NAME_SIZE));
        passenger.setFlightId(101);
        Integer id = passengerService.create(passenger);
        assertNotNull(id);

        Optional<Passenger> optionalPassenger = passengerService.findById(id);

        Assertions.assertTrue(optionalPassenger.isPresent());

        optionalPassenger.get().
                setFirstname(RandomStringUtils.randomAlphabetic(FIRST_NAME_SIZE));


        // when
        int result = passengerService.update(optionalPassenger.get());

        // then
        assertTrue(1 == result);

        Optional<Passenger> updatedOptionalPassenger = passengerService.findById(id);
        Assertions.assertTrue(updatedOptionalPassenger.isPresent());
        assertEquals(updatedOptionalPassenger.get().getPassengerId(), id);
        assertEquals(updatedOptionalPassenger.get().getFirstname(), optionalPassenger.get().getFirstname());
        assertEquals(updatedOptionalPassenger.get().getLastname(), optionalPassenger.get().getLastname());
        assertEquals(updatedOptionalPassenger.get().getFlightId(), optionalPassenger.get().getFlightId());

    }

    @Test
    public void shouldDeletePassenger() {
        // given
        Passenger passenger = new Passenger();
        passenger.setFirstname(RandomStringUtils.randomAlphabetic(FIRST_NAME_SIZE));
        passenger.setLastname(RandomStringUtils.randomAlphabetic(LAST_NAME_SIZE));
        passenger.setFlightId(101);
        Integer id = passengerService.create(passenger);
        assertNotNull(id);

        List<Passenger> passengers = passengerService.findAll();
        assertNotNull(passengers);

        // when
        int result = passengerService.delete(id);

        // then
        assertTrue(1 == result);

        List<Passenger> currentPassengers = passengerService.findAll();
        assertNotNull(currentPassengers);

        assertTrue(passengers.size()-1 == currentPassengers.size());
    }
}
