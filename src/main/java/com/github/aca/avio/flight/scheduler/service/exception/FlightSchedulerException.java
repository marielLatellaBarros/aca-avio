package com.github.aca.avio.flight.scheduler.service.exception;

public abstract class FlightSchedulerException extends RuntimeException {

    protected FlightSchedulerException(String message) {
        super(message);
    }
}
