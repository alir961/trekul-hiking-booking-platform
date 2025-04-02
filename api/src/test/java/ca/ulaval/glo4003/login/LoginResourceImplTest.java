package ca.ulaval.glo4003.login;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.login.assembler.LoginResponseAssembler;
import ca.ulaval.glo4003.login.request.LoginRequest;
import ca.ulaval.glo4003.login.response.LoginResponse;
import ca.ulaval.glo4003.token.AuthToken;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LoginResourceImplTest {
  private static final String ANY_EMAIL = "email";
  private static final String ANY_PASSWORD = "password";
  private static final String ANY_TOKEN_AS_STR = "token";
  private static final LoginRequest ANY_LOGIN_REQUEST = new LoginRequest(ANY_EMAIL, ANY_PASSWORD);
  private static final LoginResponse ANY_LOGIN_RESPONSE = new LoginResponse(ANY_TOKEN_AS_STR);
  private LoginService loginServiceMock;
  private LoginResponseAssembler loginResponseAssemblerMock;
  private LoginResourceImpl loginResource;

  @BeforeEach
  void setUp() {
    this.loginServiceMock = mock(LoginService.class);
    this.loginResponseAssemblerMock = mock(LoginResponseAssembler.class);
    this.loginResource =
        new LoginResourceImpl(this.loginServiceMock, this.loginResponseAssemblerMock);
  }

  @Test
  void whenLogin_thenLoginWithEmailAndPassword() {
    this.loginResource.login(ANY_LOGIN_REQUEST);

    verify(this.loginServiceMock)
        .loginWithEmailAndPassword(ANY_LOGIN_REQUEST.email(), ANY_LOGIN_REQUEST.password());
  }

  @Test
  void whenLogin_thenReturnAssembledLoginResponse() {
    AuthToken authTokenMock = mock(AuthToken.class);
    when(this.loginServiceMock.loginWithEmailAndPassword(
            ANY_LOGIN_REQUEST.email(), ANY_LOGIN_REQUEST.password()))
        .thenReturn(authTokenMock);
    when(this.loginResponseAssemblerMock.assemble(authTokenMock)).thenReturn(ANY_LOGIN_RESPONSE);

    Response returnedResponse = this.loginResource.login(ANY_LOGIN_REQUEST);

    assertTrue(returnedResponse.hasEntity());
    assertEquals(ANY_LOGIN_RESPONSE, returnedResponse.getEntity());
  }
}
