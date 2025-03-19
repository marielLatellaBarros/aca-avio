package com.github.aca.avio.flight.scheduler.rest;


import com.github.aca.avio.flight.scheduler.service.FlightSchedulerService;
import com.github.aca.avio.flight.scheduler.domain.ScheduledFlight;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static com.github.aca.avio.flight.scheduler.domain.IcaoAirportCode.EBAW;
import static com.github.aca.avio.flight.scheduler.domain.IcaoAirportCode.SAME;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FlightSchedulerController.class)
class FlightSchedulerControllerScheduleTest {
    private static final String FLIGHT_NUMBER = "AR9999";
    private static final String DEPARTURE_TIME = "2022-12-12T12:00:00Z";
    private static final String ARRIVAL_TIME = "2022-12-12T18:00:00-05:00";
    private static final String LOCAL_ARRIVAL_TIME = "2022-12-12T23:00:00Z";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FlightSchedulerService flightSchedulerService;

    @Test
    void shouldReturnScheduledFlight() throws Exception {
        var flightScheduleRequest = new FlightScheduleRequest(FLIGHT_NUMBER, EBAW, SAME, Instant.parse(DEPARTURE_TIME), Instant.parse(ARRIVAL_TIME));
        var scheduledFlight = new ScheduledFlight(FLIGHT_NUMBER, EBAW, SAME, Instant.parse(DEPARTURE_TIME), Instant.parse(ARRIVAL_TIME));
        when(flightSchedulerService.scheduleFlight(flightScheduleRequest)).thenReturn(scheduledFlight);

        this.mockMvc.perform(
                        post("/api/flight-scheduler/schedule")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content("""
                                            {
                                              "flightNumber": "AR9999",
                                              "departure": "EBAW",
                                              "destination": "SAME",
                                              "departureTime": "2022-12-12T12:00:00Z",
                                              "arrivalTime": "2022-12-12T18:00:00-05:00"
                                            }
                                        """))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.uuid").value(scheduledFlight.getUuid().toString()))
                .andExpect(jsonPath("$.flightNumber").value(FLIGHT_NUMBER))
                .andExpect(jsonPath("$.departure").value(EBAW.name()))
                .andExpect(jsonPath("$.destination").value(SAME.name()))
                .andExpect(jsonPath("$.departureTime").value(DEPARTURE_TIME))
                .andExpect(jsonPath("$.arrivalTime").value(LOCAL_ARRIVAL_TIME));

        verify(flightSchedulerService).scheduleFlight(flightScheduleRequest);
    }

    @Test
    void shouldReturnBadRequestAndErrorMessageWhenFlightNumberIsNull() throws Exception {
        this.mockMvc.perform(
                        post("/api/flight-scheduler/schedule")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content("""
                                            {
                                              "flightNumber": null,
                                              "departure": "EBAW",
                                              "destination": "SAME",
                                              "departureTime": "2022-12-12T12:00:00Z",
                                              "arrivalTime": "2022-12-12T18:00:00-05:00"
                                            }
                                        """))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.flightNumber").value("Flight number is required"));

        verifyNoInteractions(flightSchedulerService);
    }

    @Test
    void shouldReturnBadRequestAndErrorMessageWhenFlightNumberIsBlank() throws Exception {
        this.mockMvc.perform(
                        post("/api/flight-scheduler/schedule")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content("""
                                            {
                                              "flightNumber": "",
                                              "departure": "EBAW",
                                              "destination": "SAME",
                                              "departureTime": "2022-12-12T12:00:00Z",
                                              "arrivalTime": "2022-12-12T18:00:00-05:00"
                                            }
                                        """))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.flightNumber").value("Flight number is required"));

        verifyNoInteractions(flightSchedulerService);
    }

    @Test
    void shouldReturnBadRequestAndErrorMessageWhenFlightNumberHasAtLeastOneWhitespaceCharacter() throws Exception {
        this.mockMvc.perform(
                        post("/api/flight-scheduler/schedule")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content("""
                                            {
                                              "flightNumber": " ",
                                              "departure": "EBAW",
                                              "destination": "SAME",
                                              "departureTime": "2022-12-12T12:00:00Z",
                                              "arrivalTime": "2022-12-12T18:00:00-05:00"
                                            }
                                        """))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.flightNumber").value("Flight number is required"));

        verifyNoInteractions(flightSchedulerService);
    }

