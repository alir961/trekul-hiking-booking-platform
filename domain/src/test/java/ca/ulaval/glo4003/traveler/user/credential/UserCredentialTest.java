package ca.ulaval.glo4003.traveler.user.credential;

import static org.junit.jupiter.api.Assertions.*;

import ca.ulaval.glo4003.traveler.user.exception.UserLoginWrongCredentialException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

public class UserCredentialTest {
  private static final String EMAIL = "hiker@ulaval.ca";
  private static final String PASSWORD = "Tr3k!summit";

  @Test
  void givenTheRightPassword_whenValidating_thenNothingIsThrown() {
    UserCredential credential = new UserCredential(EMAIL, PASSWORD);

    assertDoesNotThrow(() -> credential.validatePassword(PASSWORD));
  }

  @Test
  void givenAWrongOrMissingPassword_whenValidating_thenWrongCredentialIsThrown() {
    UserCredential credential = new UserCredential(EMAIL, PASSWORD);

    assertThrows(
        UserLoginWrongCredentialException.class, () -> credential.validatePassword("tr3k!summit"));
    assertThrows(UserLoginWrongCredentialException.class, () -> credential.validatePassword(null));
  }

  @Test
  void whenCreatingCredential_thenThePasswordIsNotKeptInClearText() throws Exception {
    UserCredential credential = new UserCredential(EMAIL, PASSWORD);

    for (Field field : UserCredential.class.getDeclaredFields()) {
      field.setAccessible(true);
      Object value = field.get(credential);
      assertNotEquals(PASSWORD, value);
      if (value instanceof byte[] bytes) {
        assertFalse(Arrays.equals(PASSWORD.getBytes(StandardCharsets.UTF_8), bytes));
      }
    }
  }

  @Test
  void givenTheSamePasswordTwice_whenCreatingCredentials_thenTheyAreSaltedDifferently()
      throws Exception {
    Field hash = UserCredential.class.getDeclaredField("passwordHash");
    hash.setAccessible(true);

    byte[] first = (byte[]) hash.get(new UserCredential(EMAIL, PASSWORD));
    byte[] second = (byte[]) hash.get(new UserCredential(EMAIL, PASSWORD));

    assertFalse(Arrays.equals(first, second));
  }
}
