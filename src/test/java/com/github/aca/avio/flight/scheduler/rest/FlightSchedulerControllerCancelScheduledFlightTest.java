package com.github.aca.avio.flight.scheduler.rest;

import com.github.aca.avio.flight.scheduler.service.FlightSchedulerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FlightSchedulerController.class)
class FlightSchedulerControllerCancelScheduledFlightTest {
    private static final UUID SCHEDULED_FLIGHT_UUID = UUID.randomUUID();

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FlightSchedulerService flightSchedulerService;

    @Test
    void shouldCancelScheduledFlight() throws Exception {

        this.mockMvc.perform(delete("/api/flight-scheduler/schedule/{uuid}", SCHEDULED_FLIGHT_UUID))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(flightSchedulerService).cancelScheduledFlight(SCHEDULED_FLIGHT_UUID);

    }
}