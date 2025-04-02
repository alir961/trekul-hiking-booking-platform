package ca.ulaval.glo4003.notification.filters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4003.reservation.Reservation;
import ca.ulaval.glo4003.traveler.user.User;
import ca.ulaval.glo4003.trip.Trip;
import ca.ulaval.glo4003.trip.TripID;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TripUserNotificationFilterUnpaidReservationTest {
  private static final TripID ANY_TRIP_ID = TripID.generate();
  private TripNotificationFilterUnpaidReservation filter;
  private User anyFirstUser;
  private User anySecondUser;
  private Trip anyTrip;
  private Reservation anyReservation;

  @BeforeEach
  void setUp() {
    this.filter = new TripNotificationFilterUnpaidReservation();
    this.anyFirstUser = mock(User.class);
    this.anySecondUser = mock(User.class);
    this.anyTrip = mock(Trip.class);
    this.anyReservation = mock(Reservation.class);

    when(this.anyTrip.getId()).thenReturn(ANY_TRIP_ID);
  }

  @Test
  void givenUsersWithReservations_whenFiltering_thenKeepUsersWithReservationsForTrip() {
    when(this.anyReservation.getTripID()).thenReturn(ANY_TRIP_ID);
    when(this.anyFirstUser.hasReservation(ANY_TRIP_ID)).thenReturn(true);
    when(this.anySecondUser.hasReservation(ANY_TRIP_ID)).thenReturn(false);

    List<User> filteredUsers =
        this.filter.notificationUserFilter(
            List.of(this.anyFirstUser, this.anySecondUser), this.anyTrip);

    assertEquals(1, filteredUsers.size());
    assertEquals(this.anyFirstUser, filteredUsers.getFirst());
  }

  @Test
  void givenUsersWithoutReservations_whenFiltering_thenFilterOutUsersWithoutReservations() {
    when(this.anyFirstUser.getReservations()).thenReturn(List.of());
    when(this.anySecondUser.getReservations()).thenReturn(List.of());

    List<User> filteredUsers =
        this.filter.notificationUserFilter(
            List.of(this.anyFirstUser, this.anySecondUser), this.anyTrip);

    assertEquals(0, filteredUsers.size());
  }
}
