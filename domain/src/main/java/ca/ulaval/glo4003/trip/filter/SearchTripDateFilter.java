package ca.ulaval.glo4003.trip.filter;

import ca.ulaval.glo4003.trip.Trip;
import java.time.LocalDate;
import java.util.stream.Stream;

public class SearchTripDateFilter implements SearchTripFilter {
  private final LocalDate startTripDate;
  private final LocalDate endTripDate;

  public SearchTripDateFilter(LocalDate startTripDate, LocalDate endTripDate) {
    this.startTripDate = startTripDate;
    this.endTripDate = endTripDate;
  }

  @Override
  public Stream<Trip> apply(Stream<Trip> trips) {
    return trips.filter(trip -> trip.isBetweenDates(this.startTripDate, this.endTripDate));
  }
}
