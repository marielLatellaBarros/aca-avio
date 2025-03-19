package com.github.aca.avio.flight.scheduler;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    @ResponseStatus(HttpStatus.OK) // TODO: Add test
    public List<ScheduledFlightDto> searchScheduledFlights(@RequestParam String flightNumber) {
        var scheduledFlights = flightSchedulerService.searchScheduledFlights(flightNumber);
        return scheduledFlights.stream().map(scheduledFlight ->
                modelMapper.map(scheduledFlight, ScheduledFlightDto.class)).toList();
    }

    @DeleteMapping("/schedule/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelScheduledFlight(@PathVariable UUID uuid) {
        flightSchedulerService.cancelScheduledFlight(uuid);
    }
}
