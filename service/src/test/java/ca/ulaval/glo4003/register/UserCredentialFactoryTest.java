package ca.ulaval.glo4003.register;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ca.ulaval.glo4003.traveler.user.credential.UserCredential;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserCredentialFactoryTest {
  private static final String ANY_PASSWORD = "anyPassword";
  private static final String ANY_EMAIL = "ANY@EMAIL'COM";
  private static final UserCredential ANY_USER_CREDENTIAL =
      new UserCredential(ANY_EMAIL, ANY_PASSWORD);
  private UserCredentialFactory userCredentialFactory;

  @BeforeEach
  void setUp() {
    this.userCredentialFactory = new UserCredentialFactory();
  }

  @Test
  void whenCreatingCredential_thenUserCredentialWithCorrectFieldsIsReturned() {
    UserCredential createdUserCredential =
        this.userCredentialFactory.create(ANY_EMAIL, ANY_PASSWORD);

    assertEquals(ANY_USER_CREDENTIAL, createdUserCredential);
    assertEquals(ANY_EMAIL, createdUserCredential.getEmailAddress());
    assertDoesNotThrow(() -> createdUserCredential.validatePassword(ANY_PASSWORD));
  }
}
