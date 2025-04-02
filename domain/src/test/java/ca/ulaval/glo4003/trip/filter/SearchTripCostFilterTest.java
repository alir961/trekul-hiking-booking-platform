package ca.ulaval.glo4003.trip.filter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.trip.Trip;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SearchTripCostFilterTest {
  private static final Money ANY_LIMIT_AMOUNT = new Money("50");
  private static final Money ANY_LOWER_AMOUNT = new Money("40");
  private static final Money ANY_HIGHER_AMOUNT = new Money("60");
  private SearchTripCostFilter costFilter;

  @BeforeEach
  void setUp() {
    this.costFilter = new SearchTripCostFilter(ANY_LIMIT_AMOUNT);
  }

  @Test
  void givenTripWithLowerPrice_whenFiltering_thenTripIsStillPresent() {
    Trip lowerCostTrip = mock(Trip.class);
    List<Trip> trips = List.of(lowerCostTrip);
    when(lowerCostTrip.getCost()).thenReturn(ANY_LOWER_AMOUNT);

    Stream<Trip> tripStream = this.costFilter.apply(trips.stream());
    List<Trip> filteredList = tripStream.toList();

    assertFalse(filteredList.isEmpty());
    assertEquals(lowerCostTrip, filteredList.getFirst());
  }

  @Test
  void givenTripWithHigherPrice_whenFiltering_thenTripIsFilteredFromList() {
    Trip lowerCostTrip = mock(Trip.class);
    List<Trip> trips = List.of(lowerCostTrip);
    when(lowerCostTrip.getCost()).thenReturn(ANY_HIGHER_AMOUNT);

    Stream<Trip> tripStream = this.costFilter.apply(trips.stream());
    List<Trip> filteredList = tripStream.toList();

    assertTrue(filteredList.isEmpty());
  }
}
