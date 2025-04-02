package ca.ulaval.glo4003.trip.response;

import ca.ulaval.glo4003.hiking.response.HikingResponse;

public record SearchTripResponse(
    HikingResponse hikingResponse,
    int availableSeats,
    String status,
    String startDate,
    String price,
    String id) {}
