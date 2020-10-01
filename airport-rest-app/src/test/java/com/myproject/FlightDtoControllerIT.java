package com.myproject;

import com.myproject.model.dto.FlightDto;
import com.myproject.rest.FlightDtoController;
import com.myproject.serviceApi.FlightDtoService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@ExtendWith(MockitoExtension.class)
public class FlightDtoControllerIT {

    private  static final Logger LOGGER = LoggerFactory.getLogger(FlightDtoControllerIT.class);

    @InjectMocks
    private FlightDtoController flightDtoController;

    @Mock
    private FlightDtoService flightDtoService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(flightDtoController)
                .build();
    }

    @AfterEach
    public void end() {
        Mockito.verifyNoMoreInteractions(flightDtoService);
    }

    @Test
    public void shouldFindAllWithQuantityPassengersAndDateFilter() throws Exception {

        LocalDate dateFrom = LocalDate.of(2020,4,1);
        LocalDate dateTo = LocalDate.of(2020,5,1);
        Mockito.when(flightDtoService.findAllWithQuantityPassengersAndDateFilter(dateFrom, dateTo)).thenReturn(Arrays.asList(create(0), create(1)));

        LOGGER.debug("should find all with quantity passengers and date filter");

        getMockMvc("/flightsDto?dateFrom=" + dateFrom + "&dateTo=" + dateTo);

        Mockito.verify(flightDtoService).findAllWithQuantityPassengersAndDateFilter(dateFrom, dateTo);
    }

    @Test
    public void shouldFindAllWithQuantityPassengers() throws Exception {

        Mockito.when(flightDtoService.findAllWithQuantityPassengers()).thenReturn(Arrays.asList(create(0), create(1)));

        LOGGER.debug("should find all with quantity passengers");

        getMockMvc("/flightsDto/quantity");

        Mockito.verify(flightDtoService).findAllWithQuantityPassengers();
    }

    private MockMvc getMockMvc(String urlTemplate) throws Exception {
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateExample = localDate.format(formatter);

         mockMvc.perform(
                MockMvcRequestBuilders.get(urlTemplate)
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].flightId", Matchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].direction", Matchers.is("direction0")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].dateFlight", Matchers.is(dateExample)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].quantityPassengers", Matchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].flightId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].direction", Matchers.is("direction1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].dateFlight", Matchers.is(dateExample)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].quantityPassengers", Matchers.is(1)))
                ;;
        return mockMvc;
    }

    private FlightDto create(int index){

        FlightDto flightDto = new FlightDto();
        flightDto.setFlightId(index);
        flightDto.setDirection("direction"+index);
        flightDto.setDateFlight(LocalDate.now());
        flightDto.setQuantityPassengers(index);
        return flightDto;
    }
}



