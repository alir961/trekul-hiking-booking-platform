package ca.ulaval.glo4003.reservation.dto;

import ca.ulaval.glo4003.group.Group;
import ca.ulaval.glo4003.invoice.dto.InvoiceDto;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.reservation.ReservationID;
import ca.ulaval.glo4003.reservation.ReservationStatus;
import ca.ulaval.glo4003.transport.TransportType;
import java.time.ZonedDateTime;

public record CreatedReservationDto(
    ReservationID reservationID,
    InvoiceDto invoiceDto,
    Group group,
    Money minimumDue,
    ZonedDateTime expiration,
    ReservationStatus status,
    TransportType transportType) {}
