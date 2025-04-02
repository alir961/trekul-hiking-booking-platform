package ca.ulaval.glo4003.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.user.User;
import ca.ulaval.glo4003.traveler.user.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public abstract class UserRepositoryIT {
  private static final String AN_EXISTING_USER_EMAIL = "ANY@EMAIL'COM";
  private static final String A_NON_EXISTING_USER_EMAIL = "ANY OTHER @EMAIL'COM";
  private static final TravelerID AN_EXISTING_USER_ID = new TravelerID("ANY_ID");
  private static final TravelerID A_NON_EXISTING_USER_ID = new TravelerID("Any other id");
  private static final User AN_EXISTING_USER = mock(User.class);
  private static final User A_NON_EXISTING_USER = mock(User.class);

  protected UserRepository userRepository;

  @BeforeAll
  public static void setAll() {
    when(AN_EXISTING_USER.getId()).thenReturn(AN_EXISTING_USER_ID);
    when(AN_EXISTING_USER.getEmail()).thenReturn(AN_EXISTING_USER_EMAIL);
    when(AN_EXISTING_USER.copy()).thenReturn(AN_EXISTING_USER);
    when(A_NON_EXISTING_USER.getId()).thenReturn(A_NON_EXISTING_USER_ID);
    when(A_NON_EXISTING_USER.getEmail()).thenReturn(A_NON_EXISTING_USER_EMAIL);
    when(A_NON_EXISTING_USER.copy()).thenReturn(A_NON_EXISTING_USER);
  }

  @BeforeEach
  public void setUp() {
    this.userRepository = this.createRepository();
    this.userRepository.save(AN_EXISTING_USER);
  }

  protected abstract UserRepository createRepository();

  @Test
  void givenUserInRepository_whenGettingByEmail_thenGetExistingUser() {
    Optional<User> foundUser = this.userRepository.getByEmail(AN_EXISTING_USER_EMAIL);

    assertTrue(foundUser.isPresent());
    assertEquals(AN_EXISTING_USER, foundUser.get());
  }

  @Test
  void givenUserInRepository_whenGettingByID_thenGetExistingUser() {
    Optional<User> foundUser = this.userRepository.getById(AN_EXISTING_USER_ID);

    assertTrue(foundUser.isPresent());
    assertEquals(AN_EXISTING_USER, foundUser.get());
  }

  @Test
  void givenUserInRepository_whenRemovingUser_thenUserIsRemoved() {
    this.userRepository.remove(AN_EXISTING_USER);

    Optional<User> foundUserById = this.userRepository.getById(AN_EXISTING_USER_ID);
    Optional<User> foundUserByEmail = this.userRepository.getByEmail(AN_EXISTING_USER_EMAIL);

    assertTrue(foundUserById.isEmpty());
    assertTrue(foundUserByEmail.isEmpty());
  }

  @Test
  void givenUserNotInRepository_whenAddingUser_thenCanGetUser() {
    this.userRepository.save(A_NON_EXISTING_USER);

    Optional<User> foundUserById = this.userRepository.getById(A_NON_EXISTING_USER_ID);
    Optional<User> foundUserByEmail = this.userRepository.getByEmail(A_NON_EXISTING_USER_EMAIL);

    assertTrue(foundUserById.isPresent());
    assertEquals(A_NON_EXISTING_USER, foundUserById.get());
    assertTrue(foundUserByEmail.isPresent());
    assertEquals(A_NON_EXISTING_USER, foundUserByEmail.get());
  }

  @Test
  void givenUserNotInRepository_whenGettingUserByID_thenUserReturnedNotPresent() {
    Optional<User> foundUserById = this.userRepository.getById(A_NON_EXISTING_USER_ID);

    assertTrue(foundUserById.isEmpty());
  }

  @Test
  void givenUserNotInRepository_whenGettingUserByEmail_thenUserReturnedNotPresent() {
    Optional<User> foundUserByEmail = this.userRepository.getByEmail(A_NON_EXISTING_USER_EMAIL);

    assertTrue(foundUserByEmail.isEmpty());
  }
}
