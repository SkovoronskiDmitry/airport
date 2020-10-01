package com.myproject.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * POJO FlightDto for model.
 */
@SuppressFBWarnings(value = { "EI_EXPOSE_REP", "EI_EXPOSE_REP2" }
        , justification = "I prefer to suppress these FindBugs warnings")
public class FlightDto {

    /**
     * Constructor without arguments.
     */
    public FlightDto() {
    }

    /**
     * Constructor with Flight number, name of route flight and date
     * @param flightId Flight number
     * @param direction name of route flight
     * @param dateFlight Date of flight
     */
    public FlightDto(Integer flightId, String direction, LocalDate dateFlight) {
        this.flightId = flightId;
        this.direction = direction;
        this.dateFlight = dateFlight;
    }

    /**
     * Flight number
     */
    private Integer flightId;

    /**
     * Name of flight direction
     */
    private String direction;

    /**
     * Date of flight
     */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateFlight;

     /**
     * Quantity of passengers
     */
    private Integer quantityPassengers;

    /**
     * Return Flight number
     * @return flightId
     */
    public Integer getFlightId() {
        return flightId;
    }

    /**
     * Set Flight number
     * @param flightId Flight number
     */
    public void setFlightId(Integer flightId) {
        this.flightId = flightId;
    }

    /**
     * Return name of direction route flight
     * @return direction
     */
    public String getDirection() {
        return direction;
    }

    /**
     * Set name of direction route flight
     * @param direction name of direction route flight
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     * Return Date of flight
     * @return dateFlight
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public LocalDate getDateFlight() {
        return dateFlight;
    }

    /**
     * Set Date of flight
     * @param dateFlight
     */
    public void setDateFlight(LocalDate dateFlight) {
        this.dateFlight = dateFlight;
    }

    /**
     * Return Quantity of passengers
     * @return quantityPassengers
     */
    public Integer getQuantityPassengers() {
        return quantityPassengers;
    }

    /**
     * Set quantity of passengers
     * @param quantityPassengers
     */
    public void setQuantityPassengers(Integer quantityPassengers) {
        this.quantityPassengers = quantityPassengers;
    }

}
