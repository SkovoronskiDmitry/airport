package com.myproject;

import com.myproject.model.Flight;
import com.myproject.serviceApi.FlightService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.myproject.constants.FlightConstants.DIRECTION_MAX_SIZE;
import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath*:test-db.xml", "classpath*:test-service.xml", "classpath:dao.xml"})
public class FlightServiceImplIT {
    @Autowired
    FlightService flightService;

    @Test
    public void shouldFindAllFlights() {

        List<Flight> flights = flightService.findAll();
        assertNotNull(flights);
        assertTrue(flights.size() > 0);
    }

    @Test
    public void shouldFindFlightById() {

        // given
        Flight flight = new Flight();
        flight.setDirection(RandomStringUtils.randomAlphabetic(DIRECTION_MAX_SIZE));
        Integer id = flightService.create(flight);

        // when
        Optional<Flight> optionalFlight = flightService.findById(id);

        // then
        Assertions.assertTrue(optionalFlight.isPresent());
        assertEquals(optionalFlight.get().getFlightId(), id);
        assertEquals(optionalFlight.get().getDirection(), flight.getDirection());
    }

    @Test
    public void shouldCreateFlight() {
        Flight flight = new Flight();
        flight.setDirection(RandomStringUtils.randomAlphabetic(DIRECTION_MAX_SIZE));
        flight.setDateFlight(LocalDate.now());
        Integer id = flightService.create(flight);
        assertNotNull(id);
    }

    @Test
    public void shouldUpdateFlight() {

        // given
        Flight flight = new Flight();
        flight.setDirection(RandomStringUtils.randomAlphabetic(DIRECTION_MAX_SIZE));
        Integer id = flightService.create(flight);
        assertNotNull(id);

        Optional<Flight> flightOptional = flightService.findById(id);
        Assertions.assertTrue(flightOptional.isPresent());

        flightOptional.get().
                setDirection(RandomStringUtils.randomAlphabetic(DIRECTION_MAX_SIZE));

        // when
        int result = flightService.update(flightOptional.get());

        // then
        assertTrue(1 == result);

        Optional<Flight> updatedFlightOptional = flightService.findById(id);
        Assertions.assertTrue(updatedFlightOptional.isPresent());
        assertEquals(updatedFlightOptional.get().getFlightId(), id);
        assertEquals(updatedFlightOptional.get().getDirection(),flightOptional.get().getDirection());

    }

    @Test
    public void shouldDeleteFlight() {
        // given
        Flight flight = new Flight();
        flight.setDirection(RandomStringUtils.randomAlphabetic(DIRECTION_MAX_SIZE));
        Integer id = flightService.create(flight);

        List<Flight> flights = flightService.findAll();
        assertNotNull(flights);

        // when
        int result = flightService.delete(id);

        // then
        assertTrue(1 == result);

        List<Flight> currentFlights = flightService.findAll();
        assertNotNull(currentFlights);

        assertTrue(flights.size()-1 == currentFlights.size());
    }
}
