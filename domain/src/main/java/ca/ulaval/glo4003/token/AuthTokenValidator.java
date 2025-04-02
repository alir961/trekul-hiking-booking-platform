package ca.ulaval.glo4003.token;

import ca.ulaval.glo4003.token.exception.UserAuthenticationException;

public class AuthTokenValidator {
  public void validate(AuthToken token) {
    if (token.isExpired()) {
      throw new UserAuthenticationException();
    }
  }
}
