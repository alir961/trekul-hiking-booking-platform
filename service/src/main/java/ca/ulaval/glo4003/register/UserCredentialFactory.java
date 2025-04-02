package ca.ulaval.glo4003.register;

import ca.ulaval.glo4003.traveler.user.credential.UserCredential;

public class UserCredentialFactory {

  public UserCredential create(String email, String password) {
    return new UserCredential(email, password);
  }
}
