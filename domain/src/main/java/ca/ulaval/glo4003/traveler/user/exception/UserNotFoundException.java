package ca.ulaval.glo4003.traveler.user.exception;

import ca.ulaval.glo4003.exception.TrekULNotFoundException;

public class UserNotFoundException extends TrekULNotFoundException {
  public UserNotFoundException() {
    super("USER_NOT_FOUND", "User not found");
  }
}
