package ca.ulaval.glo4003.traveler.user.exception;

import ca.ulaval.glo4003.exception.TrekULException;

public class UserCreationInvalidPasswordException extends TrekULException {
  public UserCreationInvalidPasswordException(String criteria) {
    super("USER_CREATION_FAILED", "Invalid password. " + criteria);
  }
}
