package com.github.aca.avio.flight.scheduler;

import java.util.UUID;

public record ScheduledFlightDto(UUID uuid, String flightNumber, String departure, String destination) {}

