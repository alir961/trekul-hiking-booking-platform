package ca.ulaval.glo4003.trip.filter;

import ca.ulaval.glo4003.trip.Trip;
import ca.ulaval.glo4003.trip.TripStatus;
import java.util.stream.Stream;

public class SearchTripStatusFilter implements SearchTripFilter {
  private final TripStatus targetStatus;

  public SearchTripStatusFilter(TripStatus targetStatus) {
    this.targetStatus = targetStatus;
  }

  @Override
  public Stream<Trip> apply(Stream<Trip> trips) {
    return trips.filter(trip -> trip.hasStatus(this.targetStatus));
  }
}
