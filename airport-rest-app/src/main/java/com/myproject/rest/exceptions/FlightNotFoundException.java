package com.myproject.rest.exceptions;

public class FlightNotFoundException extends RuntimeException{
    public FlightNotFoundException(Integer id) {
        super("Flight not found for id: " + id);
    }
}
