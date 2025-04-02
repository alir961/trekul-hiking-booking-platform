package ca.ulaval.glo4003.token;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.ulaval.glo4003.traveler.TravelerID;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public abstract class AuthTokenRepositoryIT {
  private static final Duration ANY_EXPIRATION_LIMIT = Duration.ofSeconds(602);
  private static final TravelerID ANY_USER_ID = new TravelerID("ANY_ID");
  private static final Instant ANY_CREATED_TIME = Instant.ofEpochMilli(17334612);
  private static final String A_REGISTERED_TOKEN_VALUE = "01923tok";
  private static final String A_NON_EXISTING_TOKEN_VALUE = "lkmg0294";
  private AuthToken registeredToken;
  private AuthToken nonExistingToken;
  private AuthTokenRepository authTokenRepository;

  protected abstract AuthTokenRepository createRepository();

  @BeforeEach
  public void setUp() {
    this.registeredToken =
        new AuthToken(
            A_REGISTERED_TOKEN_VALUE, ANY_USER_ID, ANY_CREATED_TIME, ANY_EXPIRATION_LIMIT);
    this.nonExistingToken =
        new AuthToken(
            A_NON_EXISTING_TOKEN_VALUE, ANY_USER_ID, ANY_CREATED_TIME, ANY_EXPIRATION_LIMIT);
    this.authTokenRepository = this.createRepository();
    this.authTokenRepository.save(this.registeredToken);
  }

  @Test
  void givenTokenInRepository_whenGettingToken_thenTokenExists() {
    Optional<AuthToken> foundToken = this.authTokenRepository.get(A_REGISTERED_TOKEN_VALUE);
    assertTrue(foundToken.isPresent());
  }

  @Test
  void givenTokenInRepository_whenRemovingToken_thenTokenIsRemoved() {
    this.authTokenRepository.remove(this.registeredToken);

    Optional<AuthToken> foundToken = this.authTokenRepository.get(A_REGISTERED_TOKEN_VALUE);
    assertTrue(foundToken.isEmpty());
  }

  @Test
  void givenTokenNotInRepository_whenAddingToken_thenTokenIsAdded() {
    this.authTokenRepository.save(this.nonExistingToken);

    Optional<AuthToken> foundToken = this.authTokenRepository.get(A_NON_EXISTING_TOKEN_VALUE);

    assertTrue(foundToken.isPresent());
    assertEquals(this.nonExistingToken, foundToken.get());
  }

  @Test
  void givenTokenNotInRepository_whenGettingToken_thenTokenReturnedNotPresent() {
    Optional<AuthToken> foundToken = this.authTokenRepository.get(A_NON_EXISTING_TOKEN_VALUE);

    assertTrue(foundToken.isEmpty());
  }
}
