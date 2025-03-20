package com.github.aca.avio.flight.scheduler.service;

import com.github.aca.avio.flight.scheduler.domain.IcaoAirportCode;
import com.github.aca.avio.flight.scheduler.domain.ScheduledFlight;
import com.github.aca.avio.flight.scheduler.domain.ScheduledFlightRepository;
import com.github.aca.avio.flight.scheduler.rest.FlightScheduleRequest;
import com.github.aca.avio.flight.scheduler.service.exception.DepartureTimeCannotBeAfterArrivalTimeException;
import com.github.aca.avio.flight.scheduler.service.exception.ScheduledFlightNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FlightSchedulerService {
    private static final String FLIGHT_NUMBER_FIELD = "flightNumber";
    private static final String DEPARTURE_FIELD = "departure";
    private static final String DESTINATION_FIELD = "destination";
    private static final String DEPARTURE_TIME_FIELD = "departureTime";
    private static final String ARRIVAL_TIME_FIELD = "arrivalTime";
    private final ScheduledFlightRepository scheduledFlightRepository;

    public ScheduledFlight scheduleFlight(FlightScheduleRequest flightScheduleRequest) {
        if (flightScheduleRequest.departureTime().isAfter(flightScheduleRequest.arrivalTime())) {
            throw new DepartureTimeCannotBeAfterArrivalTimeException(flightScheduleRequest.departureTime(), flightScheduleRequest.arrivalTime());
        }
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

    public List<ScheduledFlight> searchScheduledFlights(String flightNumber, IcaoAirportCode departure, IcaoAirportCode destination,
                                                        Instant departureTimeAfter, Instant departureTimeBefore, Instant arrivalTimeAfter, Instant arrivalTimeBefore) {
        Specification<ScheduledFlight> specification = Specification.where(null);

        if (flightNumber != null) {
            specification = specification.and((root, query, cb) -> cb.equal(root.get(FLIGHT_NUMBER_FIELD), flightNumber));
        }
        if (departure != null) {
            specification = specification.and((root, query, cb) -> cb.equal(root.get(DEPARTURE_FIELD), departure));
        }
        if (destination != null) {
            specification = specification.and((root, query, cb) -> cb.equal(root.get(DESTINATION_FIELD), destination));
        }
        if (departureTimeAfter != null) {
            specification = specification.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get(DEPARTURE_TIME_FIELD), departureTimeAfter));
        }
        if (arrivalTimeAfter != null) {
            specification = specification.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get(ARRIVAL_TIME_FIELD), arrivalTimeAfter));
        }
        if (departureTimeBefore != null) {
            specification = specification.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get(DEPARTURE_TIME_FIELD), departureTimeBefore));
        }
        if (arrivalTimeBefore != null) {
            specification = specification.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get(ARRIVAL_TIME_FIELD), arrivalTimeBefore));
        }
        return scheduledFlightRepository.findAll(specification);
    }

    @Transactional
    public void cancelScheduledFlight(UUID uuid) { // TODO: Bad request if not a valid UUID
        scheduledFlightRepository.deleteByUuid(uuid);
    }
}
