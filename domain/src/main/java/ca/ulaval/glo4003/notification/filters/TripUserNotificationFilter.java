package ca.ulaval.glo4003.notification.filters;

import ca.ulaval.glo4003.traveler.user.User;
import ca.ulaval.glo4003.trip.Trip;
import java.util.List;

public interface TripUserNotificationFilter {
  List<User> notificationUserFilter(List<User> unfilteredUsers, Trip trip);
}
