package ca.ulaval.glo4003.hosting.filter;

import ca.ulaval.glo4003.hosting.Hosting;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.trip.Trip;
import java.util.stream.Stream;

public class SearchHostingPriceRangeFilter implements SearchHostingFilter {
  private final Money minPrice;
  private final Money maxPrice;

  public SearchHostingPriceRangeFilter(Money minPrice, Money maxPrice) {
    this.minPrice = minPrice;
    this.maxPrice = maxPrice;
  }

  @Override
  public Stream<Hosting> apply(Stream<Hosting> hosts, Trip trip) {
    return hosts.filter(hosting -> hosting.isBetweenPriceRange(this.minPrice, this.maxPrice));
  }
}
