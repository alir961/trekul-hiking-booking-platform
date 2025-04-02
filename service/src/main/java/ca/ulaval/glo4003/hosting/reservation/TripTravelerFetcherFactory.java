package ca.ulaval.glo4003.hosting.reservation;

import ca.ulaval.glo4003.traveler.user.UserRepository;
import ca.ulaval.glo4003.trip.TripTravelerFetcher;

public class TripTravelerFetcherFactory {

  public TripTravelerFetcher create(UserRepository userRepository) {
    return new TripTravelerFetcher(userRepository);
  }
}
