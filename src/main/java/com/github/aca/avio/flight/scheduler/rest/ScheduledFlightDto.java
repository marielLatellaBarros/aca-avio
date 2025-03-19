package com.github.aca.avio.flight.scheduler.rest;

import com.github.aca.avio.flight.scheduler.domain.IcaoAirportCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public final class ScheduledFlightDto {
    private UUID uuid;
    private String flightNumber;
    private IcaoAirportCode departure;
    private IcaoAirportCode destination;
    private Instant departureTime;
    private Instant arrivalTime;
}

