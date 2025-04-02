package ca.ulaval.glo4003.trip.filter;

import ca.ulaval.glo4003.hiking.HikingRegion;
import ca.ulaval.glo4003.trip.Trip;
import java.util.stream.Stream;

public class SearchTripRegionFilter implements SearchTripFilter {
  private final HikingRegion region;

  public SearchTripRegionFilter(HikingRegion region) {
    this.region = region;
  }

  @Override
  public Stream<Trip> apply(Stream<Trip> trips) {
    return trips.filter(trip -> trip.getRegion() == this.region);
  }
}
