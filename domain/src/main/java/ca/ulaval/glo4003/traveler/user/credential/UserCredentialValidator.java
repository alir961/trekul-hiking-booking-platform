package ca.ulaval.glo4003.traveler.user.credential;

public class UserCredentialValidator {

  EmailValidator emailValidator;
  PasswordValidator passwordValidator;

  public UserCredentialValidator(
      EmailValidator emailValidator, PasswordValidator passwordValidator) {
    this.emailValidator = emailValidator;
    this.passwordValidator = passwordValidator;
  }

  public void validate(String email, String password) {
    this.emailValidator.validate(email);
    this.passwordValidator.validate(password);
  }
}
