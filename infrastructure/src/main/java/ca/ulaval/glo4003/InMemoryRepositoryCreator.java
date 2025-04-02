package ca.ulaval.glo4003;

import ca.ulaval.glo4003.hosting.HostingRepository;
import ca.ulaval.glo4003.hosting.InMemoryHostingRepositoryImpl;
import ca.ulaval.glo4003.token.AuthTokenRepository;
import ca.ulaval.glo4003.token.InMemoryAuthTokenRepositoryImpl;
import ca.ulaval.glo4003.traveler.user.UserRepository;
import ca.ulaval.glo4003.trip.InMemoryTripRepositoryImpl;
import ca.ulaval.glo4003.trip.TripRepository;
import ca.ulaval.glo4003.user.InMemoryUserRepositoryImpl;

public class InMemoryRepositoryCreator implements RepositoryCreator {
  private final UserRepository userRepository = new InMemoryUserRepositoryImpl();
  private final AuthTokenRepository authTokenRepository = new InMemoryAuthTokenRepositoryImpl();
  private final TripRepository tripRepository = new InMemoryTripRepositoryImpl();
  private final HostingRepository hostingRepository = new InMemoryHostingRepositoryImpl();

  @Override
  public UserRepository createUserRepository() {
    return this.userRepository;
  }

  @Override
  public AuthTokenRepository createAuthTokenRepository() {
    return this.authTokenRepository;
  }

  @Override
  public TripRepository createTripRepository() {
    return this.tripRepository;
  }

  @Override
  public HostingRepository createHostingRepository() {
    return this.hostingRepository;
  }
}
