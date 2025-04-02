package ca.ulaval.glo4003.notification.filters;

import ca.ulaval.glo4003.traveler.user.User;
import ca.ulaval.glo4003.trip.Trip;
import java.util.List;

public class TripNotificationFilters {
  private final List<TripUserNotificationFilter> userFilters;

  public TripNotificationFilters(List<TripUserNotificationFilter> userFilters) {
    this.userFilters = userFilters;
  }

  public List<User> applyFiltersForUsers(List<User> unfilteredUsers, Trip trip) {
    List<User> filteredUsers = unfilteredUsers;
    for (TripUserNotificationFilter filter : this.userFilters) {
      filteredUsers = filter.notificationUserFilter(filteredUsers, trip);
    }
    return filteredUsers;
  }
}
