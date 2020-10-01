package com.myproject.service_rest;

import com.myproject.serviceApi.FlightService;
import com.myproject.model.Flight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class FlightRestService implements FlightService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlightRestService.class);

    private String url;

    private RestTemplate restTemplate;

    public FlightRestService(String url, RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Flight> findAll() {
        LOGGER.debug("findAll()");
        ResponseEntity responseEntity = restTemplate.getForEntity(url, List.class);
        return (List<Flight>) responseEntity.getBody();
    }

    @Override
    public Optional<Flight> findById(Integer flightId) {
        LOGGER.debug("find by id ({})", flightId);
        ResponseEntity<Flight> responseEntity =
                restTemplate.getForEntity(url + "/" + flightId, Flight.class);
        return Optional.ofNullable(responseEntity.getBody());
    }

    @Override
    public Integer create(Flight flight) {
        LOGGER.debug("create ({})", flight);
        ResponseEntity responseEntity = restTemplate.postForEntity(url, flight, Integer.class);
        return (Integer) responseEntity.getBody();
    }

    @Override

    public int update(Flight flight) {
        LOGGER.debug("update({})", flight);
        // restTemplate.put(url, department); if method would be void
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Flight> entity = new HttpEntity<>(flight, headers);
        ResponseEntity<Integer> result = restTemplate.exchange(url, HttpMethod.PUT, entity, Integer.class);
        return result.getBody();
    }


    @Override
    public int delete(Integer flightId) {
        LOGGER.debug("delete({})", flightId);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Flight> entity = new HttpEntity<>(headers);
        ResponseEntity<Integer> result =
                restTemplate.exchange(url+"/"+flightId, HttpMethod.DELETE, entity, Integer.class);
        return result.getBody();
    }

}