    @Test
    void shouldReturnBadRequestAndErrorMessageWhenFlightNumberLengthIsLessThanThreeCharacters() throws Exception {
        this.mockMvc.perform(
                        post("/api/flight-scheduler/schedule")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content("""
                                            {
                                              "flightNumber": "AA",
                                              "departure": "EBAW",
                                              "destination": "SAME",
                                              "departureTime": "2022-12-12T12:00:00Z",
                                              "arrivalTime": "2022-12-12T18:00:00-05:00"
                                            }
                                        """))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.flightNumber").value("Flight number should be between 3 and 7 characters long"));

        verifyNoInteractions(flightSchedulerService);
    }

    @Test
    void shouldReturnBadRequestAndErrorMessageWhenFlightNumberLengthIsGreaterThanSevenCharacters() throws Exception {
        this.mockMvc.perform(
                        post("/api/flight-scheduler/schedule")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content("""
                                            {
                                              "flightNumber": "AA123456",
                                              "departure": "EBAW",
                                              "destination": "SAME",
                                              "departureTime": "2022-12-12T12:00:00Z",
                                              "arrivalTime": "2022-12-12T18:00:00-05:00"
                                            }
                                        """))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.flightNumber").value("Flight number should be between 3 and 7 characters long"));

        verifyNoInteractions(flightSchedulerService);
    }

    @Test
    void doesNotFailWhenFlightNumberLengthIsExactlyThreeCharactersLong() throws Exception {
        var flightScheduleRequest = new FlightScheduleRequest("AR657", EBAW, SAME, Instant.parse(DEPARTURE_TIME), Instant.parse(ARRIVAL_TIME));
        var scheduledFlight = new ScheduledFlight("AR657", EBAW, SAME, Instant.parse(DEPARTURE_TIME), Instant.parse(ARRIVAL_TIME));
        when(flightSchedulerService.scheduleFlight(flightScheduleRequest)).thenReturn(scheduledFlight);

        this.mockMvc.perform(
                        post("/api/flight-scheduler/schedule")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content("""
                                            {
                                              "flightNumber": "AR657",
                                              "departure": "EBAW",
                                              "destination": "SAME",
                                              "departureTime": "2022-12-12T12:00:00Z",
                                              "arrivalTime": "2022-12-12T18:00:00-05:00"
                                            }
                                        """))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.uuid").value(scheduledFlight.getUuid().toString()))
                .andExpect(jsonPath("$.flightNumber").value("AR657"))
                .andExpect(jsonPath("$.departure").value(EBAW.name()))
                .andExpect(jsonPath("$.destination").value(SAME.name()))
                .andExpect(jsonPath("$.departureTime").value(DEPARTURE_TIME))
                .andExpect(jsonPath("$.arrivalTime").value(LOCAL_ARRIVAL_TIME));

        verify(flightSchedulerService).scheduleFlight(flightScheduleRequest);
    }

    @Test
    void doesNotFailWhenFlightNumberLengthIsExactlySevenCharactersLong() throws Exception {
        var flightScheduleRequest = new FlightScheduleRequest("AR6579C", EBAW, SAME, Instant.parse(DEPARTURE_TIME), Instant.parse(ARRIVAL_TIME));
        var scheduledFlight = new ScheduledFlight("AR6579C", EBAW, SAME, Instant.parse(DEPARTURE_TIME), Instant.parse(ARRIVAL_TIME));
        when(flightSchedulerService.scheduleFlight(flightScheduleRequest)).thenReturn(scheduledFlight);

        this.mockMvc.perform(
                        post("/api/flight-scheduler/schedule")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content("""
                                            {
                                              "flightNumber": "AR6579C",
                                              "departure": "EBAW",
                                              "destination": "SAME",
                                              "departureTime": "2022-12-12T12:00:00Z",
                                              "arrivalTime": "2022-12-12T18:00:00-05:00"
                                            }
                                        """))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.uuid").value(scheduledFlight.getUuid().toString()))
                .andExpect(jsonPath("$.flightNumber").value("AR6579C"))
                .andExpect(jsonPath("$.departure").value(EBAW.name()))
                .andExpect(jsonPath("$.destination").value(SAME.name()))
                .andExpect(jsonPath("$.departureTime").value(DEPARTURE_TIME))
                .andExpect(jsonPath("$.arrivalTime").value(LOCAL_ARRIVAL_TIME));

        verify(flightSchedulerService).scheduleFlight(flightScheduleRequest);
    }

