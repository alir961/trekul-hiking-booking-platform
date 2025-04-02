package ca.ulaval.glo4003.profile;

import static ca.ulaval.glo4003.ListResponseMapper.mapListResponse;
import static ca.ulaval.glo4003.hiking.HikingDifficulty.INTERMEDIATE;
import static ca.ulaval.glo4003.hiking.HikingRegion.GASPESIE;
import static ca.ulaval.glo4003.reservation.ReservationStatus.CONFIRMED;
import static ca.ulaval.glo4003.traveler.user.Gender.GENDER_MAN;
import static jakarta.ws.rs.client.ClientBuilder.newClient;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static jakarta.ws.rs.core.Response.Status.OK;
import static jakarta.ws.rs.core.Response.Status.UNAUTHORIZED;
import static java.time.LocalDate.now;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4003.ApplicationMocker;
import ca.ulaval.glo4003.auth.AuthBinder;
import ca.ulaval.glo4003.auth.authentication.AuthenticationRequestFilterMocker;
import ca.ulaval.glo4003.distance.Distance;
import ca.ulaval.glo4003.exception.TrekULAuthenticationExceptionMapper;
import ca.ulaval.glo4003.group.Group;
import ca.ulaval.glo4003.hiking.Hiking;
import ca.ulaval.glo4003.hiking.HikingLocation;
import ca.ulaval.glo4003.hiking.response.HikingResponse;
import ca.ulaval.glo4003.hosting.HostingID;
import ca.ulaval.glo4003.hosting.reservation.HostingReservation;
import ca.ulaval.glo4003.hosting.response.HostingResponse;
import ca.ulaval.glo4003.invoice.InvoiceID;
import ca.ulaval.glo4003.invoice.dto.InvoiceDto;
import ca.ulaval.glo4003.invoice.response.InvoiceShortResponse;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.profile.assembler.ProfileTripResponseAssembler;
import ca.ulaval.glo4003.profile.dto.ProfileTripDto;
import ca.ulaval.glo4003.profile.response.ProfileTripResponse;
import ca.ulaval.glo4003.reservation.ReservationID;
import ca.ulaval.glo4003.reservation.ReservationStatus;
import ca.ulaval.glo4003.reservation.dto.CreatedReservationDto;
import ca.ulaval.glo4003.reservation.response.ReservationStatusResponse;
import ca.ulaval.glo4003.transport.TransportType;
import ca.ulaval.glo4003.traveler.PersonName;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.user.credential.UserCredential;
import ca.ulaval.glo4003.traveler.user.profile.Profile;
import ca.ulaval.glo4003.trip.TripID;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.*;

class ProfileResourceIT {

  private static final ApplicationMocker APP = new ApplicationMocker();
  // getProfile IT
  private static final TravelerID USER_ID = new TravelerID("userId");
  private static final PersonName ANY_PROFILE_FULLNAME = new PersonName("first", "last");
  private static final String ANY_EMAIL = "email@example.com";
  private static final UserCredential ANY_USER_CREDENTIAL =
      new UserCredential(ANY_EMAIL, "anything");
  private static final ReservationID ANY_RESERVATION_ID = ReservationID.generate();
  private static final Profile PROFILE =
      new Profile(ANY_PROFILE_FULLNAME, now(), GENDER_MAN, USER_ID, ANY_USER_CREDENTIAL);

