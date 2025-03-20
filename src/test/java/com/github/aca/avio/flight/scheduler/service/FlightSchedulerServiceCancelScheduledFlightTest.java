package com.github.aca.avio.flight.scheduler.service;

import com.github.aca.avio.flight.scheduler.domain.ScheduledFlightRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FlightSchedulerServiceCancelScheduledFlightTest {

    @InjectMocks
    private FlightSchedulerService flightSchedulerService;

    @Mock
    private ScheduledFlightRepository scheduledFlightRepository;

    @Test
    void shouldDelegateToRepositoryCorrectly() {
        var flightUuid = UUID.randomUUID();
        flightSchedulerService.cancelScheduledFlight(flightUuid);

        verify(scheduledFlightRepository).deleteByUuid(flightUuid);
    }
}