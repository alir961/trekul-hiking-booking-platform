package ca.ulaval.glo4003.trip.exception;

import ca.ulaval.glo4003.exception.TrekULNotFoundException;

public class TripNotFoundException extends TrekULNotFoundException {
  public TripNotFoundException() {
    super("TRIP_NOT_FOUND", "Trip not found");
  }
}
