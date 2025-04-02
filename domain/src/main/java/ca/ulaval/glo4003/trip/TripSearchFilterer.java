package ca.ulaval.glo4003.trip;

import ca.ulaval.glo4003.trip.filter.SearchTripFilter;
import java.util.List;
import java.util.stream.Stream;

public class TripSearchFilterer {
  public List<Trip> filterSearch(List<Trip> trips, List<SearchTripFilter> searchTripFilters) {
    Stream<Trip> streamFilter = trips.stream();
    for (SearchTripFilter searchTripFilter : searchTripFilters) {
      streamFilter = searchTripFilter.apply(streamFilter);
    }
    return streamFilter.toList();
  }
}