  // searchProfileTrips IT
  private static final Hiking HIKING =
      new Hiking(new HikingLocation(GASPESIE, "Name"), INTERMEDIATE, new Distance("10"));
  private static final TripID TRIP_ID = TripID.generate();
  private static final InvoiceID INVOICE_ID = InvoiceID.generate();
  private static final Map<String, Money> MONEYS = Map.of("default", new Money("100"));
  private static final Money MONEY = new Money("278");
  private static final InvoiceDto INVOICE_DTO =
      new InvoiceDto(INVOICE_ID, MONEYS, MONEYS, new Money("200"), new Money("200"), emptyList());
  private static final Group GROUP = new Group(USER_ID);
  private static final HostingReservation HOSTING_RESERVATION =
      new HostingReservation(
          new HostingID(124568), "HostingName", Map.of(new Money("200"), new Money("200")));
  private static final ReservationStatus RESERVATION_STATUS = ReservationStatus.CONFIRMED;
  private static final TransportType RESERVATION_TRANSPORT_TYPE = TransportType.CARPOOL;
  private static final CreatedReservationDto CREATED_RESERVATION_DTO =
      new CreatedReservationDto(
          ANY_RESERVATION_ID,
          INVOICE_DTO,
          GROUP,
          MONEY,
          ZonedDateTime.now().plusMinutes(150),
          RESERVATION_STATUS,
          RESERVATION_TRANSPORT_TYPE);
  private static final ProfileTripDto PROFILE_TRIP_DTO =
      new ProfileTripDto(
          HIKING, TRIP_ID, now(), INVOICE_DTO, CREATED_RESERVATION_DTO, HOSTING_RESERVATION);
  private static final List<ProfileTripDto> PROFILE_TRIPS = List.of(PROFILE_TRIP_DTO);
  private static final HikingResponse HIKING_RESPONSE =
      new HikingResponse(GASPESIE.toString(), INTERMEDIATE.toString(), "10km");
  private static final InvoiceShortResponse INVOICE_SHORT_RESPONSE =
      new InvoiceShortResponse(INVOICE_ID.toString(), "200", "200");
  private static final ReservationStatusResponse RESERVATION_RESPONSE =
      mock(ReservationStatusResponse.class);
  private static final HostingResponse HOSTING_RESPONSE =
      new HostingResponse(
          HOSTING_RESERVATION.getHostingID().toString(),
          HOSTING_RESERVATION.getHostingName(),
          HOSTING_RESERVATION.getPriceRange().toString());
  private static final ProfileTripResponse PROFILE_TRIP_RESPONSE =
      new ProfileTripResponse(
          TRIP_ID.toString(),
          HIKING_RESPONSE,
          now().toString(),
          INVOICE_SHORT_RESPONSE,
          RESERVATION_RESPONSE,
          HOSTING_RESPONSE);

  private ProfileService profileService;
  private ProfileTripsService profileTripsService;
  private ProfileTripResponseAssembler profileTripResponseAssembler;
  private AuthenticationRequestFilterMocker authenticationRequestFilterMocker;

  private Client client;

  @BeforeEach
  void setUp() throws Exception {
    this.profileService = mock(ProfileService.class);
    this.profileTripsService = mock(ProfileTripsService.class);
    this.profileTripResponseAssembler = mock(ProfileTripResponseAssembler.class);
    this.authenticationRequestFilterMocker = new AuthenticationRequestFilterMocker(USER_ID);

    ProfileResource profileResource =
        new ProfileResourceImpl(this.profileTripsService, this.profileTripResponseAssembler);

    APP.startServer(
        new TrekULAuthenticationExceptionMapper(),
        profileResource,
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
  void whenSearchProfileTrips_thenSuccess() {
    // GIVEN
    WebTarget target =
        this.client.target("http://localhost:8080/api/trekul/profile/trips?status=confirmed");

    when(this.profileService.getProfile(USER_ID.toString())).thenReturn(PROFILE);
    when(this.profileTripResponseAssembler.assemble(PROFILE_TRIPS))
        .thenReturn(List.of(PROFILE_TRIP_RESPONSE));
    when(this.profileTripsService.searchTrips(USER_ID, CONFIRMED)).thenReturn(PROFILE_TRIPS);

    // WHEN
    Response response = target.request(APPLICATION_JSON_TYPE).get();

    // THEN
    List<HashMap<String, Object>> trips = mapListResponse(response);
    HashMap<String, Object> trip = trips.getFirst();

    assertEquals(OK.getStatusCode(), response.getStatus());
    assertEquals(1, trips.size());
    assertEquals(TRIP_ID.toString(), trip.get("tripId").toString());
  }

  @Test
  void whenSearchProfileTrips_thenUnauthorized() {
    // GIVEN
    WebTarget target =
        this.client.target("http://localhost:8080/api/trekul/profile/trips?status=confirmed");
    this.authenticationRequestFilterMocker.asUnauthorized();

    // WHEN
    Response response = target.request(APPLICATION_JSON_TYPE).get();

    // THEN
    assertEquals(UNAUTHORIZED.getStatusCode(), response.getStatus());
  }
}
