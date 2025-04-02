package ca.ulaval.glo4003.traveler.user.credential;

import ca.ulaval.glo4003.traveler.user.exception.UserCreationInvalidEmailException;

public class EmailValidator {
  public void validate(String email) {
    String emailRegex =
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$";
    if (!email.matches(emailRegex)) {
      throw new UserCreationInvalidEmailException();
    }
  }
}
