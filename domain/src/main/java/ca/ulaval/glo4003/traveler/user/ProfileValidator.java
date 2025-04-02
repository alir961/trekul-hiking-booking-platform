package ca.ulaval.glo4003.traveler.user;

import ca.ulaval.glo4003.traveler.user.exception.UserInvalidAgeException;
import ca.ulaval.glo4003.traveler.user.profile.Profile;

public class ProfileValidator {
  private final int minimumUserAge;
  private final int maximumUserAge;

  public ProfileValidator(int minimumUserAge, int maximumUserAge) {
    this.minimumUserAge = minimumUserAge;
    this.maximumUserAge = maximumUserAge;
  }

  public void validate(Profile profile) {
    int age = profile.calculateAge();
    if (age < this.minimumUserAge || age > this.maximumUserAge) {
      throw new UserInvalidAgeException();
    }
  }
}
