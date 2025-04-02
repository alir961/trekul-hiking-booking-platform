package ca.ulaval.glo4003.hosting.reservation.exception;

import ca.ulaval.glo4003.exception.TrekULException;

public class HostingReservationImpossibleException extends TrekULException {
  public HostingReservationImpossibleException() {
    super(
        "HOSTING_RESERVATION_IMPOSSIBLE",
        "A hosting reservation cannot be made because the selected hosting and the trip are not compatible.");
  }
}
