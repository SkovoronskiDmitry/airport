package com.myproject.service_rest_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.model.dto.FlightDto;
import com.myproject.service_rest.FlightDtoRestService;
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
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:app-context-test.xml"})
public class FlightDtoRestServiceIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlightDtoRestServiceIT.class);

    public static final String FLIGHTS_DTO_URL = "http://localhost:8081/flightsDto";

    @Autowired
    RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    private ObjectMapper mapper = new ObjectMapper();

    FlightDtoRestService flightDtoRestService;

    @BeforeEach
    public void before() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        flightDtoRestService = new FlightDtoRestService(FLIGHTS_DTO_URL, restTemplate);
    }

    @Test
    void shouldFindAllWithDateFilter() throws Exception {

        LOGGER.debug("should find with date filter()");
        //given
        LocalDate dateFrom = LocalDate.now().minusDays(1);
        LocalDate dateTo= LocalDate.now().plusDays(1);
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(FLIGHTS_DTO_URL + "?dateFrom=" + dateFrom + "&dateTo=" + dateTo)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(Arrays.asList(create(0), create(1))))
                );
        //when
        List<FlightDto> projectsDtoList = flightDtoRestService.findAllWithQuantityPassengersAndDateFilter(dateFrom,dateTo);

        //then
        assertTrue(projectsDtoList.size() == 2);
    }

    @Test
    void shouldFindAllWithQuantityPassengers() throws Exception {

        LOGGER.debug("should find all with quantity passengers()");
        //given

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(FLIGHTS_DTO_URL + "/quantity")))

                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(Arrays.asList(create(0), create(1))))
                );
        //when
        List<FlightDto> flightDtoList = flightDtoRestService.findAllWithQuantityPassengers();

        //then
        assertTrue(flightDtoList.size() > 0);
    }

    private FlightDto create(int index){

        FlightDto flightDto = new FlightDto();
        flightDto.setFlightId(index);
        flightDto.setDirection("direction"+index);
        flightDto.setDateFlight(LocalDate.now());
        flightDto.setQuantityPassengers(1+index);
        return flightDto;
    }



}
