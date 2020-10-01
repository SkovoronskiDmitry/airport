package com.myproject.service_rest;

import com.myproject.serviceApi.FlightDtoService;
import com.myproject.model.dto.FlightDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class FlightDtoRestService implements FlightDtoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlightDtoRestService.class);

    private String url;

    private RestTemplate restTemplate;

    public FlightDtoRestService(String url, RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
        LOGGER.debug("create Flight Dto Rest SERVICE");
    }

    @Override
    public List<FlightDto> findAllWithQuantityPassengers() {
        LOGGER.debug("quantity passengers()");
        ResponseEntity<List> responseEntity = restTemplate.getForEntity(url + "/quantity", List.class);
        return responseEntity.getBody();
    }

    @Override
    public List<FlightDto> findAllWithQuantityPassengersAndDateFilter(LocalDate dateFrom, LocalDate dateTo) {
        LOGGER.debug("find with date filter ({}, {}})",dateFrom,  dateTo);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("dateFrom", dateFrom)
                .queryParam("dateTo", dateTo);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<List> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, List.class);
        return  response.getBody();
    }
}
