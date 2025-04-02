package ca.ulaval.glo4003.reservation.response;

import ca.ulaval.glo4003.group.reponse.GroupResponse;
import ca.ulaval.glo4003.invoice.response.InvoiceResponse;
import java.time.ZonedDateTime;

public record ReservationResponse(
    String reservationID,
    InvoiceResponse invoice,
    GroupResponse group,
    String minimumToPay,
    ZonedDateTime expiration) {}
