package ca.ulaval.glo4003.hosting.filter;

import ca.ulaval.glo4003.hiking.HikingRegion;
import ca.ulaval.glo4003.hosting.Hosting;
import ca.ulaval.glo4003.trip.Trip;
import java.util.stream.Stream;

public class SearchHostingRegionFilter implements SearchHostingFilter {

  @Override
  public Stream<Hosting> apply(Stream<Hosting> hosts, Trip trip) {
    HikingRegion region = trip.getRegion();

    return hosts.filter(hosting -> hosting.getRegion() == region);
  }
}
