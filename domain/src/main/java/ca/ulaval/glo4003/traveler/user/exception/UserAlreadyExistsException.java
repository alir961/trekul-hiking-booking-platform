package ca.ulaval.glo4003.traveler.user.exception;

import ca.ulaval.glo4003.exception.TrekULException;

public class UserAlreadyExistsException extends TrekULException {
  public UserAlreadyExistsException() {
    super("CREATION_FAILED", "User already exists");
  }
}
