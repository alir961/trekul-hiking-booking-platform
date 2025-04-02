package ca.ulaval.glo4003;

import ca.ulaval.glo4003.hosting.HostingRepository;
import ca.ulaval.glo4003.token.AuthTokenRepository;
import ca.ulaval.glo4003.traveler.user.UserRepository;
import ca.ulaval.glo4003.trip.TripRepository;

public interface RepositoryCreator {
  UserRepository createUserRepository();

  TripRepository createTripRepository();

  HostingRepository createHostingRepository();

  AuthTokenRepository createAuthTokenRepository();
}
