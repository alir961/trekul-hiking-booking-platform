package ca.ulaval.glo4003.hosting.filter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4003.hosting.Hosting;
import ca.ulaval.glo4003.trip.Trip;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SearchHostingAvailableCapacityFilterTest {

  private static final int REQUIRED_CAPACITY = 1;
  private Trip tripMock;
  private SearchHostingAvailableCapacityFilter capacityFilter;

  @BeforeEach
  void setUp() {
    this.tripMock = mock(Trip.class);

    when(this.tripMock.getReservedSeats()).thenReturn(REQUIRED_CAPACITY);

    this.capacityFilter = new SearchHostingAvailableCapacityFilter();
  }

  @Test
  void givenHostingWithAvailableCapacity_whenFiltering_thenHostingIsStillPresent() {
    // GIVEN
    Hosting availableHosting = mock(Hosting.class);
    List<Hosting> hosts = List.of(availableHosting);

    when(availableHosting.getAvailableCapacity()).thenReturn(REQUIRED_CAPACITY);

    // WHEN
    Stream<Hosting> hostingStream = this.capacityFilter.apply(hosts.stream(), this.tripMock);

    // THEN
    Optional<Hosting> filteredHosting = hostingStream.findFirst();

    assertTrue(filteredHosting.isPresent());
    assertEquals(availableHosting, filteredHosting.get());
  }

  @Test
  void givenHostingWithoutAvailableCapacity_whenFiltering_thenHostingIsAbsent() {
    // GIVEN
    Hosting unavailableHosting = mock(Hosting.class);
    List<Hosting> hosts = List.of(unavailableHosting);

    when(unavailableHosting.getAvailableCapacity()).thenReturn(REQUIRED_CAPACITY - 1);

    // WHEN
    Stream<Hosting> hostingStream = this.capacityFilter.apply(hosts.stream(), this.tripMock);

    // THEN
    Optional<Hosting> filteredHosting = hostingStream.findFirst();
    assertFalse(filteredHosting.isPresent());
  }
}
