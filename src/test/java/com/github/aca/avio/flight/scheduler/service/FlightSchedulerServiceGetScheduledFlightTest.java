package com.github.aca.avio.flight.scheduler.service;

import com.github.aca.avio.flight.scheduler.domain.ScheduledFlight;
import com.github.aca.avio.flight.scheduler.domain.ScheduledFlightRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static com.github.aca.avio.flight.scheduler.domain.IcaoAirportCode.EBAW;
import static com.github.aca.avio.flight.scheduler.domain.IcaoAirportCode.EBBR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class FlightSchedulerServiceGetScheduledFlightTest {

    private static final String FLIGHT_NUMBER = "AR1234";
    private static final String DEPARTURE_TIME = "2022-12-12T12:00:00Z";
    private static final Instant DEPARTURE_TIME_PARSED = Instant.parse(DEPARTURE_TIME);
    private static final String ARRIVAL_TIME = "2022-12-12T18:00:00-05:00";
    private static final Instant ARRIVAL_TIME_PARSED = Instant.parse(ARRIVAL_TIME);

    @InjectMocks
    private FlightSchedulerService flightSchedulerService;

    @Mock
    private ScheduledFlightRepository scheduledFlightRepository;

    @Test
    void shouldReturnExistingScheduledFlight() {
        var scheduledFlight = new ScheduledFlight(FLIGHT_NUMBER, EBAW, EBBR, DEPARTURE_TIME_PARSED, ARRIVAL_TIME_PARSED);
        doReturn(Optional.of(scheduledFlight)).when(scheduledFlightRepository).findByUuid(scheduledFlight.getUuid());

        ScheduledFlight result = flightSchedulerService.getScheduledFlight(scheduledFlight.getUuid());
        assertThat(result).isNotNull();
        assertThat(result.getFlightNumber()).isEqualTo(FLIGHT_NUMBER);
        assertThat(result.getDeparture()).isEqualTo(EBAW);
        assertThat(result.getDestination()).isEqualTo(EBBR);
        assertThat(result.getDepartureTime()).isEqualTo(DEPARTURE_TIME_PARSED);
        assertThat(result.getArrivalTime()).isEqualTo(ARRIVAL_TIME_PARSED);
    }

    @Test
    void throwsExceptionWhenScheduledFlightNotFound() {
        UUID otherUuid = UUID.randomUUID();
        assertThatThrownBy(() -> flightSchedulerService.getScheduledFlight(otherUuid))
                .isInstanceOf(ScheduledFlightNotFoundException.class)
                .hasMessageContaining(String.format("Scheduled flight with uuid '%s' not found", otherUuid));
    }
}