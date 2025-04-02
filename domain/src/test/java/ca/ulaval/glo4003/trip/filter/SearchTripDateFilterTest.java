package ca.ulaval.glo4003.trip.filter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4003.trip.Trip;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SearchTripDateFilterTest {
  private static final LocalDate ANY_START_DATE = LocalDate.of(2024, 5, 25);
  private static final LocalDate ANY_END_DATE = LocalDate.of(2024, 6, 25);
  private SearchTripDateFilter dateFilter;

  @BeforeEach
  void setUp() {
    this.dateFilter = new SearchTripDateFilter(ANY_START_DATE, ANY_END_DATE);
  }

  @Test
  void givenTripWithValidDate_whenFiltering_thenTripIsStillPresent() {
    Trip lowerCostTrip = mock(Trip.class);
    List<Trip> trips = List.of(lowerCostTrip);
    when(lowerCostTrip.isBetweenDates(ANY_START_DATE, ANY_END_DATE)).thenReturn(true);

    Stream<Trip> tripStream = this.dateFilter.apply(trips.stream());
    List<Trip> filteredList = tripStream.toList();

    assertFalse(filteredList.isEmpty());
    assertEquals(lowerCostTrip, filteredList.get(0));
  }

  @Test
  void givenTripWithInvalidDate_whenFiltering_thenTripIsFilteredFromList() {
    Trip lowerCostTrip = mock(Trip.class);
    List<Trip> trips = List.of(lowerCostTrip);
    when(lowerCostTrip.isBetweenDates(ANY_START_DATE, ANY_END_DATE)).thenReturn(false);

    Stream<Trip> tripStream = this.dateFilter.apply(trips.stream());
    List<Trip> filteredList = tripStream.toList();

    assertTrue(filteredList.isEmpty());
  }
}
