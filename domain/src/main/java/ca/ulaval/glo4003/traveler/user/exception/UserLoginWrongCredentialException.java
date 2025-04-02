package ca.ulaval.glo4003.traveler.user.exception;

import ca.ulaval.glo4003.exception.TrekULAuthenticationException;

public class UserLoginWrongCredentialException extends TrekULAuthenticationException {
  public UserLoginWrongCredentialException() {
    super("USER_WRONG_CREDENTIAL", "Invalid credentials");
  }
}
