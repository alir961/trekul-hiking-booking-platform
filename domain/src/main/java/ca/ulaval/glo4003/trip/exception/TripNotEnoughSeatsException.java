package ca.ulaval.glo4003.trip.exception;

import ca.ulaval.glo4003.exception.TrekULException;

public class TripNotEnoughSeatsException extends TrekULException {
  public TripNotEnoughSeatsException(int currentSeats) {
    super("TRIP_NOT_ENOUGH_SEATS", String.format("Trip currently has %d seats left", currentSeats));
  }
}
