package ca.ulaval.glo4003.transport.response;

import java.time.LocalDateTime;

public record ReservableTransportStopResponse(String location, LocalDateTime departure) {}
