package com.github.aca.avio.flight.scheduler;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/flight-scheduler")
public class FlightSchedulerController {

    @PostMapping("/schedule")
    public String scheduleFlight() {
        // TODO: Add implementation
        return "Flight scheduled";
    }
}
