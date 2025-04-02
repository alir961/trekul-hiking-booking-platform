package ca.ulaval.glo4003.traveler.user.exception;

import ca.ulaval.glo4003.exception.TrekULException;

public class UserInvalidAgeException extends TrekULException {
  public UserInvalidAgeException() {
    super("CREATION_FAILED", "The user is below the minimum age required");
  }
}
