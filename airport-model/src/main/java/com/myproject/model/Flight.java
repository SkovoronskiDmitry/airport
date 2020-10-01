package com.myproject.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * POJO Flight for model.
 */

public class Flight {

    /**
     * Constructor without arguments.
     */
    public Flight() {
    }

    /**
     * Constructor with Flight id, name of route flight and date.
     * @param flightId Flight id.
     * @param direction name of direction flight.
     * @param dateFlight Date of flight.
     */
    public Flight(Integer flightId, String direction, LocalDate dateFlight) {
        this.flightId = flightId;
        this.direction = direction;
        this.dateFlight = dateFlight;
    }

    /**
     * Flight id.
     */
    private Integer flightId;

    /**
     * Name of flight direction .
     */
    private String direction;

    /**
     * Date of flight.
     */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateFlight;

       /**
     * Return Flight id.
     * @return flightId.
     */
    public Integer getFlightId() {
        return flightId;
    }

    /**
     * Set Flight id.
     * @param flightId Flight id.
     */
    public void setFlightId(Integer flightId) {
        this.flightId = flightId;
    }

    /**
     * Return name of direction flight.
     * @return direction.
     */
    public String getDirection() {
        return direction;
    }

    /**
     * Set name of route flight.
     * @param direction name of direction flight.
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     * Return Date of flight.
     * @return date Date of flight.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public LocalDate getDateFlight() {
        return dateFlight;
    }

    /**
     * Set Date of flight.
     * @param dateFlight Date of flight.
     */
    public void setDateFlight(LocalDate dateFlight) {
        this.dateFlight = dateFlight;
    }

}
