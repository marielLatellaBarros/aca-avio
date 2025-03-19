package com.github.aca.avio.flight.scheduler;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static com.github.aca.avio.flight.scheduler.IcaoAirportCode.EBAW;
import static com.github.aca.avio.flight.scheduler.IcaoAirportCode.SAME;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FlightSchedulerController.class)
class FlightSchedulerControllerScheduleTest {
    private static final String FLIGHT_NUMBER = "AR9999";
    private static final String DEPARTURE_TIME = "2022-12-12T12:00:00Z";
    private static final String ARRIVAL_TIME = "2022-12-12T18:00:00-05:00";
    private static final String LOCAL_ARRIVAL_TIME = "2022-12-12T23:00:00Z";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FlightSchedulerService flightSchedulerService;

    @Test
    void shouldReturnScheduledFlight() throws Exception {
        var flightScheduleRequest = new FlightScheduleRequest(FLIGHT_NUMBER, EBAW, SAME, Instant.parse(DEPARTURE_TIME), Instant.parse(ARRIVAL_TIME));
        var scheduledFlight = new ScheduledFlight(FLIGHT_NUMBER, EBAW, SAME, Instant.parse(DEPARTURE_TIME), Instant.parse(ARRIVAL_TIME));
        when(flightSchedulerService.scheduleFlight(flightScheduleRequest)).thenReturn(scheduledFlight);

        this.mockMvc.perform(
                        post("/api/flight-scheduler/schedule")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content("""
                                            {
                                              "flightNumber": "AR9999",
                                              "departure": "EBAW",
                                              "destination": "SAME",
                                              "departureTime": "2022-12-12T12:00:00Z",
                                              "arrivalTime": "2022-12-12T18:00:00-05:00"
                                            }
                                        """))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.uuid").value(scheduledFlight.getUuid().toString()))
                .andExpect(jsonPath("$.flightNumber").value(FLIGHT_NUMBER))
                .andExpect(jsonPath("$.departure").value(EBAW.name()))
                .andExpect(jsonPath("$.destination").value(SAME.name()))
                .andExpect(jsonPath("$.departureTime").value(DEPARTURE_TIME))
                .andExpect(jsonPath("$.arrivalTime").value(LOCAL_ARRIVAL_TIME));

        verify(flightSchedulerService).scheduleFlight(flightScheduleRequest);
    }
}
