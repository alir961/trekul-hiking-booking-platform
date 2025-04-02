package ca.ulaval.glo4003.register;

import static ca.ulaval.glo4003.traveler.user.Gender.GENDER_UNSPECIFIED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.register.dto.UserDto;
import ca.ulaval.glo4003.traveler.user.Gender;
import ca.ulaval.glo4003.traveler.user.ProfileValidator;
import ca.ulaval.glo4003.traveler.user.User;
import ca.ulaval.glo4003.traveler.user.credential.UserCredential;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

public class UserFactoryTest {
  private static final String ANY_USER_FIRSTNAME = "Joey";
  private static final String ANY_USER_LASTNAME = "Lasty name";
  private static final LocalDate ANY_BIRTH_DATE = LocalDate.now();
  private static final Gender ANY_GENDER = GENDER_UNSPECIFIED;
  private static final String ANY_IDUL = "JOBOI179";
  private static final String ANY_PASSWORD = "ANY_PASSWORD";
  private static final String ANY_EMAIL = "ANY@EMAIL'COM";
  private static final UserDto ANY_USER_DTO =
      new UserDto(
          ANY_USER_FIRSTNAME,
          ANY_USER_LASTNAME,
          ANY_BIRTH_DATE,
          String.valueOf(ANY_GENDER),
          ANY_IDUL,
          ANY_EMAIL,
          ANY_PASSWORD);

  private static final UserCredential ANY_USER_CREDENTIAL =
      new UserCredential(ANY_EMAIL, ANY_PASSWORD);

  private final ProfileValidator profileValidatorMock = mock(ProfileValidator.class);
  private final UserCredentialFactory userCredentialFactoryMock = mock(UserCredentialFactory.class);
  private final UserFactory userFactory =
      new UserFactory(this.profileValidatorMock, this.userCredentialFactoryMock);

  @Test
  void whenCreating_thenUserWithCorrectFieldsIsReturned() {
    when(this.userCredentialFactoryMock.create(ANY_EMAIL, ANY_PASSWORD))
        .thenReturn(ANY_USER_CREDENTIAL);

    User createdUser = this.userFactory.create(ANY_USER_DTO);

    this.assertUserFieldsAreCorrectlyMapped(createdUser);
  }

  @Test
  void whenCreatingUser_thenUserCredentialIsCreated() {
    this.userFactory.create(ANY_USER_DTO);

    verify(this.userCredentialFactoryMock).create(ANY_EMAIL, ANY_PASSWORD);
  }

  private void assertUserFieldsAreCorrectlyMapped(User createdUser) {
    assertEquals(ANY_USER_FIRSTNAME, createdUser.getName().firstname());
    assertEquals(ANY_USER_LASTNAME, createdUser.getName().lastname());
    assertEquals(ANY_IDUL, createdUser.getId().getValue());
  }
}
