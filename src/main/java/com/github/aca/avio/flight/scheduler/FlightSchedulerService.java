package com.github.aca.avio.flight.scheduler;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FlightSchedulerService {

    ScheduledFlightDto scheduleFlight(FlightScheduleRequest flightScheduleRequest) {
        // TODO: Implement repository/ domain logic
        return new ScheduledFlightDto(UUID.randomUUID(), flightScheduleRequest.flightNumber(), flightScheduleRequest.departure(), flightScheduleRequest.destination());
    }

    ScheduledFlightDto getScheduledFlight(UUID uuid) {
        // TODO: implement repository/ domain logic
        return new ScheduledFlightDto(uuid, "AR9999", "AAAA", "ZZZZ");
    }
}
