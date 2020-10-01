package com.myproject;

import com.myproject.model.Flight;
import com.myproject.rest.FlightController;
import com.myproject.serviceApi.FlightService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.myproject.constants.FlightConstants.*;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:app-context-test.xml"})
public class FlightControllerIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlightControllerIT.class);

    public static final String FLIGHTS_ENDPOINT = "/flights";

    @Autowired
    private FlightController flightController;

    @Autowired
    private FlightService flightService;

    @Test
    public void shouldFindAllFlights() throws Exception {

        List<Flight> flights = flightService.findAll();
        assertNotNull(flights);
        assertTrue(flights.size() > 0);
    }

    @Test
    public void shouldCreateFlight() throws Exception {
        Flight flight = new Flight();
        flight.setDirection(RandomStringUtils.randomAlphabetic(DIRECTION_MAX_SIZE));
        flight.setDateFlight(LocalDate.now());
        Integer id = flightService.create(flight);
        assertNotNull(id);
    }

    @Test
    public void shouldFindFlightById() throws Exception {

        // given
        Flight flight = new Flight();
        flight.setDirection(RandomStringUtils.randomAlphabetic(DIRECTION_MAX_SIZE));
        flight.setDateFlight(LocalDate.now());
        Integer id = flightService.create(flight);

        assertNotNull(id);

        // when
        Optional<Flight> optionalFlight = flightService.findById(id);

        // then
        assertTrue(optionalFlight.isPresent());
        assertEquals(optionalFlight.get().getFlightId(), id);
        assertEquals(optionalFlight.get().getDirection(), flight.getDirection());
        assertEquals(optionalFlight.get().getDateFlight(), flight.getDateFlight());
    }

    @Test
    public void shouldUpdateFlight() throws Exception {

        // given
        Flight flight = new Flight();
        flight.setDirection(RandomStringUtils.randomAlphabetic(DIRECTION_MAX_SIZE));
        flight.setDateFlight(LocalDate.now());
        Integer id = flightService.create(flight);

        assertNotNull(id);

        Optional<Flight> optionalFlight = flightService.findById(id);
        assertTrue(optionalFlight.isPresent());

        optionalFlight.get().
                setDirection(RandomStringUtils.randomAlphabetic(DIRECTION_MAX_SIZE));

        // when
        int result = flightService.update(optionalFlight.get());

        // then
        assertTrue(1 == result);

        Optional<Flight> updatedFlightOptional = flightService.findById(id);
        assertTrue(updatedFlightOptional.isPresent());
        assertEquals(updatedFlightOptional.get().getFlightId(), id);
        assertEquals(updatedFlightOptional.get().getDirection()
                ,optionalFlight.get().getDirection());
        assertEquals(updatedFlightOptional.get().getDateFlight()
                ,optionalFlight.get().getDateFlight());

    }
    @Test
    public void shouldDeleteFlight() throws Exception {
        // given
        Flight flight = new Flight();
        flight.setDirection(RandomStringUtils.randomAlphabetic(DIRECTION_MAX_SIZE));
        flight.setDateFlight(LocalDate.now());
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