package ca.ulaval.glo4003.traveler.user.profile;

import ca.ulaval.glo4003.traveler.PersonName;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.user.Gender;
import ca.ulaval.glo4003.traveler.user.credential.UserCredential;
import java.time.LocalDate;
import java.time.Period;

public record Profile(
    PersonName name,
    LocalDate birthDate,
    Gender gender,
    TravelerID idul,
    UserCredential credentials) {

  public int calculateAge() {
    LocalDate today = LocalDate.now();
    Period difference = Period.between(this.birthDate, today);
    return difference.getYears();
  }

  public void validatePassword(String password) {
    this.credentials.validatePassword(password);
  }

  public String getEmail() {
    return this.credentials.getEmailAddress();
  }
}
