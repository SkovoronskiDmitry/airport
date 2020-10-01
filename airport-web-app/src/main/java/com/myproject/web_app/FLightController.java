package com.myproject.web_app;

import com.myproject.serviceApi.FlightDtoService;
import com.myproject.serviceApi.FlightService;
import com.myproject.model.Flight;
import com.myproject.model.dto.FlightDto;
import com.myproject.web_app.validators.FlightValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


/**
 * Flight controller.
 */
@Controller
@RequestMapping("/flights")
public class FLightController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FLightController.class);

    @Autowired
    private FlightService flightService;

    @Autowired
    private FlightDtoService flightDtoService;

    @Autowired
    FlightValidator flightValidator;




    /**
     * Goto FLights list page.
     *
     * @return view name
     */
    @GetMapping
    public final String flights(@RequestParam (value = "dateFrom", required = false)
                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
                                @RequestParam (value = "dateTo", required = false)
                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
                                Model model){

        if (dateFrom!= null && dateTo != null){

            LOGGER.debug("Find flights between dates. dateFrom = {}, dateTo = {}", dateFrom, dateTo);
            List<FlightDto> flightsDtoList= flightDtoService.findAllWithQuantityPassengersAndDateFilter(dateFrom, dateTo);
            model.addAttribute("flights", flightsDtoList);
        }
        else{

            LOGGER.debug("Find all flights with quantity passengers");
            List<FlightDto> flightDtoList =flightDtoService.findAllWithQuantityPassengers();

            model.addAttribute("flights", flightDtoList );
        }
        return "flights";
    }

    /**
     * Go to edit flight page.
     *
     * @return view name
     */
    @GetMapping(value = "/{flightId}")
    public final String gotoEditFlightPage(@PathVariable Integer flightId, Model model) {

        LOGGER.debug("gotoEditFlightPage({},{})", flightId, model);
        Optional<Flight> optionalFlight = flightService.findById(flightId);
        if (optionalFlight.isPresent()) {
            model.addAttribute("isNew", false);
            model.addAttribute("flight", optionalFlight.get());
            return "flight";
        } else {
            return "redirect:flights";
        }
    }

    /**
     * Update flight.
     *
     * @param flight flight with filled data.
     * @param result binding result
     * @return view name
     */
    @PostMapping(value = "/{id}")
    public String updateFlight(@ModelAttribute("flight")@Valid Flight flight,
                               BindingResult result, Model model) {

        model.addAttribute("flightEntity", flight);
        LOGGER.debug("updateFlight({}, {})", flight, result);
        flightValidator.validate(flight, result);
        if (result.hasErrors()) {
            return "flight";
        } else {
            this.flightService.update(flight);
            return "redirect:/flights";
        }
    }

    /**
     * Goto new flight page.
     *
     * @return view name
     */
    @GetMapping(value = "/add")
    public final String gotoAddFlightPage(Model model) {
        LOGGER.debug("gotoAddFlightPage({})", model);
        model.addAttribute("flight", new Flight());
        return "flight";
    }

    /**
     * Persist new flight into persistence storage.
     *
     * @param flight new flight with filled data.
     * @param result binding result.
     * @return view name
     */
    @PostMapping(value = "/add")
    public String addFlight(@Valid Flight flight,
                            BindingResult result) {

        LOGGER.debug("addFlight({}, {})", flight, result);
        flightValidator.validate(flight, result);
        if (result.hasErrors()) {
            return "flight";
        } else {
            this.flightService.create(flight);
            return "redirect:/flights";
        }
    }


    /**
     * Delete flight.
     *
     * @return view name
     */
    @GetMapping(value = "/{flightId}/delete")
    public final String deleteFlightById(@PathVariable Integer flightId, Model model) {

        LOGGER.debug("delete({},{})", flightId, model);

        flightService.delete(flightId);

        return "redirect:/flights";
    }

}