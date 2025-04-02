package ca.ulaval.glo4003.traveler.user;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4003.traveler.user.exception.UserInvalidAgeException;
import ca.ulaval.glo4003.traveler.user.profile.Profile;
import org.junit.jupiter.api.Test;

public class ProfileValidatorTest {
  private static final int ANY_MINIMUM_AGE = 18;
  private static final int ANY_MAXIMUM_AGE = 80;
  private static final int ANY_AGE_BELOW_MINIMUM = ANY_MINIMUM_AGE - 1;
  private static final int ANY_AGE_OVER_MAXIMUM = ANY_MAXIMUM_AGE + 1;
  private final ProfileValidator profileValidator =
      new ProfileValidator(ANY_MINIMUM_AGE, ANY_MAXIMUM_AGE);

  @Test
  void givenUserOldEnough_whenValidating_thenNoExceptionsAreThrown() {
    Profile oldEnoughUser = mock(Profile.class);
    when(oldEnoughUser.calculateAge()).thenReturn(ANY_MINIMUM_AGE);

    assertDoesNotThrow(() -> this.profileValidator.validate(oldEnoughUser));
  }

  @Test
  void givenUserTooYoung_whenValidating_thenUserInvalidAgeExceptionIsThrown() {
    Profile tooYoungUser = mock(Profile.class);
    when(tooYoungUser.calculateAge()).thenReturn(ANY_AGE_BELOW_MINIMUM);

    assertThrows(UserInvalidAgeException.class, () -> this.profileValidator.validate(tooYoungUser));
  }

  @Test
  void givenUserTooOld_whenValidating_thenUserInvalidAgeExceptionIsThrown() {
    Profile tooOldUser = mock(Profile.class);
    when(tooOldUser.calculateAge()).thenReturn(ANY_AGE_OVER_MAXIMUM);

    assertThrows(UserInvalidAgeException.class, () -> this.profileValidator.validate(tooOldUser));
  }
}
