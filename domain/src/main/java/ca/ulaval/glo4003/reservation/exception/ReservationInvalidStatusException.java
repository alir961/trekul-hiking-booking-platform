package ca.ulaval.glo4003.reservation.exception;

import ca.ulaval.glo4003.exception.TrekULException;
import ca.ulaval.glo4003.reservation.ReservationStatus;

public class ReservationInvalidStatusException extends TrekULException {
  public ReservationInvalidStatusException(ReservationStatus status) {
    super(
        "RESERVATION_INVALID_STATUS",
        "This action cannot be done because reservation is " + status);
  }
}
