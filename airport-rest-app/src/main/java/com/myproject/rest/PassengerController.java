package com.myproject.rest;

import com.myproject.serviceApi.PassengerService;
import com.myproject.model.Passenger;
import com.myproject.rest.exceptions.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static com.myproject.constants.RestConstants.PASSENGER_NOT_FOUND;
import static com.myproject.constants.RestConstants.PASSENGER_NOT_FOUND_BY_ID;

/**
 * Passenger Rest Controller
 */
@RestController
public class PassengerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PassengerController.class);

    private final PassengerService passengerService;

   public PassengerController(PassengerService passengerService) {
        this.passengerService = passengerService;
       LOGGER.debug("create rest app Passenger controller");
    }


    /**
     * Go to passengers list page
     *
     * @return view name
     */
    @GetMapping(value = "/passengers")
    public final Collection<Passenger> passengers(){
        LOGGER.debug("passengers()");

        return passengerService.findAll();
    }

    /**
     * find passenger by id
     * @param id
     * @return passenger
     */
    @GetMapping(value = "/passengers/{id}")
    public ResponseEntity<Passenger> findById(@PathVariable Integer id){
        LOGGER.debug("find passenger by id({})", id);

        Optional<Passenger> optional = passengerService.findById(id);
        return optional.isPresent()
                ? new ResponseEntity<>(optional.get(), HttpStatus.OK)
                : new ResponseEntity(
                        new ErrorResponse(PASSENGER_NOT_FOUND,
                                Arrays.asList(PASSENGER_NOT_FOUND_BY_ID + id)),
                HttpStatus.NOT_FOUND);
    }

    /**
     * create new passenger
     * @param passenger
     * @return id passenger
     */
    @PostMapping(path = "/passengers", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Integer> createPassenger(@RequestBody Passenger passenger) {

        LOGGER.debug("createPassenger({})", passenger);
        Integer id = passengerService.create(passenger);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    /**
     * Update passenger
     * @param passenger
     * @return id passenger
     */
    @PutMapping(value = "/passengers", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<Integer> updatePassenger(@RequestBody Passenger passenger) {

        LOGGER.debug("updatePassenger({})", passenger);
        int result = passengerService.update(passenger);
        return new ResponseEntity(result, HttpStatus.OK);

    }

    /**
     * delete passenger
     * @param id
     * @return result of delete
     */
    @DeleteMapping(value = "/passengers/{id}", produces = {"application/json"})
    public ResponseEntity<Integer> deletePassenger(@PathVariable Integer id) {

        int result = passengerService.delete(id);
        return new ResponseEntity(result, HttpStatus.OK);
    }
}
