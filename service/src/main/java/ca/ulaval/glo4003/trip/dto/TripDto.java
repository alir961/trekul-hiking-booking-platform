package ca.ulaval.glo4003.trip.dto;

import ca.ulaval.glo4003.hiking.Hiking;
import ca.ulaval.glo4003.hosting.reservation.HostingReservation;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.trip.TripID;
import ca.ulaval.glo4003.trip.TripStatus;
import java.time.LocalDate;

public record TripDto(
    Hiking hiking,
    LocalDate date,
    int availableSeats,
    TripID tripID,
    Money cost,
    TripStatus status,
    HostingReservation hostingReservation) {}
