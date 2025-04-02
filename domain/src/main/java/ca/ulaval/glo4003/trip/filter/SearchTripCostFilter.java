package ca.ulaval.glo4003.trip.filter;

import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.trip.Trip;
import java.util.stream.Stream;

public class SearchTripCostFilter implements SearchTripFilter {
  private final Money costLimit;

  public SearchTripCostFilter(Money costLimit) {
    this.costLimit = costLimit;
  }

  @Override
  public Stream<Trip> apply(Stream<Trip> trips) {
    return trips.filter(trip -> trip.getCost().isLowerOrEqualThan(this.costLimit));
  }
}
