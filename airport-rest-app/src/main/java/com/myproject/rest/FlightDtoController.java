package com.myproject.rest;

import com.myproject.serviceApi.FlightDtoService;
import com.myproject.model.dto.FlightDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Collection;

/**
 * FlightDto rest Controller
 */
@RestController
@RequestMapping(value = "/flightsDto")
public class FlightDtoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlightDtoController.class);

    private final FlightDtoService flightDtoService;

    public FlightDtoController(FlightDtoService flightDtoService) {
        this.flightDtoService = flightDtoService;
        LOGGER.debug("create rest app Flight DTO controller");
    }


    /**
     * Find flights with date Filter
     * @param dateFrom
     * @param dateTo
     * @return Collection of flights with date filter
     */
    @GetMapping
    public Collection<FlightDto> findDtoWithDateFilter
    (@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom
     , @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo) {

        LOGGER.debug("find with date filter({},{})", dateFrom, dateTo);
        return flightDtoService.findAllWithQuantityPassengersAndDateFilter(dateFrom, dateTo);
    }
    /**
     * Get flight with quantity passengers.
     *
     * @return Flights Dtos collection.
     */
    @GetMapping("/quantity")
    public final Collection<FlightDto> flightWithQuantityPassengers() {

        LOGGER.debug("flights find all with quantity passengers()");
        return flightDtoService.findAllWithQuantityPassengers();
    }

}
