package com.github.aca.avio.flight.scheduler;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FlightSchedulerService {

    ScheduledFlightDto scheduleFlight(FlightScheduleRequest flightScheduleRequest) {
        return new ScheduledFlightDto(UUID.randomUUID(), flightScheduleRequest.flightNumber(), flightScheduleRequest.departure(), flightScheduleRequest.destination());
    }
}
