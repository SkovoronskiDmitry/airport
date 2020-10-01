package com.myproject;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.model.Passenger;
import com.myproject.rest.PassengerController;
import com.myproject.rest.exceptions.CustomExceptionHandler;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static com.myproject.constants.PassengerConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:app-context-test.xml"})
public class PassengerControllerIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(PassengerControllerIT.class);

    public static final String PASSENGERS_ENDPOINT = "/passengers";

    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    MockMvcPassengerService passengerService =  new MockMvcPassengerService();

    @Autowired
    private PassengerController passengerController;

    @Autowired
    private CustomExceptionHandler customExceptionHandler;

    @BeforeEach
    public void before() {
        mockMvc = MockMvcBuilders.standaloneSetup(passengerController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .setControllerAdvice(customExceptionHandler)
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
    }

    @Test
    public void shouldFindAllPassengers() throws Exception {

        List<Passenger> passengers = passengerService.findAll();
        assertNotNull(passengers);
        assertTrue(passengers.size() > 0);
    }


    @Test
    public void shouldCreatePassenger() throws Exception {
        Passenger passenger = new Passenger();
        passenger.setFirstname(RandomStringUtils.randomAlphabetic(FIRST_NAME_SIZE));
        passenger.setLastname(RandomStringUtils.randomAlphabetic(LAST_NAME_SIZE));
        passenger.setFlightId(101);

        Integer id = passengerService.create(passenger);
        assertNotNull(id);
    }

    @Test
    public void shouldFindPassengerByPassengerId() throws Exception {

        // given
        Passenger passenger = new Passenger();
        passenger.setFirstname(RandomStringUtils.randomAlphabetic(FIRST_NAME_SIZE));
        passenger.setLastname(RandomStringUtils.randomAlphabetic(LAST_NAME_SIZE));
        passenger.setFlightId(101);

        Integer id = passengerService.create(passenger);
        assertNotNull(id);

        // when
        Optional<Passenger> optionalPassenger = passengerService.findById(id);

        // then
        Assertions.assertTrue(optionalPassenger.isPresent());
        assertEquals(optionalPassenger.get().getPassengerId(), id);
        assertEquals(optionalPassenger.get().getFirstname(), passenger.getFirstname());
        assertEquals(optionalPassenger.get().getLastname(), passenger.getLastname());
        assertEquals(optionalPassenger.get().getFlightId(), passenger.getFlightId());

    }


    @Test
    public void shouldUpdatePassenger() throws Exception {

        // given
        Passenger passenger = new Passenger();
        passenger.setFirstname(RandomStringUtils.randomAlphabetic(FIRST_NAME_SIZE));
        passenger.setLastname(RandomStringUtils.randomAlphabetic(LAST_NAME_SIZE));
        passenger.setFlightId(101);

        Integer id = passengerService.create(passenger);
        assertNotNull(id);

        Optional<Passenger> passengerOptional = passengerService.findById(id);
        Assertions.assertTrue(passengerOptional.isPresent());

        passengerOptional.get().
                setFirstname(RandomStringUtils.randomAlphabetic(FIRST_NAME_SIZE));

        // when
        int result = passengerService.update(passengerOptional.get());

        // then
        assertTrue(1 == result);

        Optional<Passenger> updatedPassengerOptional = passengerService.findById(id);
        Assertions.assertTrue(updatedPassengerOptional.isPresent());
        assertEquals(updatedPassengerOptional.get().getPassengerId(), id);
        assertEquals(updatedPassengerOptional.get().getFirstname(), passengerOptional.get().getFirstname());

    }

    @Test
    public void shouldDeletePassenger() throws Exception {
        // given
        Passenger passenger = new Passenger();
        passenger.setFirstname(RandomStringUtils.randomAlphabetic(FIRST_NAME_SIZE));
        passenger.setLastname(RandomStringUtils.randomAlphabetic(LAST_NAME_SIZE));
        passenger.setFlightId(101);

        Integer id = passengerService.create(passenger);
        assertNotNull(id);

        List<Passenger> passengers = passengerService.findAll();
        assertNotNull(passengers);

        // when
        int result = passengerService.delete(id);

        // then
        assertTrue(1 == result);

        List<Passenger> currentPassengers = passengerService.findAll();
        assertNotNull(currentPassengers);

        assertTrue(passengers.size()-1 == currentPassengers.size());
    }



    private class MockMvcPassengerService {

        public List<Passenger> findAll() throws Exception {
            LOGGER.debug("findAll()");
            MockHttpServletResponse response = mockMvc.perform(get(PASSENGERS_ENDPOINT)
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
                    .andReturn().getResponse();
            assertNotNull(response);

            return objectMapper.readValue(response.getContentAsString(), new TypeReference<List<Passenger>>() {});
        }

        private Optional<Passenger> findById(Integer passengerId) throws Exception {

            LOGGER.debug("findById({})", passengerId);
            MockHttpServletResponse response = mockMvc.perform(get(PASSENGERS_ENDPOINT + "/" + passengerId)
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
                    .andReturn().getResponse();
            return Optional.of(objectMapper.readValue(response.getContentAsString(), Passenger.class));
        }

        private Integer create(Passenger passenger) throws Exception {

            LOGGER.debug("create({})", passenger);
            MockHttpServletResponse response =
                    mockMvc.perform(post(PASSENGERS_ENDPOINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(passenger))
                            .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isOk())
                            .andReturn().getResponse();
            return objectMapper.readValue(response.getContentAsString(), Integer.class);
        }

        private int update(Passenger passenger) throws Exception {

            LOGGER.debug("create({})", passenger);
            MockHttpServletResponse response =
                    mockMvc.perform(put(PASSENGERS_ENDPOINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(passenger))
                            .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isOk())
                            .andReturn().getResponse();
            return objectMapper.readValue(response.getContentAsString(), Integer.class);
        }

        private int delete(Integer passengerId) throws Exception {

            LOGGER.debug("delete(id:{})", passengerId);
            MockHttpServletResponse response = mockMvc.perform(
                    MockMvcRequestBuilders.delete(new StringBuilder(PASSENGERS_ENDPOINT).append("/")
                            .append(passengerId).toString())
                            .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
                    .andReturn().getResponse();

            return objectMapper.readValue(response.getContentAsString(), Integer.class);
        }

        public List<Passenger> findByPassengerId(int passengerId) throws Exception {

            LOGGER.debug("findByDepartmentId({})", passengerId);
            MockHttpServletResponse response = mockMvc.perform(get(PASSENGERS_ENDPOINT )
                    .param("departmentId", String.valueOf(passengerId))
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
                    .andReturn().getResponse();
            return objectMapper.readValue(response.getContentAsString(), new TypeReference<List<Passenger>>() {});
        }
    }
}

