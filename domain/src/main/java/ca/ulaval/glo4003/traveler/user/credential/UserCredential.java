package ca.ulaval.glo4003.traveler.user.credential;

import ca.ulaval.glo4003.traveler.user.exception.UserLoginWrongCredentialException;

public class UserCredential {
  private final String password;
  private final String email;

  public UserCredential(String email, String password) {
    this.email = email;
    this.password = password;
  }

  public void validatePassword(String passwordToCompare) {
    if (!this.password.equals(passwordToCompare)) {
      throw new UserLoginWrongCredentialException();
    }
  }

  public String getEmailAddress() {
    return this.email;
  }

  @Override
  public boolean equals(Object o) {
    boolean equal = false;
    if (o == this) {
      equal = true;
    } else if (o instanceof UserCredential otherCredentials) {
      equal =
          this.password.equals(otherCredentials.password)
              && this.email.equals(otherCredentials.email);
    }
    return equal;
  }
}
