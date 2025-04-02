package ca.ulaval.glo4003.trip.exception;

import ca.ulaval.glo4003.exception.TrekULException;

public class TripDatePassedException extends TrekULException {
  public TripDatePassedException() {
    super("TRIP_DATE_PASSED", "Trip date can't be in the past.");
  }
}
