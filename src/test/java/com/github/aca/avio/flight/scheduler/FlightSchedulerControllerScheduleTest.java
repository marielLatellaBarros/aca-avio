package com.github.aca.avio.flight.scheduler;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FlightSchedulerController.class)
class FlightSchedulerControllerScheduleTest {

    private static final String FLIGHT_NUMBER = "AR9999";
    private static final String DEPARTURE = "AAAA";
    private static final String DESTINATION = "ZZZZ";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FlightSchedulerService flightSchedulerService;

    @Test
    void shouldReturnScheduledFlight() throws Exception {
        var flightScheduleRequest = new FlightScheduleRequest(FLIGHT_NUMBER, DEPARTURE, DESTINATION);
        var scheduledFlightDto = new ScheduledFlightDto(UUID.randomUUID(), FLIGHT_NUMBER, DEPARTURE, DESTINATION);
        when(flightSchedulerService.scheduleFlight(flightScheduleRequest)).thenReturn(scheduledFlightDto);

        this.mockMvc.perform(
                        post("/api/flight-scheduler/schedule")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                            {
                                              "flightNumber": "AR9999",
                                              "departure": "AAAA",
                                              "destination": "ZZZZ"
                                            }
                                        """))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.uuid").value(scheduledFlightDto.uuid().toString()))
                .andExpect(jsonPath("$.flightNumber").value(FLIGHT_NUMBER))
                .andExpect(jsonPath("$.departure").value(DEPARTURE))
                .andExpect(jsonPath("$.destination").value(DESTINATION));


        verify(flightSchedulerService).scheduleFlight(flightScheduleRequest);
    }
}
