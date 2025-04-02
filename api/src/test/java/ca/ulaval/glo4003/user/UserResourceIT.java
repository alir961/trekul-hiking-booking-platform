package ca.ulaval.glo4003.user;

import static jakarta.ws.rs.client.ClientBuilder.newClient;
import static jakarta.ws.rs.client.Entity.json;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static jakarta.ws.rs.core.Response.Status.*;
import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.ApplicationMocker;
import ca.ulaval.glo4003.auth.AuthBinder;
import ca.ulaval.glo4003.auth.authentication.AuthenticationRequestFilterMocker;
import ca.ulaval.glo4003.exception.TrekULAuthenticationExceptionMapper;
import ca.ulaval.glo4003.exception.TrekULExceptionMapper;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.profile.ProfileService;
import ca.ulaval.glo4003.register.RegisterService;
import ca.ulaval.glo4003.register.dto.UserDto;
import ca.ulaval.glo4003.traveler.PersonName;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.user.Gender;
import ca.ulaval.glo4003.traveler.user.credential.UserCredential;
import ca.ulaval.glo4003.traveler.user.exception.UserCreationInvalidEmailException;
import ca.ulaval.glo4003.traveler.user.exception.UserCreationInvalidPasswordException;
import ca.ulaval.glo4003.traveler.user.profile.Profile;
import ca.ulaval.glo4003.user.assembler.CreateUserRequestAssembler;
import ca.ulaval.glo4003.user.assembler.ProfileResponseAssembler;
import ca.ulaval.glo4003.user.request.CreateUserRequest;
import ca.ulaval.glo4003.user.response.ProfileResponse;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserResourceIT {

  private static final ApplicationMocker APP = new ApplicationMocker();
  private static final String FIRSTNAME = "John";
  private static final String LASTNAME = "Doe";
  private static final String GENDER = Gender.GENDER_MAN.toString();
  private static final String IDUL = "JODOE1";
  private static final String EMAIL = "user@example.com";
  private static final String PASSWORD = "longPassword!1234";
  private static final UserDto USER_DTO =
      new UserDto(FIRSTNAME, LASTNAME, now(), GENDER, IDUL, EMAIL, PASSWORD);
  private static final String BAD_EMAIL = "gmail.com";
  private static final String BAD_PASSWORD = "password";
  private static final String BASE_USER_RESOURCE_URI = "http://localhost:8080/api/trekul/users";
  private static final Money ANY_CREDITS = new Money(45.);
  private static final ProfileResponse PROFILE_RESPONSE =
      new ProfileResponse(
          "Name", now(), "GENDER_MAN", "IDUL", "email@example.com", 21, ANY_CREDITS.toString());
  private static final LocalDate BIRTH_DATE = LocalDate.now();
  private static final PersonName PERSON_NAME = new PersonName(FIRSTNAME, LASTNAME);
  private static final TravelerID TRAVELER_ID = new TravelerID(IDUL);
  private static final Profile PROFILE =
      new Profile(
          PERSON_NAME,
          BIRTH_DATE,
          Gender.GENDER_MAN,
          TRAVELER_ID,
          new UserCredential(EMAIL, PASSWORD));
  private RegisterService registerServiceMock;
  private CreateUserRequestAssembler createUserRequestAssemblerMock;
  private ProfileResponseAssembler profileResponseAssemblerMock;
  private ProfileService profileServiceMock;
  private AuthenticationRequestFilterMocker authenticationRequestFilterMocker;

  private Client client;

  @BeforeEach
  void setUp() throws Exception {
    this.registerServiceMock = mock(RegisterService.class);
    this.createUserRequestAssemblerMock = mock(CreateUserRequestAssembler.class);
    this.profileResponseAssemblerMock = mock(ProfileResponseAssembler.class);
    this.profileServiceMock = mock(ProfileService.class);
    this.authenticationRequestFilterMocker = new AuthenticationRequestFilterMocker(TRAVELER_ID);

    UserResource userResource =
        new UserResourceImpl(
            this.registerServiceMock,
            this.createUserRequestAssemblerMock,
            this.profileResponseAssemblerMock,
            this.profileServiceMock);

    APP.startServer(
        new TrekULExceptionMapper(),
        new TrekULAuthenticationExceptionMapper(),
        userResource,
        new AuthBinder(),
        this.authenticationRequestFilterMocker);
    this.client = newClient();
  }

  @AfterEach
  void tearDown() throws Exception {
    APP.stopServer();
    this.client.close();
  }

  @Test
  void whenRegister_thenSuccess() {
    // GIVEN
    WebTarget target = this.client.target(BASE_USER_RESOURCE_URI);
    CreateUserRequest body =
        new CreateUserRequest(FIRSTNAME, LASTNAME, now(), GENDER, IDUL, EMAIL, PASSWORD);

    when(this.createUserRequestAssemblerMock.assemble(body)).thenReturn(USER_DTO);
    doNothing().when(this.registerServiceMock).register(USER_DTO);

    // WHEN
    Response response = target.request(APPLICATION_JSON_TYPE).post(json(body));

    // THEN
    assertEquals(OK.getStatusCode(), response.getStatus());
  }

  @Test
  void givenBadEmail_whenRegister_thenBadRequest() {
    // GIVEN
    WebTarget target = this.client.target(BASE_USER_RESOURCE_URI);
    CreateUserRequest body =
        new CreateUserRequest(FIRSTNAME, LASTNAME, now(), GENDER, IDUL, BAD_EMAIL, PASSWORD);

    when(this.createUserRequestAssemblerMock.assemble(body)).thenReturn(USER_DTO);
    doThrow(UserCreationInvalidEmailException.class)
        .when(this.registerServiceMock)
        .register(USER_DTO);

    // WHEN
    Response response = target.request(APPLICATION_JSON_TYPE).post(json(body));

    // THEN
    assertEquals(BAD_REQUEST.getStatusCode(), response.getStatus());
  }

  @Test
  void givenBadPassword_whenRegister_thenBadRequest() {
    // GIVEN
    WebTarget target = this.client.target(BASE_USER_RESOURCE_URI);
    CreateUserRequest body =
        new CreateUserRequest(FIRSTNAME, LASTNAME, now(), GENDER, IDUL, EMAIL, BAD_PASSWORD);

    when(this.createUserRequestAssemblerMock.assemble(body)).thenReturn(USER_DTO);
    doThrow(UserCreationInvalidPasswordException.class)
        .when(this.registerServiceMock)
        .register(USER_DTO);

    // WHEN
    Response response = target.request(APPLICATION_JSON_TYPE).post(json(body));

    // THEN
    assertEquals(BAD_REQUEST.getStatusCode(), response.getStatus());
  }

  @Test
  void whenGetProfile_thenSuccess() {
    // GIVEN
    WebTarget target = this.client.target(String.format("%s/%s", BASE_USER_RESOURCE_URI, IDUL));

    when(this.profileServiceMock.getProfile(IDUL)).thenReturn(PROFILE);
    when(this.profileServiceMock.getUserCredits(IDUL)).thenReturn(ANY_CREDITS);
    when(this.profileResponseAssemblerMock.assemble(PROFILE, ANY_CREDITS))
        .thenReturn(PROFILE_RESPONSE);

    // WHEN
    Response response = target.request(APPLICATION_JSON_TYPE).get();

    // THEN
    assertEquals(OK.getStatusCode(), response.getStatus());

    ProfileResponse profileResponse = response.readEntity(ProfileResponse.class);
    assertEquals(PROFILE_RESPONSE.name(), profileResponse.name());
  }

  @Test
  void whenGetProfile_thenUnauthorized() {
    // GIVEN
    WebTarget target =
        this.client.target(String.format("%s/%s", BASE_USER_RESOURCE_URI, TRAVELER_ID));
    this.authenticationRequestFilterMocker.asUnauthorized();

    // WHEN
    Response response = target.request(APPLICATION_JSON_TYPE).get();

    // THEN
    assertEquals(UNAUTHORIZED.getStatusCode(), response.getStatus());
  }
}
