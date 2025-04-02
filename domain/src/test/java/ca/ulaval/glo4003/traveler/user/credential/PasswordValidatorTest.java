package ca.ulaval.glo4003.traveler.user.credential;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ca.ulaval.glo4003.traveler.user.exception.UserCreationInvalidPasswordException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PasswordValidatorTest {

  private static final int MINIMUM_CHARACTER_ALLOWED = 10;

  private PasswordValidator passwordValidator;

  @BeforeEach
  void setUp() {
    this.passwordValidator = new PasswordValidator(MINIMUM_CHARACTER_ALLOWED);
  }

  @Test
  void givenValidPassword_whenValidatingPassword_thenNoExceptionThrown() {
    String anyValidPassword = "Password123456!";

    assertDoesNotThrow(() -> this.passwordValidator.validate(anyValidPassword));
  }

  @Test
  void givenInvalidLengthPassword_whenValidatingPassword_thenInvalidPasswordExceptionThrown() {
    String anyValidPassword = "password";

    assertThrows(
        UserCreationInvalidPasswordException.class,
        () -> this.passwordValidator.validate(anyValidPassword));
  }

  @Test
  void
      givenPasswordWithoutSpecialCharacter_whenValidatingPassword_thenInvalidPasswordExceptionThrown() {
    String anyValidPassword = "password123456";

    assertThrows(
        UserCreationInvalidPasswordException.class,
        () -> this.passwordValidator.validate(anyValidPassword));
  }

  @Test
  void givenPasswordWithoutCaps_whenValidatingPassword_thenInvalidPasswordExceptionThrown() {
    String anyValidPassword = "password123456!";

    assertThrows(
        UserCreationInvalidPasswordException.class,
        () -> this.passwordValidator.validate(anyValidPassword));
  }
}
