package ca.ulaval.glo4003.user;

import ca.ulaval.glo4003.traveler.user.UserRepository;

public class InMemoryUserRepositoryImplTest extends UserRepositoryIT {

  @Override
  protected UserRepository createRepository() {
    return new InMemoryUserRepositoryImpl();
  }
}
