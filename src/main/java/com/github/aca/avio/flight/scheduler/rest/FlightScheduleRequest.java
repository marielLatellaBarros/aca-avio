package com.github.aca.avio.flight.scheduler.rest;

import com.github.aca.avio.flight.scheduler.domain.IcaoAirportCode;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;

public record FlightScheduleRequest(
        @FlightNumber String flightNumber,
        @NotNull IcaoAirportCode departure,
        @NotNull IcaoAirportCode destination,
        @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant departureTime,
        @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant arrivalTime) {
}
