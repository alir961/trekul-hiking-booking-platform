package ca.ulaval.glo4003.traveler.user.exception;

import ca.ulaval.glo4003.exception.TrekULException;

public class UserAlreadyExistsWithThisEmailException extends TrekULException {
  public UserAlreadyExistsWithThisEmailException() {
    super("USER_CREATION_FAILED", "User already exists with this email");
  }
}
