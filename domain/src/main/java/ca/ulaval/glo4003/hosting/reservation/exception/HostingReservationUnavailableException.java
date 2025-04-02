package ca.ulaval.glo4003.hosting.reservation.exception;

import ca.ulaval.glo4003.exception.TrekULException;

public class HostingReservationUnavailableException extends TrekULException {
  public HostingReservationUnavailableException() {
    super(
        "HOSTING_RESERVATION_UNAVAILABLE",
        "A hosting reservation cannot be made because the trip is not blocked.");
  }
}
