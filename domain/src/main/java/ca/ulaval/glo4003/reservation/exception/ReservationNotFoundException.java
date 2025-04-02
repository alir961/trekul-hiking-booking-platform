package ca.ulaval.glo4003.reservation.exception;

import ca.ulaval.glo4003.exception.TrekULNotFoundException;

public class ReservationNotFoundException extends TrekULNotFoundException {
  public ReservationNotFoundException() {
    super("RESERVATION_NOT_FOUND", "Reservation not found for a Trip.");
  }
}
