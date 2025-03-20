package com.github.aca.avio.flight.scheduler.service;

import com.github.aca.avio.flight.scheduler.domain.IcaoAirportCode;
import com.github.aca.avio.flight.scheduler.domain.ScheduledFlight;
import com.github.aca.avio.flight.scheduler.domain.ScheduledFlightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static com.github.aca.avio.flight.scheduler.domain.IcaoAirportCode.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
class FlightSchedulerServiceSearchScheduledFlightsTest {
    private static final String FLIGHT_NUMBER_1 = "AR1234";
    private static final String FLIGHT_NUMBER_2 = "AR5678";
    private static final String DEPARTURE_TIME_1 = "2022-12-12T12:00:00Z";
    private static final String ARRIVAL_TIME_1 = "2022-12-12T18:00:00-05:00";
    private static final String DEPARTURE_TIME_2 = "2023-12-12T12:00:00Z";
    private static final String ARRIVAL_TIME_2 = "2023-12-12T18:00:00-05:00";
    private static final ScheduledFlight scheduledFlight1 = new ScheduledFlight(FLIGHT_NUMBER_1, SAEZ, SAME, Instant.parse(DEPARTURE_TIME_1), Instant.parse(ARRIVAL_TIME_1));
    private static final ScheduledFlight scheduledFlight2 = new ScheduledFlight(FLIGHT_NUMBER_2, EBAW, EBBR, Instant.parse(DEPARTURE_TIME_2), Instant.parse(ARRIVAL_TIME_2));

    @Autowired
    private FlightSchedulerService flightSchedulerService;

    @Autowired
    private ScheduledFlightRepository scheduledFlightRepository;

    @BeforeEach
    void setUp() {
        scheduledFlightRepository.save(scheduledFlight1);
        scheduledFlightRepository.save(scheduledFlight2);
    }

    @ParameterizedTest
    @MethodSource("sourceValues")
    void shouldReturnExistingScheduledFlightsByQueryParams(List<UUID> result, String flightNumber, IcaoAirportCode departure, IcaoAirportCode destination, Instant departureTimeAfter, Instant departureTimeBefore, Instant arrivalTimeAfter, Instant arrivalTimeBefore) {
        List<ScheduledFlight> scheduledFlights = flightSchedulerService.searchScheduledFlights(flightNumber, departure, destination, departureTimeAfter, departureTimeBefore, arrivalTimeAfter, arrivalTimeBefore);
        assertThat(scheduledFlights).extracting(ScheduledFlight::getUuid).hasSameElementsAs(result);
    }

    static Stream<Arguments> sourceValues() {
        return Stream.of(
                Arguments.of( List.of(scheduledFlight1.getUuid(), scheduledFlight2.getUuid()), null, null, null, null, null, null, null),
                Arguments.of(List.of(scheduledFlight1.getUuid()), FLIGHT_NUMBER_1, SAEZ, SAME, Instant.parse(DEPARTURE_TIME_1), Instant.parse(DEPARTURE_TIME_2), Instant.parse(ARRIVAL_TIME_1), Instant.parse(ARRIVAL_TIME_2)),
                Arguments.of(List.of(scheduledFlight1.getUuid()), FLIGHT_NUMBER_1, null, null, null, null, null, null),
                Arguments.of(List.of(scheduledFlight1.getUuid()), null, SAEZ, null, null, null, null, null),
                Arguments.of(List.of(scheduledFlight1.getUuid()), null, null, SAME, null, null, null, null),
                Arguments.of(List.of(scheduledFlight1.getUuid(), scheduledFlight2.getUuid()), null, null, null, Instant.parse(DEPARTURE_TIME_1), null, null, null),
                Arguments.of(List.of(scheduledFlight1.getUuid(), scheduledFlight2.getUuid()), null, null, null, null, Instant.parse(DEPARTURE_TIME_2), null, null),
                Arguments.of(List.of(scheduledFlight1.getUuid(), scheduledFlight2.getUuid()), null, null, null, null, null, Instant.parse(ARRIVAL_TIME_1), null),
                Arguments.of(List.of(scheduledFlight1.getUuid(), scheduledFlight2.getUuid()), null, null, null, null, null, null, Instant.parse(ARRIVAL_TIME_2))
        );
    }
}