    @Test
    void shouldReturnBadRequestAndErrorMessageWhenFlightNumberIataAirlineCodeIsInvalid() throws Exception {
        this.mockMvc.perform(
                        post("/api/flight-scheduler/schedule")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content("""
                                            {
                                              "flightNumber": "OR1234",
                                              "departure": "EBAW",
                                              "destination": "SAME",
                                              "departureTime": "2022-12-12T12:00:00Z",
                                              "arrivalTime": "2022-12-12T18:00:00-05:00"
                                            }
                                        """))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.flightNumber").value("IATA airline code is invalid"));

        verifyNoInteractions(flightSchedulerService);
    }

    @Test
    void shouldReturnBadRequestAndErrorMessageWhenFlightNumberIataAirlineCodeIsFollowedByNonDigitCharacters() throws Exception {
        this.mockMvc.perform(
                        post("/api/flight-scheduler/schedule")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content("""
                                            {
                                              "flightNumber": "ARP2",
                                              "departure": "EBAW",
                                              "destination": "SAME",
                                              "departureTime": "2022-12-12T12:00:00Z",
                                              "arrivalTime": "2022-12-12T18:00:00-05:00"
                                            }
                                        """))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.flightNumber").value("IATA airline code must be followed by 1 to 4 digits"));

        verifyNoInteractions(flightSchedulerService);
    }

    @Test
    void shouldReturnBadRequestAndErrorMessageWhenFlightNumberIataAirlineCodeEndsWithNonDigitCharacter() throws Exception {
        this.mockMvc.perform(
                        post("/api/flight-scheduler/schedule")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content("""
                                            {
                                              "flightNumber": "AR123!",
                                              "departure": "EBAW",
                                              "destination": "SAME",
                                              "departureTime": "2022-12-12T12:00:00Z",
                                              "arrivalTime": "2022-12-12T18:00:00-05:00"
                                            }
                                        """))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.flightNumber").value("IATA airline code must be followed by 1 to 4 digits"));

        verifyNoInteractions(flightSchedulerService);
    }

    @Test
    void shouldReturnBadRequestAndErrorMessageWhenFlightNumberHasOptionalCharacterNonAlphabetic() throws Exception {
        this.mockMvc.perform(
                        post("/api/flight-scheduler/schedule")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content("""
                                            {
                                              "flightNumber": "AR12345",
                                              "departure": "EBAW",
                                              "destination": "SAME",
                                              "departureTime": "2022-12-12T12:00:00Z",
                                              "arrivalTime": "2022-12-12T18:00:00-05:00"
                                            }
                                        """))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.flightNumber").value("Optional code must be a alphabetic"));

        verifyNoInteractions(flightSchedulerService);
    }

    @Test
    void shouldReturnBadRequestAndErrorMessageWhenDepartureIsNull() throws Exception {
        this.mockMvc.perform(
                        post("/api/flight-scheduler/schedule")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content("""
                                            {
                                              "flightNumber": "AR9999",
                                              "departure": null,
                                              "destination": "SAME",
                                              "departureTime": "2022-12-12T12:00:00Z",
                                              "arrivalTime": "2022-12-12T18:00:00-05:00"
                                            }
                                        """))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.departure").value("must not be null"));

        verifyNoInteractions(flightSchedulerService);
    }


    @Test // TODO: if time allows parse error message
    void shouldReturnBadRequestAndErrorMessageWhenDepartureIsInvalid() throws Exception {
        this.mockMvc.perform(
                        post("/api/flight-scheduler/schedule")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content("""
                                            {
                                              "flightNumber": "AR9999",
                                              "departure": "ABCD",
                                              "destination": "SAME",
                                              "departureTime": "2022-12-12T12:00:00Z",
                                              "arrivalTime": "2022-12-12T18:00:00-05:00"
                                            }
                                        """))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("JSON parse error: Cannot deserialize value of type `com.github.aca.avio.flight.scheduler.domain.IcaoAirportCode` from String \"ABCD\": not one of the values accepted for Enum class: [EBAW, EBBR, SAME, SAEZ]"));

