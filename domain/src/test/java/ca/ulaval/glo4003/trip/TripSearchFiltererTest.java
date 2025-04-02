package ca.ulaval.glo4003.trip;

import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.trip.filter.SearchTripFilter;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

public class TripSearchFiltererTest {
  private final SearchTripFilter firstFilter = mock(SearchTripFilter.class);
  private final SearchTripFilter secondFilter = mock(SearchTripFilter.class);
  private TripSearchFilterer tripSearchFilterer;
  private Trip trip;

  @BeforeEach
  void setUp() {
    this.tripSearchFilterer = new TripSearchFilterer();
    this.trip = mock(Trip.class);
  }

  @Test
  void whenFilteringTripList_thenFiltersAreApplied() {
    List<Trip> tripList = List.of(this.trip);
    List<SearchTripFilter> filters = List.of(this.firstFilter, this.secondFilter);
    ArgumentCaptor<Stream<Trip>> firstFilterCaptor = forClass(Stream.class);
    ArgumentCaptor<Stream<Trip>> secondFilterCaptor = forClass(Stream.class);

    this.tripSearchFilterer.filterSearch(tripList, filters);

    verify(this.firstFilter).apply(firstFilterCaptor.capture());
    verify(this.secondFilter).apply(secondFilterCaptor.capture());
  }
}
