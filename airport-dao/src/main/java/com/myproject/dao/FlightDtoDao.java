package com.myproject.dao;

import com.myproject.model.dto.FlightDto;

import java.time.LocalDate;
import java.util.List;

public interface FlightDtoDao {
    /**
    * Get all flights with quantity passengers.
    *
    * @return Flights list.
    */
        List <FlightDto> findAllWithQuantityPassengers();

    /**
     * Get all flights with quantity passengers and Date Filter.
     *
     * @return Flights list.
     */
    List <FlightDto> findAllWithQuantityPassengersAndDateFilter (LocalDate dateFrom, LocalDate dateTo);

}
