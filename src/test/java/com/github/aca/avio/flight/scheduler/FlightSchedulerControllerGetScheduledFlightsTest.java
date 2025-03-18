package com.github.aca.avio.flight.scheduler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FlightSchedulerController.class)
class FlightSchedulerControllerGetScheduledFlightsTest {
    private static final UUID UUID_1 = UUID.randomUUID();
    private static final String FLIGHT_NUMBER_1 = "AR9999";
    private static final String DEPARTURE_1 = "ABCD";
    private static final String DESTINATION_1 = "EFGH";
    private static final UUID UUID_2 = UUID.randomUUID();
    private static final String FLIGHT_NUMBER_2 = "AR1111";
    private static final String DEPARTURE_2 = "IJKL";
    private static final String DESTINATION_2 = "MNOP";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FlightSchedulerService flightSchedulerService;

    @Test
    void shouldReturnScheduledFlights() throws Exception {
        var scheduledFlightDto1 = new ScheduledFlightDto(UUID_1, FLIGHT_NUMBER_1, DEPARTURE_1, DESTINATION_1);
        var scheduledFlightDto2 = new ScheduledFlightDto(UUID_2, FLIGHT_NUMBER_2, DEPARTURE_2, DESTINATION_2);
        when(flightSchedulerService.getScheduledFlights()).thenReturn(List.of(scheduledFlightDto1, scheduledFlightDto2));

        this.mockMvc.perform(get("/api/flight-scheduler/schedule")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].uuid").value(UUID_1.toString()))
                .andExpect(jsonPath("$[0].flightNumber").value(FLIGHT_NUMBER_1))
                .andExpect(jsonPath("$[0].departure").value(DEPARTURE_1))
                .andExpect(jsonPath("$[0].destination").value(DESTINATION_1))
                .andExpect(jsonPath("$[1].uuid").value(UUID_2.toString()))
                .andExpect(jsonPath("$[1].flightNumber").value(FLIGHT_NUMBER_2))
                .andExpect(jsonPath("$[1].departure").value(DEPARTURE_2))
                .andExpect(jsonPath("$[1].destination").value(DESTINATION_2));

        verify(flightSchedulerService).getScheduledFlights();
    }

    @Test
    void shouldReturnEmptyListWhenNoScheduledFlights() throws Exception {
        when(flightSchedulerService.getScheduledFlights()).thenReturn(List.of());

        this.mockMvc.perform(get("/api/flight-scheduler/schedule")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(flightSchedulerService).getScheduledFlights();
    }

}