package ca.ulaval.glo4003.profile.response;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_ABSENT;

import ca.ulaval.glo4003.hiking.response.HikingResponse;
import ca.ulaval.glo4003.hosting.response.HostingResponse;
import ca.ulaval.glo4003.invoice.response.InvoiceShortResponse;
import ca.ulaval.glo4003.reservation.response.ReservationStatusResponse;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(NON_ABSENT)
public record ProfileTripResponse(
    String tripId,
    HikingResponse hiking,
    String startDate,
    InvoiceShortResponse invoice,
    ReservationStatusResponse reservation,
    HostingResponse host) {}
