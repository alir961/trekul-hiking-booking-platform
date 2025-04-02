package ca.ulaval.glo4003.hosting.filter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4003.hosting.Hosting;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.trip.Trip;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SearchHostingPriceRangeTest {

  private static final Money MIN_PRICE = new Money("100");
  private static final Money MAX_PRICE = new Money("200");

  private final Trip tripMock = mock(Trip.class);

  private SearchHostingPriceRangeFilter priceRangeFilter;

  @BeforeEach
  void setUp() {
    this.priceRangeFilter = new SearchHostingPriceRangeFilter(MIN_PRICE, MAX_PRICE);
  }

  @Test
  void givenHostingAvailableWithinPriceRange_whenFiltering_thenHostingIsStillPresent() {
    // GIVEN
    Hosting hostingWithinPriceRange = mock(Hosting.class);
    List<Hosting> hosts = List.of(hostingWithinPriceRange);

    when(hostingWithinPriceRange.isBetweenPriceRange(MIN_PRICE, MAX_PRICE)).thenReturn(true);

    // WHEN
    Stream<Hosting> hostingStream = this.priceRangeFilter.apply(hosts.stream(), this.tripMock);

    // THEN
    Optional<Hosting> filteredHosting = hostingStream.findFirst();

    assertTrue(filteredHosting.isPresent());
    assertEquals(hostingWithinPriceRange, filteredHosting.get());
  }

  @Test
  void givenHostingNotAvailableWithinPriceRange_whenFiltering_thenHostingIsAbsent() {
    // GIVEN
    Hosting hostingOutsidePriceRange = mock(Hosting.class);
    List<Hosting> hosts = List.of(hostingOutsidePriceRange);

    when(hostingOutsidePriceRange.isBetweenPriceRange(MIN_PRICE, MAX_PRICE)).thenReturn(false);

    // WHEN
    Stream<Hosting> hostingStream = this.priceRangeFilter.apply(hosts.stream(), this.tripMock);

    // THEN
    Optional<Hosting> filteredHosting = hostingStream.findFirst();

    assertFalse(filteredHosting.isPresent());
  }
}
