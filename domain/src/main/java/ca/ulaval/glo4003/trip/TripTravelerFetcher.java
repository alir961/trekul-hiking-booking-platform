package ca.ulaval.glo4003.trip;

import ca.ulaval.glo4003.traveler.user.User;
import ca.ulaval.glo4003.traveler.user.UserRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TripTravelerFetcher {
  private final UserRepository userRepository;
  private final Set<User> users = new HashSet<>();

  public TripTravelerFetcher(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public List<User> fetchUsers(Trip trip) {
    List<User> users = this.userRepository.getBunch(trip.retrieveReservedUsers());
    this.users.addAll(users);

    return users;
  }

  public Set<User> fetchUpdatedUsers() {
    return this.users;
  }
}
