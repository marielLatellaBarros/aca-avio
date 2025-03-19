package com.github.aca.avio.flight.scheduler.service;

import com.github.aca.avio.flight.scheduler.domain.ScheduledFlight;
import com.github.aca.avio.flight.scheduler.domain.ScheduledFlightRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static com.github.aca.avio.flight.scheduler.domain.IcaoAirportCode.EBAW;
import static com.github.aca.avio.flight.scheduler.domain.IcaoAirportCode.EBBR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FlightSchedulerServiceGetScheduledFlightsTest {

    private static final String FLIGHT_NUMBER_1 = "AR1234";
    private static final String FLIGHT_NUMBER_2 = "AR5678";
    private static final String DEPARTURE_TIME_1 = "2021-12-12T12:00:00Z";
    private static final String DEPARTURE_TIME_2 = "2022-12-12T12:00:00Z";
    private static final Instant DEPARTURE_TIME_1_PARSED = Instant.parse(DEPARTURE_TIME_1);
    private static final Instant DEPARTURE_TIME_2_PARSED = Instant.parse(DEPARTURE_TIME_2);
    private static final String ARRIVAL_TIME_1 = "2021-12-13T18:00:00-05:00";
    private static final String ARRIVAL_TIME_2 = "2021-12-14T18:00:00-05:00";
    private static final Instant ARRIVAL_TIME_1_PARSED = Instant.parse(ARRIVAL_TIME_1);
    private static final Instant ARRIVAL_TIME_2_PARSED = Instant.parse(ARRIVAL_TIME_2);

    @InjectMocks
    private FlightSchedulerService flightSchedulerService;

    @Mock
    private ScheduledFlightRepository scheduledFlightRepository;

    @Test
    void shouldReturnExistingScheduledFlights() {
        var scheduledFlight1 = new ScheduledFlight(FLIGHT_NUMBER_1, EBAW, EBBR, DEPARTURE_TIME_1_PARSED, ARRIVAL_TIME_1_PARSED);
        var scheduledFlight2 = new ScheduledFlight(FLIGHT_NUMBER_2, EBBR, EBAW, DEPARTURE_TIME_2_PARSED, ARRIVAL_TIME_2_PARSED);
        when(scheduledFlightRepository.findAll()).thenReturn(List.of(scheduledFlight1, scheduledFlight2));

        List<ScheduledFlight> scheduledFlights = flightSchedulerService.getScheduledFlights();
        assertThat(scheduledFlights).hasSize(2);
        assertThat(scheduledFlights).extracting(
                        ScheduledFlight::getUuid, ScheduledFlight::getFlightNumber,
                        ScheduledFlight::getDeparture, ScheduledFlight::getDestination,
                        ScheduledFlight::getDepartureTime, ScheduledFlight::getArrivalTime)
                .containsExactlyInAnyOrder(
                        tuple(scheduledFlight1.getUuid(), FLIGHT_NUMBER_1, EBAW, EBBR, DEPARTURE_TIME_1_PARSED, ARRIVAL_TIME_1_PARSED),
                        tuple(scheduledFlight2.getUuid(), FLIGHT_NUMBER_2, EBBR, EBAW, DEPARTURE_TIME_2_PARSED, ARRIVAL_TIME_2_PARSED)
                );
    }

    @Test
    void shouldReturnEmptyListWhenNoScheduledFlightsAreFound() {
        List<ScheduledFlight> scheduledFlights = flightSchedulerService.getScheduledFlights();
        assertThat(scheduledFlights).isEmpty();
    }
}