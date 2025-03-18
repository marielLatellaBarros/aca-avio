package com.github.aca.avio.flight.scheduler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/flight-scheduler")
public class FlightSchedulerController {

    private final FlightSchedulerService flightSchedulerService;

    public FlightSchedulerController(FlightSchedulerService flightSchedulerService) {
        this.flightSchedulerService = flightSchedulerService;
    }

    @PostMapping("/schedule")
    // TODO: Add @Valid annotation to validate the request body
    public ResponseEntity<ScheduledFlightDto> scheduleFlight(@RequestBody FlightScheduleRequest flightScheduleRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(flightSchedulerService.scheduleFlight(flightScheduleRequest));
    }
}
