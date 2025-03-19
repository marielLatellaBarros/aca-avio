package com.github.aca.avio.flight.scheduler.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface ScheduledFlightRepository extends JpaRepository<ScheduledFlight, Integer>, JpaSpecificationExecutor<ScheduledFlight> {

    Optional<ScheduledFlight> findByUuid(UUID uuid);

    void deleteByUuid(UUID uuid);
}