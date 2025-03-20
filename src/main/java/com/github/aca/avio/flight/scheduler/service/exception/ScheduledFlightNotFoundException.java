package com.github.aca.avio.flight.scheduler.service.exception;

import java.util.UUID;

public class ScheduledFlightNotFoundException extends FlightSchedulerException {

    public ScheduledFlightNotFoundException(UUID uuid) {
        super(String.format("Scheduled flight with uuid '%s' not found", uuid));
    }

}
