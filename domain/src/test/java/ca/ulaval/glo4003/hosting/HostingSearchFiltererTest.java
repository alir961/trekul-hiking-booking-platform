package ca.ulaval.glo4003.hosting;

import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.hosting.filter.SearchHostingFilter;
import ca.ulaval.glo4003.trip.Trip;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

public class HostingSearchFiltererTest {
  private final SearchHostingFilter firstFilter = mock(SearchHostingFilter.class);
  private final SearchHostingFilter secondFilter = mock(SearchHostingFilter.class);
  private Trip tripMock;
  private HostingSearchFilterer hostingSearchFilterer;

  @BeforeEach
  void setUp() {
    this.tripMock = mock(Trip.class);
    this.hostingSearchFilterer =
        new HostingSearchFilterer(List.of(this.firstFilter, this.secondFilter));
  }

  @Test
  void whenFilteringHostingList_thenFiltersAreApplied() {
    List<Hosting> hostingList = List.of(mock(Hosting.class));
    ArgumentCaptor<Stream<Hosting>> firstFilterCaptor = forClass(Stream.class);
    ArgumentCaptor<Stream<Hosting>> secondFilterCaptor = forClass(Stream.class);

    this.hostingSearchFilterer.filterSearch(hostingList, this.tripMock);

    verify(this.firstFilter).apply(firstFilterCaptor.capture(), eq(this.tripMock));
    verify(this.secondFilter).apply(secondFilterCaptor.capture(), eq(this.tripMock));
  }
}
