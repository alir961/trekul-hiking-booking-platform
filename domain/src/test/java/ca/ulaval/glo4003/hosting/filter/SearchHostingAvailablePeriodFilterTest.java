package ca.ulaval.glo4003.hosting.filter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.hosting.Hosting;
import ca.ulaval.glo4003.trip.Trip;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SearchHostingAvailablePeriodFilterTest {

  private static final LocalDate ANY_DATE = LocalDate.of(2024, 1, 1);
  private Trip tripMock;
  private SearchHostingAvailablePeriodFilter periodFilter;

  @BeforeEach
  void setUp() {
    this.tripMock = mock(Trip.class);

    when(this.tripMock.getDate()).thenReturn(ANY_DATE);

    this.periodFilter = new SearchHostingAvailablePeriodFilter();
  }

  @Test
  void givenHostingAvailableWithinPeriod_whenFiltering_thenHostingIsStillPresent() {
    // GIVEN
    Hosting hostingAvailableInPeriod = mock(Hosting.class);
    List<Hosting> hosts = List.of(hostingAvailableInPeriod);

    when(hostingAvailableInPeriod.canBeReservedForDates(ANY_DATE, ANY_DATE)).thenReturn(true);

    // WHEN
    Stream<Hosting> hostingStream = this.periodFilter.apply(hosts.stream(), this.tripMock);

    // THEN
    Optional<Hosting> filteredHosting = hostingStream.findFirst();

    assertTrue(filteredHosting.isPresent());
    assertEquals(hostingAvailableInPeriod, filteredHosting.get());
  }

  @Test
  void givenHostingNotAvailableWithinPeriod_whenFiltering_thenHostingIsAbsent() {
    // GIVEN
    Hosting hostingUnavailableInPeriod = mock(Hosting.class);
    List<Hosting> hosts = List.of(hostingUnavailableInPeriod);

    when(hostingUnavailableInPeriod.canBeReservedForDates(ANY_DATE, ANY_DATE)).thenReturn(false);

    // WHEN
    Stream<Hosting> hostingStream = this.periodFilter.apply(hosts.stream(), this.tripMock);

    // THEN
    Optional<Hosting> filteredHosting = hostingStream.findFirst();

    assertFalse(filteredHosting.isPresent());
  }
}
