package ca.ulaval.glo4003.hosting.filter;

import ca.ulaval.glo4003.hosting.Hosting;
import ca.ulaval.glo4003.trip.Trip;
import java.time.LocalDate;
import java.util.stream.Stream;

public class SearchHostingAvailablePeriodFilter implements SearchHostingFilter {

  @Override
  public Stream<Hosting> apply(Stream<Hosting> hosts, Trip trip) {
    LocalDate date = trip.getDate();

    return hosts.filter(hosting -> hosting.canBeReservedForDates(date, date));
  }
}
