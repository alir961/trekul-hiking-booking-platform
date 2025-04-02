package ca.ulaval.glo4003.trip;

import static ca.ulaval.glo4003.trip.TripStatus.AVAILABLE;

import ca.ulaval.glo4003.hiking.Hiking;
import ca.ulaval.glo4003.transport.TransportCollection;

public class TripFactory {
  private static final TripStatus DEFAULT_TRIP_STATUS = AVAILABLE;
  private final TripConfig config;

  public TripFactory(TripConfig config) {
    this.config = config;
  }

  public Trip createTrip(Hiking hiking, TransportCollection transportCollection) {
    return new Trip(
        TripID.generate(),
        this.config.getMaximumSeats(),
        this.config.getDefaultStartDate(),
        hiking,
        this.config.getDefaultTripCost(),
        DEFAULT_TRIP_STATUS,
        transportCollection);
  }
}
