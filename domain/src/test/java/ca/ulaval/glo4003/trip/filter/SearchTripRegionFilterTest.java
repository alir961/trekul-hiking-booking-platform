package ca.ulaval.glo4003.trip.filter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4003.hiking.HikingRegion;
import ca.ulaval.glo4003.trip.Trip;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SearchTripRegionFilterTest {
  private static final HikingRegion ANY_REGION = HikingRegion.GASPESIE;
  private SearchTripRegionFilter regionFilter;

  @BeforeEach
  void setUp() {
    this.regionFilter = new SearchTripRegionFilter(ANY_REGION);
  }

  @Test
  void givenTripWithSameRegionHiking_whenFiltering_thenTripIsStillPresent() {
    Trip lowerCostTrip = mock(Trip.class);
    List<Trip> trips = List.of(lowerCostTrip);
    when(lowerCostTrip.getRegion()).thenReturn(ANY_REGION);

    Stream<Trip> tripStream = this.regionFilter.apply(trips.stream());
    List<Trip> filteredList = tripStream.toList();

    assertFalse(filteredList.isEmpty());
    assertEquals(lowerCostTrip, filteredList.get(0));
  }
}
