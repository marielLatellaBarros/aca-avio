package com.github.aca.avio.flight.scheduler;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FlightSchedulerService {

    private final ScheduledFlightRepository scheduledFlightRepository;

    public ScheduledFlight scheduleFlight(FlightScheduleRequest flightScheduleRequest) {
        ScheduledFlight scheduledFlightJpaEntity = new ScheduledFlight(
                flightScheduleRequest.flightNumber(),
                flightScheduleRequest.departure(),
                flightScheduleRequest.destination(),
                flightScheduleRequest.departureTime(),
                flightScheduleRequest.arrivalTime());
        return scheduledFlightRepository.save(scheduledFlightJpaEntity);
    }

    public ScheduledFlight getScheduledFlight(UUID uuid) {
        return scheduledFlightRepository.findByUuid(uuid).orElseThrow(() -> new ScheduledFlightNotFoundException(uuid));
    }

    public List<ScheduledFlight> getScheduledFlights() {
        return scheduledFlightRepository.findAll();
    }

    public List<ScheduledFlight> searchScheduledFlights(String flightNumber) {
        return scheduledFlightRepository.findByFlightNumber(flightNumber);
    }

    @Transactional
    public void cancelScheduledFlight(UUID uuid) {
        scheduledFlightRepository.deleteByUuid(uuid);
    }
}
