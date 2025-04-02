package ca.ulaval.glo4003.token.exception;

import ca.ulaval.glo4003.exception.TrekULAuthenticationException;

public class UserAuthenticationException extends TrekULAuthenticationException {

  public UserAuthenticationException() {
    super("USER_AUTH_EXPIRED", "The token is invalid or expired, please log in");
  }
}
