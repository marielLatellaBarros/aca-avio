package com.github.aca.avio.flight.scheduler;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FlightSchedulerService {

    UUID scheduleFlight() {
        return UUID.randomUUID();
    }
}
