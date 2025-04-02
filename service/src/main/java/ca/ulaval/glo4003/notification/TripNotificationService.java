package ca.ulaval.glo4003.notification;

import ca.ulaval.glo4003.notification.filters.TripNotificationFilters;
import ca.ulaval.glo4003.notification.types.trip.upcoming.UpcomingTripNotificationProcessor;
import ca.ulaval.glo4003.traveler.user.User;
import ca.ulaval.glo4003.traveler.user.UserRepository;
import ca.ulaval.glo4003.trip.Trip;
import ca.ulaval.glo4003.trip.TripThresholdFinder;
import java.util.List;

public class TripNotificationService {
  private final TripNotificationFilters filters;
  private final UpcomingTripNotificationProcessor tripUpcomingTripNotificationProcessor;
  private final TripThresholdFinder tripThresholdFinder;
  private final UserRepository userRepository;

  public TripNotificationService(
      UserRepository userRepository,
      TripNotificationFilters filters,
      TripThresholdFinder tripThresholdFinder,
      UpcomingTripNotificationProcessor tripUpcomingTripNotificationProcessor) {
    this.userRepository = userRepository;
    this.filters = filters;
    this.tripThresholdFinder = tripThresholdFinder;
    this.tripUpcomingTripNotificationProcessor = tripUpcomingTripNotificationProcessor;
  }

  public void notifyUpComingTrips() {
    List<Trip> upcomingTrips = this.tripThresholdFinder.find();
    upcomingTrips.forEach(this::notifyUpComingTrip);
  }

  private void notifyUpComingTrip(Trip trip) {
    List<User> usersForTrip = this.userRepository.getBunch(trip.retrieveReservedUsers());
    List<User> usersToBeNotified = this.filters.applyFiltersForUsers(usersForTrip, trip);
    this.tripUpcomingTripNotificationProcessor.notify(usersToBeNotified, trip);
  }
}
