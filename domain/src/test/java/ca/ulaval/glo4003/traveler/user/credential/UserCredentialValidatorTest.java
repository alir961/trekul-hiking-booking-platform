package ca.ulaval.glo4003.traveler.user.credential;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserCredentialValidatorTest {
  private UserCredentialValidator credentialValidator;
  private PasswordValidator passwordValidatorMock;
  private EmailValidator emailValidatorMock;

  @BeforeEach
  void setUp() {
    this.passwordValidatorMock = mock(PasswordValidator.class);
    this.emailValidatorMock = mock(EmailValidator.class);
    this.credentialValidator =
        new UserCredentialValidator(this.emailValidatorMock, this.passwordValidatorMock);
  }

  @Test
  void whenValidate_thenAllValidatorsAreCalled() {
    String anyEmail = "test@example.com";
    String anyPassword = "Test@123";

    this.credentialValidator.validate(anyEmail, anyPassword);

    verify(this.emailValidatorMock).validate(anyEmail);
    verify(this.passwordValidatorMock).validate(anyPassword);
  }
}
