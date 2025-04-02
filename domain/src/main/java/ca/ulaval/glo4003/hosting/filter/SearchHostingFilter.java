package ca.ulaval.glo4003.hosting.filter;

import ca.ulaval.glo4003.hosting.Hosting;
import ca.ulaval.glo4003.trip.Trip;
import java.util.stream.Stream;

public interface SearchHostingFilter {
  Stream<Hosting> apply(Stream<Hosting> hosts, Trip trip);
}
