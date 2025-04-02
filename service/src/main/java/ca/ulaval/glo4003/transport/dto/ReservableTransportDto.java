package ca.ulaval.glo4003.transport.dto;

import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.transport.TransportID;
import java.util.List;

public record ReservableTransportDto(
    TransportID transportID,
    int availableSeats,
    Money cost,
    List<ReservableTransportStopDto> stops) {}
