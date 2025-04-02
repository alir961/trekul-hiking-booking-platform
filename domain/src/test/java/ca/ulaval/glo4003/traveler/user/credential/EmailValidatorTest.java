package ca.ulaval.glo4003.traveler.user.credential;

import static org.junit.jupiter.api.Assertions.*;

import ca.ulaval.glo4003.traveler.user.exception.UserCreationInvalidEmailException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class EmailValidatorTest {

  private EmailValidator emailValidator = new EmailValidator();

  @BeforeEach
  void setUp() {
    this.emailValidator = new EmailValidator();
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "invalid-email",
        "invalid@com",
        "@example.com",
        "name@.com",
        "user@domain",
        "user@domain..com",
        "missing@domain.",
        "missing@.com",
        "invalid@domain,com",
        "invalid@@example.com",
        "email@123.123.123.123"
      })
  void givenInvalidEmail_whenValidating_thenThrowUserCreationInvalidEmailException(
      String invalidEmail) {

    assertThrows(
        UserCreationInvalidEmailException.class, () -> this.emailValidator.validate(invalidEmail));
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "valid.email@example.com",
        "user.name+tag@domain.co.uk",
        "user_name@domain.com",
        "name@sub.domain.com",
        "user@domain.museum",
        "user@sub-domain.domain.com",
        "firstname.lastname@domain.com",
        "1234567890@domain.com",
        "user@domain.tld"
      })
  void givenValidEmail_whenValidating_thenDoesNotThrowException(String validEmail) {

    assertDoesNotThrow(() -> this.emailValidator.validate(validEmail));
  }
}
