package com.myproject.service_rest_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.model.Passenger;
import com.myproject.service_rest.PassengerRestService;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:app-context-test.xml"})
public class PassengerRestServiceIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(PassengerRestServiceIT.class);

    PassengerRestService passengerRestService;

    private final String PASSENGERS_URL = "http://localhost:8081/passengers";

    @Autowired
    RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    private void before()
    {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        passengerRestService = new PassengerRestService(PASSENGERS_URL,restTemplate);
    }

    @Test
    void shouldFindAllPassengers()throws Exception {

        LOGGER.debug("should find all passengers");
        //given
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PASSENGERS_URL)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(Arrays.asList(create(0), create(1))))
                );
        //when
        List<Passenger> passengerList = passengerRestService.findAll();

        // then
        mockServer.verify();
        assertNotNull(passengerList);
        assertTrue(passengerList.size() > 0);
    }

    @Test
    void shouldFindPassengerById() throws Exception {

        LOGGER.debug("should find passenger by id");
        // given
        Integer id = 1;
        Passenger passenger = create(id);

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PASSENGERS_URL + "/" + id)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(passenger))
                );

        // when
        Optional<Passenger> optionalPassengers = passengerRestService.findById(id);

        // then
        mockServer.verify();
        assertTrue(optionalPassengers.isPresent());
        assertEquals(optionalPassengers.get().getPassengerId(), id);
        assertEquals(optionalPassengers.get().getFirstname(), passenger.getFirstname());
        assertEquals(optionalPassengers.get().getLastname(), passenger.getLastname());
        assertEquals(optionalPassengers.get().getFlightId(), passenger.getFlightId());
    }

    @Test
    void shouldCreatePassenger() throws Exception{

        LOGGER.debug("should create passenger");
        Integer id = 1;
        Passenger passenger = create(id);
        //given
        mockServer.expect(ExpectedCount.once(),requestTo(new URI(PASSENGERS_URL)))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(id)));


        //when
        Integer passengerId = passengerRestService.create(passenger);

        //then
        mockServer.verify();
        assertNotNull(passengerId);
    }

    @Test
    void shouldUpdatePassenger() throws Exception{

        LOGGER.debug("should update passenger()");
        //given
        Integer id = 1;
        Passenger passenger = create(id);
        mockServer.expect(ExpectedCount.once(),requestTo(PASSENGERS_URL))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(1)));

        //when
        int result = passengerRestService.update(passenger);

        //then
        mockServer.verify();
        assertEquals(result, 1);
    }

    @Test
    void shouldDeletePassenger() throws Exception {

        LOGGER.debug("should delete passenger");
        //given
        Integer id = 1;
        Passenger passenger = create(id);
        mockServer.expect(ExpectedCount.once(), requestTo(PASSENGERS_URL + id ))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString("1")));

        //when
        int result = passengerRestService.delete(id);

        //then
        assertTrue(result == 1);

    }

    private Passenger create(int index){

        Passenger passenger = new Passenger();
        passenger.setPassengerId(index);
        passenger.setFirstname("firstname"+index);
        passenger.setLastname("lastname"+index);
        passenger.setFlightId(100+index);

        return passenger;
    }

}