        verifyNoInteractions(flightSchedulerService);
    }

    @Test // TODO: if time allows parse error message
    void shouldReturnBadRequestAndErrorMessageWhenDestinationIsNull() throws Exception {
        this.mockMvc.perform(
                        post("/api/flight-scheduler/schedule")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content("""
                                            {
                                              "flightNumber": "AR9999",
                                              "departure": "SAME",
                                              "destination": null,
                                              "departureTime": "2022-12-12T12:00:00Z",
                                              "arrivalTime": "2022-12-12T18:00:00-05:00"
                                            }
                                        """))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.destination").value("must not be null"));

        verifyNoInteractions(flightSchedulerService);
    }

    @Test // TODO: if time allows parse error message
    void shouldReturnBadRequestAndErrorMessageWhenDestinationIsInvalid() throws Exception {
        this.mockMvc.perform(
                        post("/api/flight-scheduler/schedule")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content("""
                                            {
                                              "flightNumber": "AR9999",
                                              "departure": "SAME",
                                              "destination": "ABCD",
                                              "departureTime": "2022-12-12T12:00:00Z",
                                              "arrivalTime": "2022-12-12T18:00:00-05:00"
                                            }
                                        """))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("JSON parse error: Cannot deserialize value of type `com.github.aca.avio.flight.scheduler.domain.IcaoAirportCode` from String \"ABCD\": not one of the values accepted for Enum class: [EBAW, EBBR, SAME, SAEZ]"));

        verifyNoInteractions(flightSchedulerService);
    }

    @Test // TODO: if time allows parse error message
    void shouldReturnBadRequestAndErrorMessageWhenDepartureTimeIsNull() throws Exception {
        this.mockMvc.perform(
                        post("/api/flight-scheduler/schedule")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content("""
                                            {
                                              "flightNumber": "AR9999",
                                              "departure": "SAME",
                                              "destination": "SAEZ",
                                              "departureTime": null,
                                              "arrivalTime": "2022-12-12T18:00:00-05:00"
                                            }
                                        """))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.departureTime").value("must not be null"));

        verifyNoInteractions(flightSchedulerService);
    }

    @Test // TODO: if time allows parse error message
    void shouldReturnBadRequestAndErrorMessageWhenDepartureTimeHasIncorrectFormat() throws Exception {
        this.mockMvc.perform(
                        post("/api/flight-scheduler/schedule")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content("""
                                            {
                                              "flightNumber": "AR9999",
                                              "departure": "SAME",
                                              "destination": "SAEZ",
                                              "departureTime": "2022-12-12T12:00:00",
                                              "arrivalTime": "2022-12-12T18:00:00-05:00"
                                            }
                                        """))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("JSON parse error: Cannot deserialize value of type `java.time.Instant` from String \"2022-12-12T12:00:00\": Failed to deserialize java.time.Instant: (java.time.format.DateTimeParseException) Text '2022-12-12T12:00:00' could not be parsed at index 19"));

        verifyNoInteractions(flightSchedulerService);
    }

    @Test // TODO: if time allows parse error message
    void shouldReturnBadRequestAndErrorMessageWhenArrivalTimeIsNull() throws Exception {
        this.mockMvc.perform(
                        post("/api/flight-scheduler/schedule")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content("""
                                            {
                                              "flightNumber": "AR9999",
                                              "departure": "SAME",
                                              "destination": "SAEZ",
                                              "departureTime": "2022-12-12T12:00:00Z",
                                              "arrivalTime": null
                                            }
                                        """))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.arrivalTime").value("must not be null"));

        verifyNoInteractions(flightSchedulerService);
    }

    @Test // TODO: if time allows parse error message
    void shouldReturnBadRequestAndErrorMessageWhenArrivalTimeHasIncorrectFormat() throws Exception {
        this.mockMvc.perform(
                        post("/api/flight-scheduler/schedule")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content("""
                                            {
                                              "flightNumber": "AR9999",
                                              "departure": "SAME",
                                              "destination": "SAEZ",
                                              "departureTime": "2022-12-12T12:00:00Z",
                                              "arrivalTime": "2022-12-12T18:00:00-05"
                                            }
                                        """))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("JSON parse error: Cannot deserialize value of type `java.time.Instant` from String \"2022-12-12T18:00:00-05\": Failed to deserialize java.time.Instant: (java.time.format.DateTimeParseException) Text '2022-12-12T18:00:00-05' could not be parsed at index 19"));

        verifyNoInteractions(flightSchedulerService);
    }

    // TODO: if time allows test composite errors
}
