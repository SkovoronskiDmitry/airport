package com.myproject.rest;

import com.myproject.serviceApi.FlightService;
import com.myproject.model.Flight;
import com.myproject.rest.exceptions.FlightNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * Flight rest Controller
 */
@RestController
@RequestMapping("/flights")
public class FlightController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlightController.class);

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }


    /**
     * Go to flights list page
     *
     * @return view name
     */
    @GetMapping
    public final Collection<Flight> flights(){
        LOGGER.debug("flights()");
        return flightService.findAll();
    }

    /**
     * find flight by flightId
     * @param flightId
     * @return flight
     */
    @GetMapping(value = "/{flightId}")
     public Flight findById(@PathVariable Integer flightId){
            LOGGER.debug("find flight by id({})", flightId);

           return flightService.findById(flightId).orElseThrow(() -> new FlightNotFoundException(flightId));
    }

    /**
     * create new flight
     * @param flight
     * @return id flight
     */
    @PostMapping(consumes = "application/json", produces = "application/json")
    public Integer createFlight(@RequestBody Flight flight) {

        LOGGER.debug("createFlight({})", flight);
        return  flightService.create(flight);
    }

    /**
     * Update flight
     * @param flight
     * @return id flight
     */
    @PutMapping(consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<Integer> updateFlight(@RequestBody Flight flight) {

        LOGGER.debug("updateFlight({})", flight);
        int result = flightService.update(flight);
        return new ResponseEntity(result, HttpStatus.OK);

    }

    /**
     * delete flight
     * @param flightsId
     * @return result of delete
     */
    @DeleteMapping(value = "/{flightsId}",produces = {"application/json"})
    public ResponseEntity<Integer> deleteFlight(@PathVariable Integer flightsId) {

        LOGGER.debug("delete()");
        int result = flightService.delete(flightsId);
        return  new ResponseEntity(result, HttpStatus.OK);

    }

}
