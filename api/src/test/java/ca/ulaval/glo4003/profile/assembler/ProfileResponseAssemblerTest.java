package ca.ulaval.glo4003.profile.assembler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.traveler.PersonName;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.user.Gender;
import ca.ulaval.glo4003.traveler.user.credential.UserCredential;
import ca.ulaval.glo4003.traveler.user.profile.Profile;
import ca.ulaval.glo4003.user.assembler.ProfileResponseAssembler;
import ca.ulaval.glo4003.user.response.ProfileResponse;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProfileResponseAssemblerTest {
  private static final String ANY_FIRSTNAME = "Joey";
  private static final String ANY_LASTNAME = "The First";
  private static final PersonName ANY_FULLNAME = new PersonName(ANY_FIRSTNAME, ANY_LASTNAME);
  private static final int ANY_AGE = 35;
  private static final String ANY_IDUL = "BIVOO456";
  private static final String ANY_EMAIL = "Jojo123@example.com";
  private static final LocalDate ANY_BIRTH_DAY = LocalDate.now().minusYears(ANY_AGE);
  private static final Gender ANY_GENDER = Gender.GENDER_UNSPECIFIED;
  private static final UserCredential ANY_CREDENTIALS =
      new UserCredential(ANY_EMAIL, "anything for pass");
  private static final Money ANY_CREDITS = new Money(45.);
  private static final Profile ANY_PROFILE =
      new Profile(
          ANY_FULLNAME, ANY_BIRTH_DAY, ANY_GENDER, new TravelerID(ANY_IDUL), ANY_CREDENTIALS);
  private ProfileResponseAssembler profileResponseAssembler;

  @BeforeEach
  void setUp() {
    this.profileResponseAssembler = new ProfileResponseAssembler();
  }

  @Test
  void whenAssemblingProfileResponse_thenProfileResponseAssembleCorrectly() {
    ProfileResponse assembledProfileResponse =
        this.profileResponseAssembler.assemble(ANY_PROFILE, ANY_CREDITS);

    this.assertProfileResponseAssembleCorrectly(assembledProfileResponse);
  }

  private void assertProfileResponseAssembleCorrectly(ProfileResponse assembledResponse) {
    assertEquals(ANY_AGE, assembledResponse.age());
    assertEquals(ANY_EMAIL, assembledResponse.email());
    assertEquals(ANY_BIRTH_DAY, assembledResponse.birthDay());
    assertEquals(ANY_FULLNAME.toString(), assembledResponse.name());
    assertEquals(ANY_IDUL, assembledResponse.idul());
    assertEquals(ANY_GENDER.toString(), assembledResponse.gender());
    assertEquals(ANY_CREDITS.toString(), assembledResponse.credits());
  }
}
