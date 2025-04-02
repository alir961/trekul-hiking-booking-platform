package ca.ulaval.glo4003.notification.filters;

import ca.ulaval.glo4003.traveler.user.User;
import ca.ulaval.glo4003.trip.Trip;
import java.util.List;

public class TripNotificationFilterAlreadyNotified implements TripUserNotificationFilter {

  @Override
  public List<User> notificationUserFilter(List<User> users, Trip trip) {
    return users.stream().filter(user -> !user.hasBeenNotifiedForTrip(trip.getId())).toList();
  }
}
