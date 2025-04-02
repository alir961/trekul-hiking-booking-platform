package ca.ulaval.glo4003.traveler.user.exception;

import ca.ulaval.glo4003.exception.TrekULException;

public class UserCreationInvalidEmailException extends TrekULException {
  public UserCreationInvalidEmailException() {
    super("USER_CREATION_FAILED", "Invalid email");
  }
}
