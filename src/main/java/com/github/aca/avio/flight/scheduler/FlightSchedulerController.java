package com.github.aca.avio.flight.scheduler;

import org.springframework.web.bind.annotation.PostMapping;
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
    public String scheduleFlight() {
        return flightSchedulerService.scheduleFlight().toString();
    }
}
