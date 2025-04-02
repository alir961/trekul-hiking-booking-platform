package ca.ulaval.glo4003.hosting;

import ca.ulaval.glo4003.hosting.filter.SearchHostingFilter;
import ca.ulaval.glo4003.trip.Trip;
import java.util.List;
import java.util.stream.Stream;

public class HostingSearchFilterer {

  private final List<SearchHostingFilter> filterList;

  public HostingSearchFilterer(List<SearchHostingFilter> filterList) {
    this.filterList = filterList;
  }

  public List<Hosting> filterSearch(List<Hosting> hosts, Trip trip) {
    Stream<Hosting> streamFilter = hosts.stream();

    for (SearchHostingFilter searchHostingFilter : this.filterList) {
      streamFilter = searchHostingFilter.apply(streamFilter, trip);
    }
    return streamFilter.toList();
  }
}
