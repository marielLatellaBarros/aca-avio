package com.github.aca.avio.flight.scheduler.service;

import java.util.UUID;

public class ScheduledFlightNotFoundException extends RuntimeException {

    public ScheduledFlightNotFoundException(UUID uuid) {
        super(String.format("Scheduled flight with uuid '%s' not found", uuid));
    }

}
