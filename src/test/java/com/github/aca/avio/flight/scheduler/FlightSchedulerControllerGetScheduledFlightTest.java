package com.github.aca.avio.flight.scheduler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static com.github.aca.avio.flight.scheduler.IcaoAirportCode.EBAW;
import static com.github.aca.avio.flight.scheduler.IcaoAirportCode.EBBR;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FlightSchedulerController.class)
class FlightSchedulerControllerGetScheduledFlightTest {
    private static final String FLIGHT_NUMBER = "AR9999";
    private static final String DEPARTURE_TIME = "2022-12-12T12:00:00Z";
    private static final String ARRIVAL_TIME = "2022-12-12T18:00:00-05:00";
    private static final String LOCAL_ARRIVAL_TIME = "2022-12-12T23:00:00Z";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FlightSchedulerService flightSchedulerService;

    @Test
    void shouldReturnScheduledFlightForGivenUuid() throws Exception {
        var scheduledFlight = new ScheduledFlight(FLIGHT_NUMBER, EBAW, EBBR, Instant.parse(DEPARTURE_TIME), Instant.parse(ARRIVAL_TIME));
        when(flightSchedulerService.getScheduledFlight(scheduledFlight.getUuid())).thenReturn(scheduledFlight);

        this.mockMvc.perform(get("/api/flight-scheduler/schedule/{uuid}", scheduledFlight.getUuid())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").value(scheduledFlight.getUuid().toString()))
                .andExpect(jsonPath("$.flightNumber").value(FLIGHT_NUMBER))
                .andExpect(jsonPath("$.departure").value(EBAW.name()))
                .andExpect(jsonPath("$.destination").value(EBBR.name()))
                .andExpect(jsonPath("$.departureTime").value(DEPARTURE_TIME))
                .andExpect(jsonPath("$.arrivalTime").value(LOCAL_ARRIVAL_TIME));

        verify(flightSchedulerService).getScheduledFlight(scheduledFlight.getUuid());
    }
}