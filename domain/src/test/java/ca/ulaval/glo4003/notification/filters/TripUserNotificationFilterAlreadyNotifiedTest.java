package ca.ulaval.glo4003.notification.filters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.user.User;
import ca.ulaval.glo4003.trip.Trip;
import ca.ulaval.glo4003.trip.TripID;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TripUserNotificationFilterAlreadyNotifiedTest {
  private TripNotificationFilterAlreadyNotified filter;
  private User anyFirstUser;
  private User anySecondUser;
  private Trip anyTrip;
  private TripID anyTripID;

  @BeforeEach
  void setUp() {
    this.filter = new TripNotificationFilterAlreadyNotified();
    this.anyFirstUser = mock(User.class);
    this.anySecondUser = mock(User.class);
    this.anyTrip = mock(Trip.class);
    this.anyTripID = TripID.generate();
    final TravelerID anyFirstUserID = new TravelerID("USER1");
    final TravelerID anySecondUserID = new TravelerID("USER2");

    when(this.anyTrip.getId()).thenReturn(this.anyTripID);
    when(this.anyFirstUser.getId()).thenReturn(anyFirstUserID);
    when(this.anySecondUser.getId()).thenReturn(anySecondUserID);
  }

  @Test
  void givenUsersAlreadyNotified_whenFiltering_thenFilterOutNotifiedUsers() {
    when(this.anyFirstUser.hasBeenNotifiedForTrip(this.anyTripID)).thenReturn(true);
    when(this.anySecondUser.hasBeenNotifiedForTrip(this.anyTripID)).thenReturn(false);

    List<User> filteredUsers =
        this.filter.notificationUserFilter(
            List.of(this.anyFirstUser, this.anySecondUser), this.anyTrip);

    int expectedNumberOfUsersLeft = 1;
    assertEquals(expectedNumberOfUsersLeft, filteredUsers.size());
    assertEquals(this.anySecondUser, filteredUsers.getFirst());
  }

  @Test
  void givenUsersNotNotified_whenFiltering_thenKeepAllUsers() {
    when(this.anyFirstUser.hasBeenNotifiedForTrip(this.anyTripID)).thenReturn(false);
    when(this.anySecondUser.hasBeenNotifiedForTrip(this.anyTripID)).thenReturn(false);

    List<User> filteredUsers =
        this.filter.notificationUserFilter(
            List.of(this.anyFirstUser, this.anySecondUser), this.anyTrip);

    int expectedNumberOfUsersLeft = 2;
    assertEquals(expectedNumberOfUsersLeft, filteredUsers.size());
    assertEquals(this.anyFirstUser, filteredUsers.getFirst());
    assertEquals(this.anySecondUser, filteredUsers.get(1));
  }
}
