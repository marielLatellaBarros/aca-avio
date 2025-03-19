package com.github.aca.avio.flight.scheduler;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduledFlight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String flightNumber;

    @Enumerated(EnumType.STRING)
    private IcaoAirportCode departure;

    @Enumerated(EnumType.STRING)
    private IcaoAirportCode destination;

    private Instant departureTime;

    private Instant arrivalTime;

    private UUID uuid;

    public ScheduledFlight(String flightNumber, IcaoAirportCode departure, IcaoAirportCode destination, Instant departureTime, Instant arrivalTime) {
        this.uuid = UUID.randomUUID();
        this.flightNumber = flightNumber;
        this.departure = departure;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }
}
