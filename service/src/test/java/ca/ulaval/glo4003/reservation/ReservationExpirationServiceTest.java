package ca.ulaval.glo4003.reservation;

import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.group.Group;
import ca.ulaval.glo4003.traveler.user.User;
import ca.ulaval.glo4003.traveler.user.UserRepository;
import ca.ulaval.glo4003.trip.Trip;
import ca.ulaval.glo4003.trip.TripID;
import ca.ulaval.glo4003.trip.TripRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReservationExpirationServiceTest {

  private static final TripID ANY_TRIP_ID = TripID.generate();

  private ReservationExpirationService reservationExpirationService;
  private UserRepository userRepositoryMock;
  private TripRepository tripRepositoryMock;
  private Reservation expiredReservationMock;
  private Reservation activeReservationMock;
  private Group groupMock;
  private Trip tripMock;
  private User userMock;

  @BeforeEach
  void setUp() {
    this.userMock = mock(User.class);
    this.tripRepositoryMock = mock(TripRepository.class);
    this.userRepositoryMock = mock(UserRepository.class);
    this.reservationExpirationService =
        new ReservationExpirationService(this.userRepositoryMock, this.tripRepositoryMock);
    this.expiredReservationMock = mock(Reservation.class);
    this.activeReservationMock = mock(Reservation.class);
    this.tripMock = mock(Trip.class);
    this.groupMock = mock(Group.class);

    when(this.expiredReservationMock.isExpired()).thenReturn(true);
    when(this.activeReservationMock.isExpired()).thenReturn(false);
    when(this.tripRepositoryMock.get(ANY_TRIP_ID)).thenReturn(Optional.of(this.tripMock));
    when(this.userRepositoryMock.getAll()).thenReturn(List.of(this.userMock));
  }

  @Test
  void whenCancelExpiredReservations_thenExpiredReservationsSeatsReleased() {
    this.setUpActiveAndExpiredReservation();

    this.reservationExpirationService.cancelExpiredReservations();

    verify(this.tripMock).releaseSeat(this.expiredReservationMock);
  }

  @Test
  void whenCancelExpiredReservations_thenExpiredReservationsAreProcessed() {
    this.setUpActiveAndExpiredReservation();
    this.reservationExpirationService.cancelExpiredReservations();

    verify(this.tripMock).releaseSeat(this.expiredReservationMock);
    verify(this.tripRepositoryMock).save(this.tripMock);
  }

  @Test
  void whenCancelExpiredReservations_thenUserCancelExpiredReservationsIsCalled() {
    this.setUpActiveAndExpiredReservation();
    this.reservationExpirationService.cancelExpiredReservations();

    verify(this.userMock).cancelExpiredReservations();
  }

  @Test
  void givenMultipleUsers_whenCancelExpiredReservations_thenEachUserIsProcessed() {
    this.setUpActiveAndExpiredReservation();
    User anotherUserMock = mock(User.class);
    Reservation anotherExpiredReservationMock = mock(Reservation.class);
    when(this.userRepositoryMock.getAll()).thenReturn(List.of(this.userMock, anotherUserMock));
    when(anotherUserMock.cancelExpiredReservations())
        .thenReturn(List.of(anotherExpiredReservationMock));
    when(anotherExpiredReservationMock.getTripID()).thenReturn(ANY_TRIP_ID);
    when(anotherExpiredReservationMock.getGroup()).thenReturn(this.groupMock);

    this.reservationExpirationService.cancelExpiredReservations();

    verify(this.userMock).cancelExpiredReservations();
    verify(anotherUserMock).cancelExpiredReservations();
  }

  private void setUpActiveAndExpiredReservation() {
    when(this.userMock.cancelExpiredReservations())
        .thenReturn(List.of(this.expiredReservationMock));
    when(this.expiredReservationMock.getTripID()).thenReturn(ANY_TRIP_ID);
    when(this.activeReservationMock.getTripID()).thenReturn(ANY_TRIP_ID);
    when(this.expiredReservationMock.getGroup()).thenReturn(this.groupMock);
    when(this.tripRepositoryMock.get(ANY_TRIP_ID)).thenReturn(Optional.of(this.tripMock));
  }
}
