package ca.ulaval.glo4003.token;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4003.clock.Clock;
import ca.ulaval.glo4003.token.exception.UserAuthenticationException;
import ca.ulaval.glo4003.traveler.TravelerID;
import java.time.Duration;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AuthTokenValidatorTest {
  private static final Instant NOW = Instant.now();
  private static final Duration ANY_EXPIRATION_DURATION = Duration.ofMinutes(5);
  private static final Instant ANY_OUTDATED_TIME = NOW.minus(Duration.ofMinutes(6));
  private static final Instant ANY_VALID_TIME = NOW.minus(Duration.ofMinutes(4));
  private static final TravelerID ANY_USER_ID = new TravelerID("10lld09a");
  private static final String ANY_TOKEN_VALUE = "10ls0a93j";
  private AuthTokenValidator validator;

  @BeforeEach
  void setUp() {
    Clock clock = mock(Clock.class);
    when(clock.now()).thenReturn(NOW);
    this.validator = new AuthTokenValidator();
  }

  @Test
  void givenOutdatedToken_whenValidating_thenValidationThrowsUserAuthenticationException() {
    AuthToken token =
        new AuthToken(ANY_TOKEN_VALUE, ANY_USER_ID, ANY_OUTDATED_TIME, ANY_EXPIRATION_DURATION);

    assertThrows(UserAuthenticationException.class, () -> this.validator.validate(token));
  }

  @Test
  void givenValidToken_whenValidating_thenValidationDoesNotThrowsUserAuthenticationException() {
    AuthToken token =
        new AuthToken(ANY_TOKEN_VALUE, ANY_USER_ID, ANY_VALID_TIME, ANY_EXPIRATION_DURATION);

    assertDoesNotThrow(() -> this.validator.validate(token));
  }
}
