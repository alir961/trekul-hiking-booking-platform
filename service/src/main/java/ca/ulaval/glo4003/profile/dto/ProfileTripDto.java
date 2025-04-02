package ca.ulaval.glo4003.profile.dto;

import ca.ulaval.glo4003.hiking.Hiking;
import ca.ulaval.glo4003.hosting.reservation.HostingReservation;
import ca.ulaval.glo4003.invoice.dto.InvoiceDto;
import ca.ulaval.glo4003.reservation.dto.CreatedReservationDto;
import ca.ulaval.glo4003.trip.TripID;
import java.time.LocalDate;

public record ProfileTripDto(
    Hiking hiking,
    TripID tripID,
    LocalDate date,
    InvoiceDto invoice,
    CreatedReservationDto reservation,
    HostingReservation hostingReservation) {}
