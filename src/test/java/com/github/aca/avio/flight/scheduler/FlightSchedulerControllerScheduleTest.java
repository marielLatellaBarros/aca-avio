package com.github.aca.avio.flight.scheduler;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FlightSchedulerController.class)
class FlightSchedulerControllerScheduleTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FlightSchedulerService flightSchedulerService;

    @Test
    void shouldReturnScheduledFlight() throws Exception {
        UUID scheduledFlight = UUID.randomUUID();
        when(flightSchedulerService.scheduleFlight()).thenReturn(scheduledFlight);

        this.mockMvc.perform(post("/api/flight-scheduler/schedule"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(scheduledFlight.toString()));

        verify(flightSchedulerService).scheduleFlight();
    }
}
