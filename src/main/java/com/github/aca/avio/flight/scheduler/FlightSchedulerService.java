package com.github.aca.avio.flight.scheduler;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
// TODO: Implement repository/ domain logic
@Service
public class FlightSchedulerService {

    ScheduledFlightDto scheduleFlight(FlightScheduleRequest flightScheduleRequest) {
        return new ScheduledFlightDto(UUID.randomUUID(), flightScheduleRequest.flightNumber(), flightScheduleRequest.departure(), flightScheduleRequest.destination());
    }

    ScheduledFlightDto getScheduledFlight(UUID uuid) {
        return new ScheduledFlightDto(uuid, "AR9999", "AAAA", "ZZZZ");
    }

    List<ScheduledFlightDto> getScheduledFlights() {
        return List.of(new ScheduledFlightDto(UUID.randomUUID(), "AR9999", "AAAA", "BBBB"),
                new ScheduledFlightDto(UUID.randomUUID(), "AR1111", "CCCC", "DDDD"));
    }

    void cancelScheduledFlight(UUID uuid) {
    }
}
