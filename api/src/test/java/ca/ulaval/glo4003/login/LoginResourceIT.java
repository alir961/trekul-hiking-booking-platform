package ca.ulaval.glo4003.login;

import static jakarta.ws.rs.client.ClientBuilder.newClient;
import static jakarta.ws.rs.client.Entity.json;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static jakarta.ws.rs.core.Response.Status.OK;
import static jakarta.ws.rs.core.Response.Status.UNAUTHORIZED;
import static java.time.Instant.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4003.ApplicationMocker;
import ca.ulaval.glo4003.exception.TrekULAuthenticationExceptionMapper;
import ca.ulaval.glo4003.login.assembler.LoginResponseAssembler;
import ca.ulaval.glo4003.login.request.LoginRequest;
import ca.ulaval.glo4003.login.response.LoginResponse;
import ca.ulaval.glo4003.token.AuthToken;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.user.exception.UserLoginWrongCredentialException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import java.time.Duration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoginResourceIT {

  private static final ApplicationMocker APP = new ApplicationMocker();
  private static final String EMAIL = "user@example.com";
  private static final String PASSWORD = "longPassword!1234";
  private static final TravelerID USER_ID = new TravelerID("userId");
  private static final Duration ANY_EXPIRATION_LIMIT = Duration.ofSeconds(8887);
  private static final AuthToken TOKEN =
      new AuthToken("token", USER_ID, now(), ANY_EXPIRATION_LIMIT);
  private static final LoginResponse LOGIN_RESPONSE = new LoginResponse(TOKEN.getValue());
  private static final String INVALID_PASSWORD = "password";

  private LoginService loginService;
  private LoginResponseAssembler loginResponseAssembler;

  private Client client;

  @BeforeEach
  void setUp() throws Exception {
    this.loginService = mock(LoginService.class);
    this.loginResponseAssembler = mock(LoginResponseAssembler.class);

    LoginResource loginResource =
        new LoginResourceImpl(this.loginService, this.loginResponseAssembler);

    APP.startServer(new TrekULAuthenticationExceptionMapper(), loginResource);
    this.client = newClient();
  }

  @AfterEach
  void tearDown() throws Exception {
    APP.stopServer();
    this.client.close();
  }

  @Test
  void whenLogin_thenSuccess() {
    // GIVEN
    WebTarget target = this.client.target("http://localhost:8080/api/trekul/login");
    LoginRequest body = new LoginRequest(EMAIL, PASSWORD);

    when(this.loginService.loginWithEmailAndPassword(EMAIL, PASSWORD)).thenReturn(TOKEN);
    when(this.loginResponseAssembler.assemble(TOKEN)).thenReturn(LOGIN_RESPONSE);

    // WHEN
    Response response = target.request(APPLICATION_JSON_TYPE).post(json(body));

    // THEN
    assertEquals(OK.getStatusCode(), response.getStatus());

    LoginResponse loginResponse = response.readEntity(LoginResponse.class);
    assertEquals(TOKEN.getValue(), loginResponse.token());
  }

  @Test
  void whenLogin_thenUnauthorized() {
    // GIVEN
    WebTarget target = this.client.target("http://localhost:8080/api/trekul/login");
    LoginRequest body = new LoginRequest(EMAIL, INVALID_PASSWORD);

    when(this.loginService.loginWithEmailAndPassword(EMAIL, INVALID_PASSWORD))
        .thenThrow(UserLoginWrongCredentialException.class);

    // WHEN
    Response response = target.request(APPLICATION_JSON_TYPE).post(json(body));

    // THEN
    assertEquals(UNAUTHORIZED.getStatusCode(), response.getStatus());
  }
}
