package ca.ulaval.glo4003.hosting.filter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.hiking.HikingRegion;
import ca.ulaval.glo4003.hosting.Hosting;
import ca.ulaval.glo4003.trip.Trip;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SearchHostingRegionFilterTest {

  private static final HikingRegion ANY_REGION = HikingRegion.GASPESIE;
  private static final HikingRegion ANY_OTHER_REGION = HikingRegion.CHARLEVOIX;
  private Trip tripMock;
  private SearchHostingRegionFilter regionFilter;

  @BeforeEach
  void setUp() {
    this.tripMock = mock(Trip.class);

    when(this.tripMock.getRegion()).thenReturn(ANY_REGION);

    this.regionFilter = new SearchHostingRegionFilter();
  }

  @Test
  void givenHostingInSpecifiedRegion_whenFiltering_thenHostingIsStillPresent() {
    // GIVEN
    Hosting hostingInRegion = mock(Hosting.class);
    List<Hosting> hosts = List.of(hostingInRegion);

    when(hostingInRegion.getRegion()).thenReturn(ANY_REGION);

    // WHEN
    Stream<Hosting> hostingStream = this.regionFilter.apply(hosts.stream(), this.tripMock);

    // THEN
    Optional<Hosting> filteredHosting = hostingStream.findFirst();

    assertTrue(filteredHosting.isPresent());
    assertEquals(hostingInRegion, filteredHosting.get());
  }

  @Test
  void givenHostingNotInSpecifiedRegion_whenFiltering_thenHostingIsAbsent() {
    // GIVEN
    Hosting hostingInRegion = mock(Hosting.class);
    List<Hosting> hosts = List.of(hostingInRegion);

    when(hostingInRegion.getRegion()).thenReturn(ANY_OTHER_REGION);

    // WHEN
    Stream<Hosting> hostingStream = this.regionFilter.apply(hosts.stream(), this.tripMock);

    // THEN
    Optional<Hosting> filteredHosting = hostingStream.findFirst();

    assertFalse(filteredHosting.isPresent());
  }
}
