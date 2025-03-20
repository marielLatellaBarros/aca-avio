package com.github.aca.avio.flight.scheduler.service;

import com.github.aca.avio.flight.scheduler.domain.ScheduledFlight;
import com.github.aca.avio.flight.scheduler.rest.FlightScheduleRequest;
import com.github.aca.avio.flight.scheduler.service.exception.DepartureTimeCannotBeAfterArrivalTimeException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;

import static com.github.aca.avio.flight.scheduler.domain.IcaoAirportCode.EBAW;
import static com.github.aca.avio.flight.scheduler.domain.IcaoAirportCode.EBBR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class FlightSchedulerServiceScheduleFlightTest {

    private static final String FLIGHT_NUMBER = "AR1234";
    private static final String DEPARTURE_TIME = "2022-10-12T12:00:00Z";
    private static final Instant DEPARTURE_TIME_PARSED = Instant.parse(DEPARTURE_TIME);
    private static final String ARRIVAL_TIME = "2022-11-12T12:00:00Z";
    private static final Instant ARRIVAL_TIME_PARSED = Instant.parse(ARRIVAL_TIME);

    @Autowired
    private FlightSchedulerService flightSchedulerService;

    @Test
    void shouldPersistScheduledFlight() {
        FlightScheduleRequest flightScheduleRequest = new FlightScheduleRequest(FLIGHT_NUMBER, EBAW, EBBR, DEPARTURE_TIME_PARSED, ARRIVAL_TIME_PARSED);

        ScheduledFlight persistedScheduledFlight = flightSchedulerService.scheduleFlight(flightScheduleRequest);

        assertThat(persistedScheduledFlight.getUuid()).isNotNull();
        assertThat(persistedScheduledFlight.getFlightNumber()).isEqualTo(FLIGHT_NUMBER);
        assertThat(persistedScheduledFlight.getDeparture()).isEqualTo(EBAW);
        assertThat(persistedScheduledFlight.getDestination()).isEqualTo(EBBR);
        assertThat(persistedScheduledFlight.getDepartureTime()).isEqualTo(DEPARTURE_TIME_PARSED);
        assertThat(persistedScheduledFlight.getArrivalTime()).isEqualTo(ARRIVAL_TIME_PARSED);
    }

    @Test
    void shouldThrowExceptionWhenDepartureTimeIsAfterArrivalTime() {
        FlightScheduleRequest flightScheduleRequest = new FlightScheduleRequest(FLIGHT_NUMBER, EBAW, EBBR, ARRIVAL_TIME_PARSED, DEPARTURE_TIME_PARSED);

        assertThatThrownBy(() -> flightSchedulerService.scheduleFlight(flightScheduleRequest))
                .isInstanceOf(DepartureTimeCannotBeAfterArrivalTimeException.class)
                .hasMessage("Departure time 2022-11-12T12:00:00Z cannot happen after arrival time 2022-10-12T12:00:00Z");
    }
}