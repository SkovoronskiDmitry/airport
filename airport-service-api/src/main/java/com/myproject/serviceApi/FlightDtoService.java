package com.myproject.serviceApi;

import com.myproject.model.dto.FlightDto;

import java.time.LocalDate;
import java.util.List;

public interface FlightDtoService {
    /**
     * Get all flights with quantity passengers.
     *
     * @return Flights list.
     */
    List<FlightDto> findAllWithQuantityPassengers();

    /**
     * Get all flights with quantity passengers and filter of date.
     *
     * @return Flights list.
     */
    List <FlightDto> findAllWithQuantityPassengersAndDateFilter(LocalDate dateFrom, LocalDate dateTo);

}
