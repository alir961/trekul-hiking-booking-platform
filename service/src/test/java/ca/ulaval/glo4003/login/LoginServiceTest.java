package ca.ulaval.glo4003.login;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.token.AuthToken;
import ca.ulaval.glo4003.token.AuthTokenFactory;
import ca.ulaval.glo4003.token.AuthTokenRepository;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.user.User;
import ca.ulaval.glo4003.traveler.user.UserRepository;
import ca.ulaval.glo4003.traveler.user.exception.UserLoginWrongCredentialException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

public class LoginServiceTest {
  private static final String ANY_EMAIL = "test@test.com";
  private static final String ANY_PASSWORD = "Password";
  private static final TravelerID ANY_TRAVELER_ID = new TravelerID("3ghg5h");
  private static final AuthToken ANY_NEW_AUTH_TOKEN = mock(AuthToken.class);
  private User anyFoundUserMock;
  private AuthTokenRepository authTokenRepoMock;
  private AuthTokenFactory authTokenFactoryMock;
  private UserRepository userRepositoryMock;
  private LoginService loginService;

  @BeforeEach
  public void setUp() {
    this.authTokenRepoMock = mock(AuthTokenRepository.class);
    this.authTokenFactoryMock = mock(AuthTokenFactory.class);
    this.userRepositoryMock = mock(UserRepository.class);
    this.loginService =
        new LoginService(
            this.userRepositoryMock, this.authTokenRepoMock, this.authTokenFactoryMock);

    this.anyFoundUserMock = mock(User.class);
    when(this.anyFoundUserMock.getEmail()).thenReturn(ANY_EMAIL);
    when(this.anyFoundUserMock.getId()).thenReturn(ANY_TRAVELER_ID);
    when(this.userRepositoryMock.getByEmail(ANY_EMAIL))
        .thenReturn(Optional.of(this.anyFoundUserMock));
  }

  @Test
  void givenUserDoesNotExist_whenLogin_thenThrowUserLoginWrongCredentialException() {
    Optional<User> userNotFound = Optional.empty();

    when(this.userRepositoryMock.getByEmail(ANY_EMAIL)).thenReturn(userNotFound);

    assertThrows(
        UserLoginWrongCredentialException.class,
        () -> this.loginService.loginWithEmailAndPassword(ANY_EMAIL, ANY_PASSWORD));
  }

  @Test
  void givenUserExists_whenLogin_thenValidateUserPassword() {
    this.loginService.loginWithEmailAndPassword(ANY_EMAIL, ANY_PASSWORD);

    verify(this.anyFoundUserMock).validatePassword(ANY_PASSWORD);
  }

  @Test
  void givenUserExistsAndLoginIsValid_whenLogin_thenReturnTokenFromFactory() {
    when(this.authTokenFactoryMock.createToken(ANY_TRAVELER_ID)).thenReturn(ANY_NEW_AUTH_TOKEN);

    AuthToken token = this.loginService.loginWithEmailAndPassword(ANY_EMAIL, ANY_PASSWORD);

    assertSame(ANY_NEW_AUTH_TOKEN, token);
  }

  @Test
  void givenUserExistsAndLoginIsValid_whenLogin_thenSaveTokenAfterValidation() {
    when(this.authTokenFactoryMock.createToken(ANY_TRAVELER_ID)).thenReturn(ANY_NEW_AUTH_TOKEN);

    this.loginService.loginWithEmailAndPassword(ANY_EMAIL, ANY_PASSWORD);

    InOrder order = inOrder(this.anyFoundUserMock, this.authTokenRepoMock);
    order.verify(this.anyFoundUserMock).validatePassword(ANY_PASSWORD);
    order.verify(this.authTokenRepoMock).save(ANY_NEW_AUTH_TOKEN);
  }
}
