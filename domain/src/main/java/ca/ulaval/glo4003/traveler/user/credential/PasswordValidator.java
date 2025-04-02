package ca.ulaval.glo4003.traveler.user.credential;

import ca.ulaval.glo4003.traveler.user.exception.UserCreationInvalidPasswordException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator {
  private static final Pattern SPECIAL_CHARACTER_PATTERN =
      Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]", Pattern.CASE_INSENSITIVE);
  private static final Pattern CAPS_PATTERN = Pattern.compile("[A-Z]");
  private static final Pattern NUMBER_PATTERN = Pattern.compile("[0-9]");

  private final int minimumCharacter;

  public PasswordValidator(int minimumCharacter) {
    this.minimumCharacter = minimumCharacter;
  }

  public void validate(String password) {
    this.validateMinimumCharacter(password);
    this.validateSpecialCharacter(password);
    this.validateCaps(password);
    this.validateNumberCharacter(password);
  }

  private void validateMinimumCharacter(String password) {
    if (password.length() < this.minimumCharacter) {
      throw new UserCreationInvalidPasswordException("Must be at least 10 characters long");
    }
  }

  private void validateCaps(String password) {
    Matcher matcher = CAPS_PATTERN.matcher(password);
    if (!matcher.find()) {
      throw new UserCreationInvalidPasswordException("Must have at least one capital letter");
    }
  }

  private void validateSpecialCharacter(String password) {
    Matcher matcher = SPECIAL_CHARACTER_PATTERN.matcher(password);
    if (!matcher.find()) {
      throw new UserCreationInvalidPasswordException("Must have at least one special character");
    }
  }

  private void validateNumberCharacter(String password) {
    Matcher matcher = NUMBER_PATTERN.matcher(password);
    if (!matcher.find()) {
      throw new UserCreationInvalidPasswordException("Must have at least one number");
    }
  }
}
