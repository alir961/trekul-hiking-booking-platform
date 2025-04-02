package ca.ulaval.glo4003.trip.exception;

import ca.ulaval.glo4003.exception.TrekULException;
import ca.ulaval.glo4003.trip.TripStatus;

public class TripNotAvailableException extends TrekULException {
  public TripNotAvailableException(TripStatus status) {
    super("TRIP_NOT_AVAILABLE", "Trip cannot be reserved because it is currently " + status);
  }
}
