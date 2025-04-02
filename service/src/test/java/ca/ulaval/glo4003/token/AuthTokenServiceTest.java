package ca.ulaval.glo4003.token;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.token.exception.UserAuthenticationException;
import ca.ulaval.glo4003.traveler.TravelerID;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AuthTokenServiceTest {
  private static final Duration ANY_EXPIRATION_LIMIT = Duration.ofSeconds(701);
  private static final Instant ANY_CREATED_TIME = Instant.ofEpochMilli(17334612);
  private AuthTokenRepository mockAuthTokenRepo;
  private AuthTokenValidator spyAuthTokenValidator;
  private AuthTokenService authTokenService;

  @BeforeEach
  void setUp() {
    this.mockAuthTokenRepo = mock(AuthTokenRepository.class);
    this.spyAuthTokenValidator = mock(AuthTokenValidator.class);

    this.authTokenService =
        new AuthTokenService(this.mockAuthTokenRepo, this.spyAuthTokenValidator);
  }

  @Test
  void givenExistingToken_whenReadingToken_thenValidateIt() {
    String tokenValue = "f0scs";
    AuthToken existingToken = this.createAuthToken(tokenValue);
    when(this.mockAuthTokenRepo.get(tokenValue)).thenReturn(Optional.of(existingToken));

    this.authTokenService.readToken(tokenValue);

    verify(this.spyAuthTokenValidator).validate(existingToken);
  }

  @Test
  void givenNonExistingToken_whenReadingToken_thenThrowUserAuthenticationException() {
    String tokenValue = "asf13rff";
    when(this.mockAuthTokenRepo.get(tokenValue)).thenReturn(Optional.empty());

    assertThrows(
        UserAuthenticationException.class, () -> this.authTokenService.readToken(tokenValue));
  }

  @Test
  void givenValidToken_whenReadingToken_thenGetUserIDFromToken() {
    String tokenValue = "rf9v8vsw";
    AuthToken validToken = this.createAuthToken(tokenValue);
    when(this.mockAuthTokenRepo.get(tokenValue)).thenReturn(Optional.of(validToken));

    TravelerID result = this.authTokenService.readToken(tokenValue);

    assertEquals(validToken.getUserID(), result);
  }

  private AuthToken createAuthToken(String token) {
    TravelerID anyUserID = new TravelerID("s0g39jd");
    return new AuthToken(token, anyUserID, ANY_CREATED_TIME, ANY_EXPIRATION_LIMIT);
  }
}
