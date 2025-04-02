package ca.ulaval.glo4003.token;

import ca.ulaval.glo4003.token.exception.UserAuthenticationException;
import ca.ulaval.glo4003.traveler.TravelerID;

public class AuthTokenService {
  private final AuthTokenRepository authTokenRepository;
  private final AuthTokenValidator authTokenValidator;

  public AuthTokenService(
      AuthTokenRepository authTokenRepository, AuthTokenValidator authTokenValidator) {
    this.authTokenRepository = authTokenRepository;
    this.authTokenValidator = authTokenValidator;
  }

  public TravelerID readToken(String tokenValue) {
    AuthToken token =
        this.authTokenRepository.get(tokenValue).orElseThrow(UserAuthenticationException::new);
    this.authTokenValidator.validate(token);
    return token.getUserID();
  }
}
