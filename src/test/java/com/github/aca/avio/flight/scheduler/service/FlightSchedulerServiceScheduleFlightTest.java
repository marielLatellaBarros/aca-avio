package com.github.aca.avio.flight.scheduler.service;

import com.github.aca.avio.flight.scheduler.domain.ScheduledFlight;
import com.github.aca.avio.flight.scheduler.domain.ScheduledFlightRepository;
import com.github.aca.avio.flight.scheduler.rest.FlightScheduleRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static com.github.aca.avio.flight.scheduler.domain.IcaoAirportCode.EBAW;
import static com.github.aca.avio.flight.scheduler.domain.IcaoAirportCode.EBBR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FlightSchedulerServiceScheduleFlightTest {

    private static final String FLIGHT_NUMBER = "AR1234";
    private static final String DEPARTURE_TIME = "2022-12-12T12:00:00Z";
    private static final Instant DEPARTURE_TIME_PARSED = Instant.parse(DEPARTURE_TIME);
    private static final String ARRIVAL_TIME = "2022-12-12T18:00:00-05:00";
    private static final Instant ARRIVAL_TIME_PARSED = Instant.parse(ARRIVAL_TIME);

    @InjectMocks
    private FlightSchedulerService flightSchedulerService;

    @Mock
    private ScheduledFlightRepository scheduledFlightRepository;

    @Captor
    private ArgumentCaptor<ScheduledFlight> scheduledFlightCaptor;

    @Test
    void shouldReturnExistingScheduledFlight() {
        FlightScheduleRequest flightScheduleRequest = new FlightScheduleRequest(FLIGHT_NUMBER, EBAW, EBBR, DEPARTURE_TIME_PARSED, ARRIVAL_TIME_PARSED);

        flightSchedulerService.scheduleFlight(flightScheduleRequest);

        verify(scheduledFlightRepository).save(scheduledFlightCaptor.capture());
        ScheduledFlight scheduledFlight = scheduledFlightCaptor.getValue();
        assertThat(scheduledFlight.getFlightNumber()).isEqualTo(FLIGHT_NUMBER);
        assertThat(scheduledFlight.getDeparture()).isEqualTo(EBAW);
        assertThat(scheduledFlight.getDestination()).isEqualTo(EBBR);
        assertThat(scheduledFlight.getDepartureTime()).isEqualTo(DEPARTURE_TIME_PARSED);
        assertThat(scheduledFlight.getArrivalTime()).isEqualTo(ARRIVAL_TIME_PARSED);
    }
}