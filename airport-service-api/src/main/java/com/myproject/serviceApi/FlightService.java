package com.myproject.serviceApi;

import com.myproject.model.Flight;

import java.util.List;
import java.util.Optional;

public interface FlightService {

    /**
     * Find all flights.
     * @return flights list.
     */
    List<Flight> findAll();

    /**
     * Find flight by id.
     * @param flightId Flight id.
     * @return Flight.
     */
    Optional<Flight> findById(Integer flightId);

    /**
     * Create new Flight.
     * @param flight Flight.
     * @return persisted Flight id.
     */
    Integer create(Flight flight);

    /**
     * Update Flight.
     * @param flight Flight.
     * @return number of updated record in the database.
     */
    int update(Flight flight);

    /**
     * Delete Flight.
     * @param flightId Flight id.
     * @return number of deleted record in the database.
     */
    int delete(Integer flightId);


}
