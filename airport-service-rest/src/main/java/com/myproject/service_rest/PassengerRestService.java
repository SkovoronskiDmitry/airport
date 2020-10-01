package com.myproject.service_rest;

import com.myproject.serviceApi.PassengerService;
import com.myproject.model.Passenger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class PassengerRestService implements PassengerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PassengerRestService.class);

    private String url;

    private RestTemplate restTemplate;

    public PassengerRestService(String url, RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
        LOGGER.debug("create Passenger Rest SERVICE");
    }


    @Override
    public List<Passenger> findAll() {

        LOGGER.debug("find all passengers");
        ResponseEntity responseEntity = restTemplate.getForEntity(url,List.class);
        return (List<Passenger>) responseEntity.getBody();
    }

    @Override
    public Optional<Passenger> findById(Integer passengerId) {
        LOGGER.debug("find passenger by id");
        ResponseEntity<Passenger> responseEntity = restTemplate.getForEntity(url + "/" + passengerId,Passenger.class);
        return Optional.ofNullable(responseEntity.getBody());
    }

    @Override
    public Integer create(Passenger passenger) {
        LOGGER.debug("create({})", passenger);
        ResponseEntity responseEntity = restTemplate.postForEntity(url, passenger, Integer.class);
        return (Integer)responseEntity.getBody();
    }

    @Override
    public int update(Passenger passenger) {
        LOGGER.debug("update({})", passenger);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Passenger> entity = new HttpEntity<>(passenger,headers);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        ResponseEntity <Integer> responseEntity = restTemplate.exchange(url , HttpMethod.PUT, entity,Integer.class );
        return responseEntity.getBody();
    }

    @Override
    public int delete(Integer passengerId) {
        LOGGER.debug("delete passenger with id ({})", passengerId);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Integer> entity = new HttpEntity<>(passengerId,headers);
        ResponseEntity responseEntity = restTemplate.exchange(url +"/" + passengerId, HttpMethod.DELETE,entity,Integer.class);

        return (Integer) responseEntity.getBody();

    }
}
