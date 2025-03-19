package com.github.aca.avio.flight.scheduler.rest;

import com.github.aca.avio.flight.scheduler.domain.IcaoAirportCode;
import com.github.aca.avio.flight.scheduler.domain.ScheduledFlight;
import com.github.aca.avio.flight.scheduler.service.FlightSchedulerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/flight-scheduler")
public class FlightSchedulerController {

    private final FlightSchedulerService flightSchedulerService;

    private final ModelMapper modelMapper;

    @PostMapping("/schedule")
    @ResponseStatus(HttpStatus.CREATED)
    public ScheduledFlightDto scheduleFlight(@Valid @RequestBody FlightScheduleRequest flightScheduleRequest) {
        var scheduledFlight = flightSchedulerService.scheduleFlight(flightScheduleRequest);
        return modelMapper.map(scheduledFlight, ScheduledFlightDto.class);
    }

    @GetMapping("/schedule/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public ScheduledFlightDto getScheduledFlight(@PathVariable UUID uuid) {
        var scheduledFlight = flightSchedulerService.getScheduledFlight(uuid);
        return modelMapper.map(scheduledFlight, ScheduledFlightDto.class);
    }

    @GetMapping("/schedule")
    @ResponseStatus(HttpStatus.OK)
    public List<ScheduledFlightDto> getScheduledFlights() {
        var scheduledFlights = flightSchedulerService.getScheduledFlights();
        return scheduledFlights.stream().map(scheduledFlight ->
                modelMapper.map(scheduledFlight, ScheduledFlightDto.class)).toList();
    }

    @GetMapping("/schedule/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ScheduledFlightDto> searchScheduledFlights(@RequestParam(required = false) String flightNumber,
                                                           @RequestParam(required = false) IcaoAirportCode departure,
                                                           @RequestParam(required = false) IcaoAirportCode destination,
                                                           @RequestParam(required = false) Instant departureTimeAfter,
                                                           @RequestParam(required = false) Instant departureTimeBefore,
                                                           @RequestParam(required = false) Instant arrivalTimeAfter,
                                                           @RequestParam(required = false) Instant arrivalTimeBefore) {
        List<ScheduledFlight> scheduledFlights = flightSchedulerService.searchScheduledFlights(flightNumber, departure,
                destination, departureTimeAfter, departureTimeBefore, arrivalTimeAfter, arrivalTimeBefore);
        return scheduledFlights.stream().map(
                scheduledFlight -> modelMapper.map(scheduledFlight, ScheduledFlightDto.class)).toList();
    }

    @DeleteMapping("/schedule/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelScheduledFlight(@PathVariable UUID uuid) {
        flightSchedulerService.cancelScheduledFlight(uuid);
    }
}
