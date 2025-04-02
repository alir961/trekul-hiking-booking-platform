package ca.ulaval.glo4003.transport.response;

import java.util.List;

public record ReservableTransportResponse(
    String transportID,
    int availableSeats,
    String cost,
    List<ReservableTransportStopResponse> stops) {}
