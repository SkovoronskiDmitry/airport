package com.myproject.service_rest_test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.model.Flight;
import com.myproject.service_rest.FlightRestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:app-context-test.xml"})
public class FlightRestServiceIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlightRestServiceIT.class);

    public static final String FLIGHTS_URL = "http://localhost:8081/flights";

    @Autowired
    RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    private ObjectMapper mapper = new ObjectMapper();

    FlightRestService flightRestService;

    @BeforeEach
    public void before() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        flightRestService = new FlightRestService(FLIGHTS_URL, restTemplate);
    }

    @Test
    void shouldFindAllFlights() throws URISyntaxException, JsonProcessingException {

        LOGGER.debug("Should find all flights");
        // given
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(FLIGHTS_URL)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(Arrays.asList(create(0), create(1))))                );

        // when
        List<Flight> flights = flightRestService.findAll();

        // then
        mockServer.verify();
        assertNotNull(flights);
        assertTrue(flights.size() > 0);
    }

    @Test
    void shouldCreateFlight() throws Exception {

        LOGGER.debug("should create Flight");
        // given
        Integer id = 1;
        Flight flight = create(id);

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(FLIGHTS_URL)))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(id))
                );
        //when
        Integer flightId = flightRestService.create(flight);

        // then
        mockServer.verify();
        assertNotNull(flightId);
    }

    @Test
    void shouldFindFlightById() throws Exception {

        LOGGER.debug("should find flight by id ");
        // given
        Integer id = 1;
        Flight flight = create(id);

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(FLIGHTS_URL + "/" + id)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(flight))
                );

        // when
        Optional<Flight> optionalFlights = flightRestService.findById(id);

        // then
        mockServer.verify();
        assertTrue(optionalFlights.isPresent());
        assertEquals(optionalFlights.get().getFlightId(), id);
        assertEquals(optionalFlights.get().getDirection(), flight.getDirection());
        assertEquals(optionalFlights.get().getDateFlight(), flight.getDateFlight());
    }

    @Test
    void shouldUpdateFlight() throws Exception {

        // given
        Integer id = 1;
        Flight flight = create(id);

        mockServer.expect(ExpectedCount.once(), requestTo(FLIGHTS_URL))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(id))
                );

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(FLIGHTS_URL + "/" + id)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(flight))
                );

        // when
        int result = flightRestService.update(flight);
        Optional<Flight> updatedFlightOptional = flightRestService.findById(id);

        // then
        mockServer.verify();
        assertTrue(1 == result);

        assertTrue(updatedFlightOptional.isPresent());
        assertEquals(updatedFlightOptional.get().getFlightId(), id);
        assertEquals(updatedFlightOptional.get().getDirection(), flight.getDirection());
    }





    @Test
    void shouldDeleteFlight() throws Exception {

        //given
        Integer id = 1;

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(FLIGHTS_URL + id)))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString("1"))
                );
        //when
        int result = flightRestService.delete(id);

        // then
        mockServer.verify();
        assertEquals(result,1);
    }


    private Flight create(int index){

        Flight flight = new Flight();
        flight.setFlightId(index);
        flight.setDirection("direction:"+index);
        return flight;
    }

}
