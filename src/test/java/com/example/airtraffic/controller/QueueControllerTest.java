package com.example.airtraffic.controller;

import com.example.airtraffic.model.Aircraft;
import com.example.airtraffic.model.AircraftSize;
import com.example.airtraffic.model.AircraftType;
import com.example.airtraffic.service.QueueManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = QueueController.class)
public class QueueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private QueueManager mockQueueManager;

    private Aircraft aircraft = getAircraft();
    private ObjectMapper mapper = new ObjectMapper();

    @Before
    @SneakyThrows
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(mockQueueManager.dequeue())
                .thenReturn(Optional.of(aircraft));
        when(mockQueueManager.listQueued())
                .thenReturn(Lists.list(aircraft));

        this.mockMvc = MockMvcBuilders.standaloneSetup(new QueueController(mockQueueManager)).build();
    }

    @Test
    public void testInsertAircraft() throws Exception {
        this.mockMvc
                .perform(
                        post("/aircraft/insert")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(aircraft)))
                .andExpect(
                        status().isOk());
    }

    @Test
    public void testRemoveAircraft() throws Exception {
        this.mockMvc
                .perform(
                        get("/aircraft/remove"))
                .andExpect(
                        status().isOk())
                .andExpect(
                        content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(
                        // TODO use json result matcher
                        content().string(
                                mapper.writeValueAsString(
                                        aircraft)));
        ;
    }

    @Test
    public void testListQueuedAircrafts() throws Exception {
        this.mockMvc
                .perform(
                        get("/aircraft/list"))
                .andExpect(
                        status().isOk())
                .andExpect(
                        content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(
                        // TODO use json result matcher
                        content().string(
                                mapper.writeValueAsString(
                                        Lists.list(aircraft))));
        ;
    }

    private Aircraft getAircraft() {
        return Aircraft.builder()
                .id(1)
                .type(AircraftType.Cargo)
                .size(AircraftSize.SMALL)
                .queueTimestamp(System.currentTimeMillis())
                .build();
    }
}
