package ca.ulaval.glo4003.traveler.user.profile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import ca.ulaval.glo4003.traveler.PersonName;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.user.Gender;
import ca.ulaval.glo4003.traveler.user.credential.UserCredential;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProfileTest {
  private static final int A_USER_AGE = 49;
  private static final String ANY_USER_FIRSTNAME = "Joey";
  private static final String ANY_USER_LASTNAME = "Any last name";
  private static final PersonName ANY_USER_FULLNAME =
      new PersonName(ANY_USER_FIRSTNAME, ANY_USER_LASTNAME);
  private static final TravelerID ANY_USER_ID = new TravelerID("id");
  private static final Gender ANY_GENDER = Gender.GENDER_UNSPECIFIED;
  private UserCredential anyUserCredentialMock;

  @BeforeEach
  void setUp() {
    this.anyUserCredentialMock = mock(UserCredential.class);
  }

  @Test
  void whenGettingAge_thenYearsAreFromBirthdayTillNow() {
    LocalDate birthday = LocalDate.now().minusYears(A_USER_AGE);
    Profile profileWithKnownAge =
        new Profile(
            ANY_USER_FULLNAME, birthday, ANY_GENDER, ANY_USER_ID, this.anyUserCredentialMock);
    int age = profileWithKnownAge.calculateAge();

    assertEquals(A_USER_AGE, age);
  }
}
