package com.github.aca.avio.flight.scheduler.rest;

import com.github.aca.avio.flight.scheduler.domain.ScheduledFlight;
import com.github.aca.avio.flight.scheduler.service.FlightSchedulerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static com.github.aca.avio.flight.scheduler.domain.IcaoAirportCode.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FlightSchedulerController.class)
class FlightSchedulerControllerSearchScheduledFlightsTest {
    private static final String FLIGHT_NUMBER = "AR1234";
    private static final String DEPARTURE_TIME_1 = "2022-12-12T12:00:00Z";
    private static final String ARRIVAL_TIME_1 = "2022-12-12T18:00:00-05:00";
    private static final String LOCAL_ARRIVAL_TIME_1 = "2022-12-12T23:00:00Z";
    private static final String DEPARTURE_TIME_2 = "2023-12-12T12:00:00Z";
    private static final String ARRIVAL_TIME_2 = "2023-12-12T18:00:00-05:00";
    private static final String LOCAL_ARRIVAL_TIME_2 = "2023-12-12T23:00:00Z";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FlightSchedulerService flightSchedulerService;

    @Test
    void shouldReturnScheduledFlightsForGivenFlightNumberRequestParam() throws Exception {
        ScheduledFlight scheduledFlight1 = new ScheduledFlight(FLIGHT_NUMBER, EBAW, EBBR, Instant.parse(DEPARTURE_TIME_1), Instant.parse(ARRIVAL_TIME_1));
        ScheduledFlight scheduledFlight2 = new ScheduledFlight(FLIGHT_NUMBER, SAEZ, SAME, Instant.parse(DEPARTURE_TIME_2), Instant.parse(ARRIVAL_TIME_2));
        when(flightSchedulerService.searchScheduledFlights(FLIGHT_NUMBER)).thenReturn(List.of(scheduledFlight1, scheduledFlight2));

        this.mockMvc.perform(get("/api/flight-scheduler/schedule/search?flightNumber=AR1234")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].uuid").value(scheduledFlight1.getUuid().toString()))
                .andExpect(jsonPath("$[0].flightNumber").value(FLIGHT_NUMBER))
                .andExpect(jsonPath("$[0].departure").value(EBAW.name()))
                .andExpect(jsonPath("$[0].destination").value(EBBR.name()))
                .andExpect(jsonPath("$[0].departureTime").value(DEPARTURE_TIME_1))
                .andExpect(jsonPath("$[0].arrivalTime").value(LOCAL_ARRIVAL_TIME_1))
                .andExpect(jsonPath("$[1].uuid").value(scheduledFlight2.getUuid().toString()))
                .andExpect(jsonPath("$[1].flightNumber").value(FLIGHT_NUMBER))
                .andExpect(jsonPath("$[1].departure").value(SAEZ.name()))
                .andExpect(jsonPath("$[1].destination").value(SAME.name()))
                .andExpect(jsonPath("$[1].departureTime").value(DEPARTURE_TIME_2))
                .andExpect(jsonPath("$[1].arrivalTime").value(LOCAL_ARRIVAL_TIME_2));

        verify(flightSchedulerService).searchScheduledFlights(FLIGHT_NUMBER);
    }

    @Test
    void shouldReturnEmptyListWhenNoScheduledFlightsFoundForGivenFlightNumberRequestParam() throws Exception {
        when(flightSchedulerService.searchScheduledFlights(FLIGHT_NUMBER)).thenReturn(List.of());

        this.mockMvc.perform(get("/api/flight-scheduler/schedule/search?flightNumber=AR1234")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(flightSchedulerService).searchScheduledFlights(FLIGHT_NUMBER);
    }
}