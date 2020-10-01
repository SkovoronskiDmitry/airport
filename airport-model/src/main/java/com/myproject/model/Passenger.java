package com.myproject.model;

/**
 *  POJO Passenger for model.
 */
public class Passenger {

    /**
     * Constructor without arguments.
     */
    public Passenger() {
    }

    /**
     * Constructor with  Passenger id.
     * @param passengerId Passenger id.
     */
    public Passenger(Integer passengerId) {

        this.passengerId = passengerId;
    }

    /**
     * Passenger id.
     */
    private Integer passengerId;

    /**
     * Passenger firstname.
     */
    private String firstname;

    /**
     * Passenger lastname.
     */
    private String lastname;

    /**
     * Flight id.
     */
    private Integer flightId;

    /**
     * Return Passenger id.
     * @return passengerId.
     */
    public Integer getPassengerId() {
        return passengerId;
    }
    /**
     * Set Passenger id.
     * @param passengerId Passenger id.
     */
    public void setPassengerId(Integer passengerId) {
        this.passengerId = passengerId;
    }

     /**
     * Return Passenger firstname.
     * @return firstname.
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * Set Passenger firstname.
     * @param firstname Passenger firstname.
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * Return Passenger lastname.
     * @return lastname.
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * Set Passenger lastname.
     * @param lastname Passenger lastname.
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * Return Flight id.
     * @return flightId.
     */
    public Integer getFlightId() {
        return flightId;
    }

    /**
     * Set  Flight id.
     * @param flightId Flight id.
     */
    public void setFlightId(Integer flightId) {
        this.flightId = flightId;
    }
}
