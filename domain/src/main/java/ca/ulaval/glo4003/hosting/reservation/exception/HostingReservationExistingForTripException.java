package ca.ulaval.glo4003.hosting.reservation.exception;

import ca.ulaval.glo4003.exception.TrekULException;

public class HostingReservationExistingForTripException extends TrekULException {
  public HostingReservationExistingForTripException() {
    super("EXISTING_HOSTING_RESERVATION", "A hosting reservation already exists for this trip.");
  }
}
