package ca.ulaval.glo4003.trip.filter;

import ca.ulaval.glo4003.trip.Trip;
import java.util.stream.Stream;

public interface SearchTripFilter {
  Stream<Trip> apply(Stream<Trip> trips);
}
