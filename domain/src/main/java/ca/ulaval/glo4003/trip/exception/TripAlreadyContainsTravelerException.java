package ca.ulaval.glo4003.trip.exception;

import ca.ulaval.glo4003.exception.TrekULException;

public class TripAlreadyContainsTravelerException extends TrekULException {
  public TripAlreadyContainsTravelerException() {
    super(
        "TRIP_ALREADY_CONTAINS_TRAVELER",
        "Trip cannot contain the same traveler more than once. This is based on IDs.");
  }
}
