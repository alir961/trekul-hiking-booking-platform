package ca.ulaval.glo4003.trip.exception;

import ca.ulaval.glo4003.exception.TrekULException;

public class TripInvalidCostFilterException extends TrekULException {
  public TripInvalidCostFilterException() {
    super("TRIP_INVALID_COST_FILTER", "Cost filter must be with a strictly positive amount.");
  }
}
