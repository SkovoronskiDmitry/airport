package com.myproject.serviceApi;

import com.myproject.model.Passenger;

import java.util.List;
import java.util.Optional;

public interface PassengerService {
    /**
     * Find all passengers.
     * @return passengers list.
     */
    List<Passenger> findAll();

    /**
     * Find passengers by id.
     * @param passengerId Passenger id.
     * @return Passenger.
     */
    Optional<Passenger> findById(Integer passengerId);

    /**
     * Create new passenger.
     * @param passenger passenger.
     * @return persisted passenger id.
     */
    Integer create(Passenger passenger);

    /**
     * Update passenger.
     * @param passenger passenger.
     * @return number of updated record in the database.
     */
    int update(Passenger passenger);

    /**
     * Delete passenger.
     * @param passengerId passenger id.
     * @return number of deleted record in the database.
     */
    int delete(Integer passengerId);
}
