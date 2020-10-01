package com.myproject.webTest;

import com.myproject.model.Flight;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.annotation.ApplicationScope;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:app-context-test-web.xml"})
@ApplicationScope
@Transactional
public class FlightControllerIT {

    private final String FLIGHTS_URL = "/flights";

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void shouldReturnFlightsPage() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(FLIGHTS_URL)
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("flights"))
                .andExpect(model().attribute("flights", hasItem(
                        allOf(
                                hasProperty("flightId", is(101)),
                                hasProperty("direction", is("Minsk-Brest")),
                                hasProperty("dateFlight", is(convertToLocalDate("2020-03-11")))
                        )
                )))
                .andExpect(model().attribute("flights", hasItem(
                        allOf(
                                hasProperty("flightId", is(102)),
                                hasProperty("direction", is("Minsk-Frankfurt")),
                                hasProperty("dateFlight", is(convertToLocalDate("2020-03-12")))
                        )
                )))
        ;
    }
    @Test
    public void shouldReturnFlightsWithDateFilter() throws Exception {

        LocalDate dateFrom = LocalDate.now().minusDays(18);
        LocalDate dateTo= LocalDate.now().plusDays(7);

        mockMvc.perform(
                MockMvcRequestBuilders.get(FLIGHTS_URL)
                        .param("dateFrom", dateFrom.toString())
                        .param("dateTo", dateTo.toString())
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("flights"))
                .andExpect(model().attribute("flights", hasItem(
                        allOf(
                                hasProperty("flightId", is(103)),
                                hasProperty("direction", is("Minsk-Brest")),
                                hasProperty("dateFlight", is(convertToLocalDate("2020-04-10")))
                        )
                )));
    }

    @Test
    public void shouldOpenEditFlightsPageById() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.get( FLIGHTS_URL+"/101")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("flight"))
                .andExpect(model().attribute("isNew", is(false)))
                .andExpect(model().attribute("flight", hasProperty("flightId", is(101))))
                .andExpect(model().attribute("flight", hasProperty("direction", is("Minsk-Brest"))))
                .andExpect(model().attribute("flight", hasProperty("dateFlight", is(convertToLocalDate("2020-03-11")))))
                ;
    }

    @Test
    public void shouldReturnToFlightsPageIfFlightNotFoundById() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.get(FLIGHTS_URL+"/13")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("flights"));
    }
    @Test
    public void shouldUpdateFlightAfterEdit() throws Exception {

        Flight flight = create(1);

        mockMvc.perform(
                MockMvcRequestBuilders.post(FLIGHTS_URL + "/101")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("flightId", String.valueOf(flight.getFlightId()))
                        .param("direction", flight.getDirection())
                        .param("dateFlight", String.valueOf(flight.getDateFlight()))
                        .sessionAttr("flight", flight)
        ).andExpect(status().isFound())
                .andExpect(view().name("redirect:" + FLIGHTS_URL))
                .andExpect(redirectedUrl(FLIGHTS_URL));
    }

    @Test
    public void shouldOpenNewFlightPage() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(FLIGHTS_URL + "/add")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("flight"))
                .andExpect(model().attribute("isNew", is(true)))
                .andExpect(model().attribute("flight", isA(Flight.class)));
    }

    @Test
    public void shouldAddNewFlight() throws Exception {

        Flight flight = create(1);

        mockMvc.perform(
                MockMvcRequestBuilders.post(FLIGHTS_URL + "/add")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("flightId", String.valueOf(flight.getFlightId()))
                        .param("direction", flight.getDirection())
                        .param("dateFlight",("2020-04-10"))
        ).andExpect(status().isFound())
                .andExpect(redirectedUrl(FLIGHTS_URL));
    }


    @Test
    public void shouldDeleteFlight() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.get(FLIGHTS_URL + "/1/delete")
        ).andExpect(status().isFound())
                .andExpect(view().name("redirect:" + FLIGHTS_URL))
                .andExpect(redirectedUrl(FLIGHTS_URL));
    }

    private Flight create(int index){

        Flight flight = new Flight();
        flight.setFlightId(index);
        flight.setDirection("Minsk-" + index);
        flight.setDateFlight(LocalDate.now());
        return flight;
    }
    private LocalDate convertToLocalDate(String dateAdded) {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDateForId = LocalDate.parse(dateAdded, dtf);
        return localDateForId;
    }
}
