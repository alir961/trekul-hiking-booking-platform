package ca.ulaval.glo4003.trip.exception;

import ca.ulaval.glo4003.exception.TrekULException;

public class TripInvalidDateFilterException extends TrekULException {
  public TripInvalidDateFilterException() {
    super("TRIP_INVALID_DATE_FILTER", "Start date must be after end date.");
  }
}
