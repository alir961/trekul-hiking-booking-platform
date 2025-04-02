package ca.ulaval.glo4003.token;

public class InMemoryAuthTokenRepositoryTest extends AuthTokenRepositoryIT {

  @Override
  public AuthTokenRepository createRepository() {
    return new InMemoryAuthTokenRepositoryImpl();
  }
}
