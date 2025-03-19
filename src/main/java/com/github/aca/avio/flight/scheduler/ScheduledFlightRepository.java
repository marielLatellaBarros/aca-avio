package com.github.aca.avio.flight.scheduler;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ScheduledFlightRepository extends JpaRepository<ScheduledFlight, Integer> {

    Optional<ScheduledFlight> findByUuid(UUID uuid);

    void deleteByUuid(UUID uuid);

    List<ScheduledFlight> findByFlightNumber(String flightNumber);
}
