package ca.ulaval.glo4003.hosting.filter;

import ca.ulaval.glo4003.hosting.Hosting;
import ca.ulaval.glo4003.trip.Trip;
import java.util.stream.Stream;

public class SearchHostingAvailableCapacityFilter implements SearchHostingFilter {

  @Override
  public Stream<Hosting> apply(Stream<Hosting> hosts, Trip trip) {
    int requiredCapacity = trip.getReservedSeats();

    return hosts.filter(hosting -> hosting.getAvailableCapacity() >= requiredCapacity);
  }
}
