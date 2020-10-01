package com.myproject;

import com.myproject.dao.FlightDao;
import com.myproject.model.Flight;
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

import static com.myproject.constants.FlightConstants.*;
import static org.junit.Assert.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath*:test-db.xml", "classpath*:test-dao.xml", "classpath:dao.xml"})
public class FlightDaoJdbcIT {

    private final FlightDao fLightDao;

    @Autowired
    public FlightDaoJdbcIT(FlightDao fLightDao) {
        this.fLightDao = fLightDao;
    }

    @Test
    public void shouldFindAllFlights() {

        List<Flight> flights = fLightDao.findAll();
        assertNotNull(flights);
        assertTrue(flights.size() > 0);
    }

    @Test
    public void shouldFindFlightById() {

        // given
        Flight flight = new Flight();
        flight.setDirection(RandomStringUtils.randomAlphabetic(DIRECTION_MAX_SIZE));
        Integer id = fLightDao.create(flight);

        // when
        Optional<Flight> optionalFlight = fLightDao.findById(id);

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
        Integer id = fLightDao.create(flight);
        assertNotNull(id);
    }

    @Test
    public void shouldUpdateFlight() {

        // given
        Flight flight = new Flight();
        flight.setDirection(RandomStringUtils.randomAlphabetic(DIRECTION_MAX_SIZE));
        Integer id = fLightDao.create(flight);
        assertNotNull(id);

        Optional<Flight> flightOptional = fLightDao.findById(id);
        Assertions.assertTrue(flightOptional.isPresent());

        flightOptional.get().
                setDirection(RandomStringUtils.randomAlphabetic(DIRECTION_MAX_SIZE));

        // when
        int result = fLightDao.update(flightOptional.get());

        // then
        assertTrue(1 == result);

        Optional<Flight> updatedFlightOptional = fLightDao.findById(id);
        Assertions.assertTrue(updatedFlightOptional.isPresent());
        assertEquals(updatedFlightOptional.get().getFlightId(), id);
        assertEquals(updatedFlightOptional.get().getDirection(),flightOptional.get().getDirection());

    }

    @Test
    public void shouldDeleteFlight() {
        // given
        Flight flight = new Flight();
        flight.setDirection(RandomStringUtils.randomAlphabetic(DIRECTION_MAX_SIZE));
        Integer id = fLightDao.create(flight);

        List<Flight> flights = fLightDao.findAll();
        assertNotNull(flights);

        // when
        int result = fLightDao.delete(id);

        // then
        assertTrue(1 == result);

        List<Flight> currentFlights = fLightDao.findAll();
        assertNotNull(currentFlights);

        assertTrue(flights.size()-1 == currentFlights.size());
    }
}
