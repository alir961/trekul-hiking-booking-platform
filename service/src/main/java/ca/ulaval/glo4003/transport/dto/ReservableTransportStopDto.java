package ca.ulaval.glo4003.transport.dto;

import java.time.LocalDateTime;

public record ReservableTransportStopDto(String location, LocalDateTime departureTime) {}
