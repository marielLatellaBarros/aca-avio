package com.github.aca.avio.flight.scheduler;

import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;

public record FlightScheduleRequest(
        String flightNumber, // TODO: Add custom validation for ICAO flight number
        @NotNull IcaoAirportCode departure,
        @NotNull IcaoAirportCode destination,
        @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant departureTime,
        @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant arrivalTime) {
}
