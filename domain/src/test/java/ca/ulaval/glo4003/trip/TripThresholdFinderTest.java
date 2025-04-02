package ca.ulaval.glo4003.trip;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.clock.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TripThresholdFinderTest {
  private static final Duration ANY_DURATION_THRESHOLD_IN_SECONDS = Duration.ofDays(7);
  private static final LocalDate NOW = LocalDate.now();
  private static final LocalDate ANY_DURATION_THRESHOLD_IN_DAYS =
      NOW.plusDays(ANY_DURATION_THRESHOLD_IN_SECONDS.toDays());
  private Clock clockMock;
  private TripRepository tripRepositoryMock;
  private TripThresholdFinder tripThresholdFinder;

  @BeforeEach
  void setUp() {
    this.clockMock = mock(Clock.class);
    this.tripRepositoryMock = mock(TripRepository.class);
    this.tripThresholdFinder =
        new TripThresholdFinder(
            this.clockMock, this.tripRepositoryMock, ANY_DURATION_THRESHOLD_IN_SECONDS);
    when(this.clockMock.today()).thenReturn(NOW);
  }

  @Test
  void whenFind_thenUseThresholdToSearchForTrips() {
    this.tripThresholdFinder.find();

    verify(this.tripRepositoryMock).findBetweenDates(NOW, ANY_DURATION_THRESHOLD_IN_DAYS);
  }

  @Test
  void whenFind_thenReturnTripsFromRepository() {
    List<Trip> anyTripsInRepo = new ArrayList<>();
    when(this.tripRepositoryMock.findBetweenDates(NOW, ANY_DURATION_THRESHOLD_IN_DAYS))
        .thenReturn(anyTripsInRepo);

    List<Trip> returnedTrips = this.tripThresholdFinder.find();

    assertSame(anyTripsInRepo, returnedTrips);
  }
}
