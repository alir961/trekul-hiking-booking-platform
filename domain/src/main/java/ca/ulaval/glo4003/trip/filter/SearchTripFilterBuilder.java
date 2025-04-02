package ca.ulaval.glo4003.trip.filter;

import ca.ulaval.glo4003.hiking.HikingRegion;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.trip.TripStatus;
import ca.ulaval.glo4003.trip.exception.TripInvalidCostFilterException;
import ca.ulaval.glo4003.trip.exception.TripInvalidDateFilterException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SearchTripFilterBuilder {
  private List<SearchTripFilter> filters = new ArrayList<>();

  public SearchTripFilterBuilder addSearchTripCostFilter(Money maximumCost) {
    if (!maximumCost.isStrictlyPositive()) {
      throw new TripInvalidCostFilterException();
    }
    this.filters.add(new SearchTripCostFilter(maximumCost));

    return this;
  }

  public SearchTripFilterBuilder addSearchTripDateFilter(LocalDate start, LocalDate end) {
    if (start.isAfter(end)) {
      throw new TripInvalidDateFilterException();
    }
    this.filters.add(new SearchTripDateFilter(start, end));

    return this;
  }

  public SearchTripFilterBuilder addSearchTripRegionFilter(HikingRegion region) {
    this.filters.add(new SearchTripRegionFilter(region));

    return this;
  }

  public SearchTripFilterBuilder addSearchTripStatusFilter(TripStatus status) {
    this.filters.add(new SearchTripStatusFilter(status));

    return this;
  }

  public List<SearchTripFilter> build() {
    return this.filters;
  }
}
