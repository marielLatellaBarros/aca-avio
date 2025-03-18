package com.github.aca.avio.flight.scheduler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FlightSchedulerController.class)
class FlightSchedulerControllerGetScheduledFlightTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FlightSchedulerService flightSchedulerService;

    @Test
    void shouldReturnScheduledFlightForGivenUuid() throws Exception {
        var uuid = UUID.randomUUID();
        var scheduledFlightDto = new ScheduledFlightDto(uuid, "AR9999", "AAAA", "ZZZZ");
        when(flightSchedulerService.getScheduledFlight(uuid)).thenReturn(scheduledFlightDto);

        this.mockMvc.perform(get("/api/flight-scheduler/schedule/{uuid}", uuid))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").value(uuid.toString()))
                .andExpect(jsonPath("$.flightNumber").value("AR9999"))
                .andExpect(jsonPath("$.departure").value("AAAA"))
                .andExpect(jsonPath("$.destination").value("ZZZZ"));

        verify(flightSchedulerService).getScheduledFlight(uuid);
    }
}