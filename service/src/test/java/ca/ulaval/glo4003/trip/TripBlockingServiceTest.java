package ca.ulaval.glo4003.trip;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.trip.exception.TripNotFoundException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TripBlockingServiceTest {
  private static final TripID ANY_EXISTING_TRIP_ID = TripID.generate();
  private static final Duration ANY_BLOCKING_DURATION = Duration.ofDays(5);
  private Trip tripMock;
  private TripRepository tripRepositoryMock;
  private TripBlockingService tripBlockingService;
  private Trip anyExistingTrip;

  @BeforeEach
  void setUp() {
    this.tripRepositoryMock = this.mockTripRepositoryWithExistingTrip();

    this.tripBlockingService =
        new TripBlockingService(this.tripRepositoryMock, ANY_BLOCKING_DURATION);

    this.tripMock = mock(Trip.class);
  }

  @Test
  void givenTripIsBeforeBlockingDate_whenBlockingUpcomingTrips_thenTripIsBlocked() {
    this.whenTripRepositoryFindBetweenDatesThenReturnListOf(this.tripMock);

    this.tripBlockingService.blockUpcomingTrips();

    verify(this.tripMock).blockTrip();
  }

  @Test
  void givenTripIsBeforeBlockingDate_whenBlockingUpcomingTrips_thenTripIsSaved() {
    this.whenTripRepositoryFindBetweenDatesThenReturnListOf(this.tripMock);

    this.tripBlockingService.blockUpcomingTrips();

    verify(this.tripRepositoryMock).save(this.tripMock);
  }

  @Test
  void givenAnExistingUserWithPermission_whenBlockingTrip_thenTripIsBlocked() {
    this.tripBlockingService.blockTrip(ANY_EXISTING_TRIP_ID);

    verify(this.anyExistingTrip).blockTrip();
    verify(this.tripRepositoryMock).save(this.anyExistingTrip);
  }

  @Test
  void givenNonExistentTrip_whenBlockingTrip_thenThrowTripNotFoundException() {
    TripID anyNonExistingTripID = TripID.generate();
    when(this.tripRepositoryMock.get(anyNonExistingTripID)).thenReturn(Optional.empty());

    assertThrows(
        TripNotFoundException.class,
        () -> this.tripBlockingService.blockTrip(anyNonExistingTripID));
  }

  private void whenTripRepositoryFindBetweenDatesThenReturnListOf(Trip tripToReturn) {
    List<Trip> listToReturn = new ArrayList<>();
    listToReturn.add(tripToReturn);

    LocalDate yesterday = LocalDate.now().minusDays(1);
    LocalDate blockingDay = LocalDate.now().plusDays(ANY_BLOCKING_DURATION.toDays());

    when(this.tripRepositoryMock.findBetweenDates(yesterday, blockingDay)).thenReturn(listToReturn);
  }

  private TripRepository mockTripRepositoryWithExistingTrip() {
    TripRepository tripRepository = mock(TripRepository.class);
    this.anyExistingTrip = mock(Trip.class);
    when(tripRepository.get(ANY_EXISTING_TRIP_ID)).thenReturn(Optional.of(this.anyExistingTrip));

    return tripRepository;
  }
}
