package com.github.aca.avio.flight.scheduler.service.exception;

import java.time.Instant;

public class DepartureTimeCannotBeAfterArrivalTimeException extends FlightSchedulerException {
    public DepartureTimeCannotBeAfterArrivalTimeException(Instant departureTime, Instant arrivalTime) {
        super(String.format("Departure time %s cannot happen after arrival time %s", departureTime, arrivalTime));
    }
}

