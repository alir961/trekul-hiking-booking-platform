package ca.ulaval.glo4003.register;

import static ca.ulaval.glo4003.traveler.user.Gender.GENDER_UNSPECIFIED;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.register.dto.UserDto;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.user.Gender;
import ca.ulaval.glo4003.traveler.user.User;
import ca.ulaval.glo4003.traveler.user.UserRepository;
import ca.ulaval.glo4003.traveler.user.credential.UserCredentialValidator;
import ca.ulaval.glo4003.traveler.user.exception.UserAlreadyExistsException;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public class RegisterServiceTest {
  private static final String ANY_IDUL = "JOBOI179";
  private static final String ANY_USER_FIRSTNAME = "Joey";
  private static final String ANY_USER_LASTNAME = "Lasty name";
  private static final String ANY_PASSWORD = "ANY_PASSWORD";
  private static final String ANY_EMAIL = "ANY@EMAIL'COM";
  private static final TravelerID ANY_USER_ID = new TravelerID(ANY_IDUL);
  private static final Gender ANY_GENDER = GENDER_UNSPECIFIED;
  private static final LocalDate ANY_BIRTH_DATE = LocalDate.now();
  private static final UserDto ANY_USER_DTO =
      new UserDto(
          ANY_USER_FIRSTNAME,
          ANY_USER_LASTNAME,
          ANY_BIRTH_DATE,
          String.valueOf(ANY_GENDER),
          ANY_IDUL,
          ANY_EMAIL,
          ANY_PASSWORD);

  private final UserFactory userFactoryMock = mock(UserFactory.class);
  private final UserRepository userRepositoryMock = mock(UserRepository.class);
  private final UserCredentialValidator userCredentialValidatorMock =
      mock(UserCredentialValidator.class);
  private final RegisterService registerService =
      new RegisterService(
          this.userFactoryMock, this.userRepositoryMock, this.userCredentialValidatorMock);

  @Test
  void givenUserAlreadyExistsInRepository_whenRegister_thenThrowUserAlreadyExistsException() {
    this.givenUserAlreadyExists();

    assertThrows(
        UserAlreadyExistsException.class, () -> this.registerService.register(ANY_USER_DTO));
  }

  @Test
  void givenUserDoesNotExistInRepository_whenRegister_thenUserIsCreated() {
    User createdUserMock = mock(User.class);
    when(this.userFactoryMock.create(ANY_USER_DTO)).thenReturn(createdUserMock);

    this.registerService.register(ANY_USER_DTO);

    verify(this.userFactoryMock).create(ANY_USER_DTO);
  }

  @Test
  void givenUserDoesNotExistInRepository_whenRegister_thenUserIsRegistered() {
    User createdUserMock = mock(User.class);
    when(this.userFactoryMock.create(ANY_USER_DTO)).thenReturn(createdUserMock);

    this.registerService.register(ANY_USER_DTO);

    verify(this.userRepositoryMock).save(createdUserMock);
  }

  @Test
  void givenUserDoesNotExistInRepository_whenRegister_thenUserCredentialIsValidated() {
    User createdUserMock = mock(User.class);
    when(this.userFactoryMock.create(ANY_USER_DTO)).thenReturn(createdUserMock);

    this.registerService.register(ANY_USER_DTO);

    verify(this.userCredentialValidatorMock)
        .validate(ANY_USER_DTO.email(), ANY_USER_DTO.password());
  }

  private void givenUserAlreadyExists() {
    Optional<User> presentUser = Optional.of(mock(User.class));

    when(this.userRepositoryMock.getById(ANY_USER_ID)).thenReturn(presentUser);
  }
}
