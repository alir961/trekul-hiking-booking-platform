package ca.ulaval.glo4003.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.profile.ProfileService;
import ca.ulaval.glo4003.register.RegisterService;
import ca.ulaval.glo4003.register.dto.UserDto;
import ca.ulaval.glo4003.reservation.CancelReservationService;
import ca.ulaval.glo4003.traveler.PersonName;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.user.Gender;
import ca.ulaval.glo4003.traveler.user.credential.UserCredential;
import ca.ulaval.glo4003.traveler.user.profile.Profile;
import ca.ulaval.glo4003.user.assembler.CreateUserRequestAssembler;
import ca.ulaval.glo4003.user.assembler.ProfileResponseAssembler;
import ca.ulaval.glo4003.user.request.CreateUserRequest;
import ca.ulaval.glo4003.user.response.ProfileResponse;
import jakarta.ws.rs.core.Response;
import java.time.LocalDate;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserResourceImplTest {
  private static final String ANY_FIRSTNAME = "firstname";
  private static final String ANY_LASTNAME = "lastname";
  private static final LocalDate ANY_BIRTH_DATE = LocalDate.now();
  private static final Gender ANY_GENDER = Gender.GENDER_UNSPECIFIED;
  private static final TravelerID ANY_AUTHENTICATED_USER_ID = new TravelerID("123");
  private static final String ANY_IDUL = "123";
  private static final String ANY_EMAIL = "jojo@example.com";
  private static final String ANY_PASSWORD = "pwd123";
  private static final PersonName ANY_NAME = new PersonName("Joey", "The First again");
  private static final int ANY_AGE = 35;
  private static final LocalDate ANY_BIRTH_DAY = LocalDate.now();
  private static final UserCredential ANY_USER_CREDENTIALS =
      new UserCredential(ANY_EMAIL, "any password");
  private static final Profile ANY_PROFILE =
      new Profile(
          ANY_NAME, ANY_BIRTH_DAY, ANY_GENDER, new TravelerID(ANY_IDUL), ANY_USER_CREDENTIALS);
  private static final Money ANY_CREDITS = new Money(45.);
  private static final UserDto ANY_USER_DTO =
      new UserDto(
          ANY_FIRSTNAME,
          ANY_LASTNAME,
          ANY_BIRTH_DATE,
          String.valueOf(ANY_GENDER),
          ANY_IDUL,
          ANY_EMAIL,
          ANY_PASSWORD);
  private static final CreateUserRequest ANY_CREATE_USER_REQUEST =
      new CreateUserRequest(
          ANY_FIRSTNAME,
          ANY_LASTNAME,
          ANY_BIRTH_DATE,
          ANY_GENDER.toString(),
          ANY_IDUL,
          ANY_EMAIL,
          ANY_PASSWORD);
  private RegisterService registerServiceMock;
  private CreateUserRequestAssembler createUserRequestAssemblerMock;
  private ProfileResponseAssembler profileResponseAssemblerMock;
  private ProfileService profileServiceMock;
  private CancelReservationService cancelReservationServiceMock;
  private UserResourceImpl userResource;

  @BeforeEach
  void setUp() {
    this.registerServiceMock = mock(RegisterService.class);
    this.createUserRequestAssemblerMock = mock(CreateUserRequestAssembler.class);
    this.profileResponseAssemblerMock = mock(ProfileResponseAssembler.class);
    this.profileServiceMock = mock(ProfileService.class);
    this.userResource =
        new UserResourceImpl(
            this.registerServiceMock,
            this.createUserRequestAssemblerMock,
            this.profileResponseAssemblerMock,
            this.profileServiceMock);
  }

  @Test
  void whenRegister_thenAssembleToUserDto() {
    this.userResource.createUser(ANY_CREATE_USER_REQUEST);

    verify(this.createUserRequestAssemblerMock).assemble(ANY_CREATE_USER_REQUEST);
  }

  @Test
  void whenRegister_thenRegisterUser() {
    when(this.createUserRequestAssemblerMock.assemble(ANY_CREATE_USER_REQUEST))
        .thenReturn(ANY_USER_DTO);

    this.userResource.createUser(ANY_CREATE_USER_REQUEST);

    verify(this.registerServiceMock).register(ANY_USER_DTO);
  }

  @Test
  void whenRegister_thenReturnHttpOk() {
    when(this.createUserRequestAssemblerMock.assemble(ANY_CREATE_USER_REQUEST))
        .thenReturn(ANY_USER_DTO);

    Response returnedResponse = this.userResource.createUser(ANY_CREATE_USER_REQUEST);

    assertEquals(HttpStatus.OK_200, returnedResponse.getStatus());
  }

  @Test
  void givenAuthenticatedUser_whenReadProfile_thenGetProfileFromAuthenticatedUserId() {
    this.userResource.readProfile(ANY_AUTHENTICATED_USER_ID, ANY_IDUL);

    verify(this.profileServiceMock).getProfile(ANY_IDUL);
  }

  @Test
  void givenAuthenticatedUser_whenReadProfile_thenReturnAssembledProfileResponse() {
    ProfileResponse assembledProfileResponse = this.givenAuthenticatedUserRetrievesProfile();

    Response returnedResponse = this.userResource.readProfile(ANY_AUTHENTICATED_USER_ID, ANY_IDUL);

    assertTrue(returnedResponse.hasEntity());
    assertEquals(assembledProfileResponse, returnedResponse.getEntity());
  }

  @Test
  void givenAuthenticatedUser_whenReadProfile_thenReturnHttpOk() {
    this.givenAuthenticatedUserRetrievesProfile();

    Response returnedResponse = this.userResource.readProfile(ANY_AUTHENTICATED_USER_ID, ANY_IDUL);

    assertEquals(HttpStatus.OK_200, returnedResponse.getStatus());
  }

  private ProfileResponse givenAuthenticatedUserRetrievesProfile() {
    ProfileResponse profileResponse = this.createAnyProfileResponse();
    when(this.profileServiceMock.getProfile(ANY_IDUL)).thenReturn(ANY_PROFILE);
    when(this.profileServiceMock.getUserCredits(ANY_IDUL)).thenReturn(ANY_CREDITS);
    when(this.profileResponseAssemblerMock.assemble(ANY_PROFILE, ANY_CREDITS))
        .thenReturn(profileResponse);
    return profileResponse;
  }

  private ProfileResponse createAnyProfileResponse() {
    return new ProfileResponse(
        ANY_NAME.toString(),
        ANY_BIRTH_DAY,
        ANY_GENDER.toString(),
        ANY_IDUL,
        ANY_EMAIL,
        ANY_AGE,
        ANY_CREDITS.toString());
  }
}
