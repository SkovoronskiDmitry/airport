package com.myproject.webTest;

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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:app-context-test-web.xml"})
@Transactional
public class PassengerControllerIT {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void shouldReturnPassengerPage() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/passengers")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("passengers"))
                .andExpect(model().attribute("passengers", hasItem(
                        allOf(
                                hasProperty("passengerId", is(1)),
                                hasProperty("firstname", is("Michail")),
                                hasProperty("lastname", is("Ivanov")),
                                hasProperty("flightId", is(101))
                             )
                )))
                .andExpect(model().attribute("passengers", hasItem(
                        allOf(
                                hasProperty("passengerId", is(2)),
                                hasProperty("firstname", is("Nickolai")),
                                hasProperty("lastname", is("Kroch")),
                                hasProperty("flightId", is(105))
                        )
                )))
        ;
    }

    @Test
    public void shouldOpenEditPassengerPageById() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/passenger/1")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("passenger"))
                .andExpect(model().attribute("isNew", is(false)))
                .andExpect(model().attribute("passenger", hasProperty("passengerId", is(1))))
                .andExpect(model().attribute("passenger", hasProperty("firstname", is("Michail"))))
                .andExpect(model().attribute("passenger", hasProperty("lastname", is("Ivanov"))))
                .andExpect(model().attribute("passenger", hasProperty("flightId", is(101))))
                .andExpect(model().attribute("flights", hasItem(
                        allOf(
                                hasProperty("flightId", is(102))

                        )
                )));
    }

    @Test
    public void shouldReturnToPassengerPageIfPassengerNotFoundById() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.get("/passenger/13")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("passengers"));
    }
}
