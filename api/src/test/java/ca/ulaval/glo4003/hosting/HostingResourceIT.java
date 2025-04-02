package ca.ulaval.glo4003.hosting;

import static ca.ulaval.glo4003.ListResponseMapper.mapListResponse;
import static ca.ulaval.glo4003.hiking.HikingRegion.GASPESIE;
import static ca.ulaval.glo4003.traveler.user.role.Permission.HOSTING_RESERVATION;
import static jakarta.ws.rs.client.ClientBuilder.newClient;
import static jakarta.ws.rs.client.Entity.json;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static jakarta.ws.rs.core.Response.Status.*;
import static java.util.Collections.emptyMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.ApplicationMocker;
import ca.ulaval.glo4003.auth.AuthBinder;
import ca.ulaval.glo4003.auth.authentication.AuthenticationRequestFilterMocker;
import ca.ulaval.glo4003.auth.authorisation.PermissionRequestFilterMocker;
import ca.ulaval.glo4003.exception.*;
import ca.ulaval.glo4003.hiking.HikingRegion;
import ca.ulaval.glo4003.hosting.assembler.SearchHostingResponseAssembler;
import ca.ulaval.glo4003.hosting.dto.HostingDto;
import ca.ulaval.glo4003.hosting.exception.HostingNoCapacityAvailableException;
import ca.ulaval.glo4003.hosting.exception.HostingNotFoundException;
import ca.ulaval.glo4003.hosting.reservation.HostingReservationService;
import ca.ulaval.glo4003.hosting.reservation.assembler.CreateHostingReservationResponseAssembler;
import ca.ulaval.glo4003.hosting.reservation.dto.CreatedHostingReservationDto;
import ca.ulaval.glo4003.hosting.reservation.response.HostingReservationResponse;
import ca.ulaval.glo4003.hosting.response.SearchHostingResponse;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.trip.TripID;
import ca.ulaval.glo4003.trip.exception.TripNotFoundException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HostingResourceIT {

  private static final ApplicationMocker APP = new ApplicationMocker();
  private static final TravelerID USER_ID = new TravelerID("userId");
  private static final TripID TRIP_ID = TripID.generate();
  private static final HostingID HOSTING_ID = new HostingID(1);
  private static final String SEARCH_HOSTS_URL =
      String.format("http://localhost:8080/api/trekul/trips/%s/hosts", TRIP_ID);
  private static final String BOOK_HOST_URL =
      String.format("%s/%s:book", SEARCH_HOSTS_URL, HOSTING_ID);
  private static final String HOSTING_NAME = "hosting";
  private static final HikingRegion REGION = GASPESIE;
  private static final HostingDto HOSTING_DTO =
      new HostingDto(HOSTING_ID, HOSTING_NAME, REGION, emptyMap(), 1, 1, emptyMap());
  private static final List<HostingDto> HOSTING_DTO_LIST = List.of(HOSTING_DTO);
  private static final SearchHostingResponse SEARCH_HOSTING_RESPONSE =
      new SearchHostingResponse(
          HOSTING_ID.toString(), HOSTING_NAME, REGION.toString(), 1, "1", "1");
  private static final List<SearchHostingResponse> SEARCH_HOSTING_RESPONSE_LIST =
      List.of(SEARCH_HOSTING_RESPONSE);
  private static final CreatedHostingReservationDto CREATED_HOSTING_RESERVATION_DTO =
      new CreatedHostingReservationDto(HOSTING_ID, HOSTING_NAME, TRIP_ID, emptyMap());
  private static final HostingReservationResponse HOSTING_RESERVATION_RESPONSE =
      new HostingReservationResponse(HOSTING_ID.toString(), TRIP_ID.toString(), "");

  private HostingService hostingService;
  private SearchHostingResponseAssembler searchHostingResponseAssembler;
  private HostingReservationService hostingReservationService;
  private CreateHostingReservationResponseAssembler createHostingReservationResponseAssembler;

  private AuthenticationRequestFilterMocker authenticationRequestFilterMocker;
  private PermissionRequestFilterMocker permissionRequestFilterMocker;
  private Client client;

  @BeforeEach
  void setUp() throws Exception {
    this.hostingService = mock(HostingService.class);
    this.searchHostingResponseAssembler = mock(SearchHostingResponseAssembler.class);
    this.hostingReservationService = mock(HostingReservationService.class);
    this.createHostingReservationResponseAssembler =
        mock(CreateHostingReservationResponseAssembler.class);
    this.authenticationRequestFilterMocker = new AuthenticationRequestFilterMocker(USER_ID);
    this.permissionRequestFilterMocker = new PermissionRequestFilterMocker();

    HostingResource hostingResource =
        new HostingResourceImpl(
            this.hostingService,
            this.searchHostingResponseAssembler,
            this.hostingReservationService,
            this.createHostingReservationResponseAssembler);

    APP.startServer(
        new TrekULDeniedExceptionMapper(),
        new TrekULAuthenticationExceptionMapper(),
        new TrekULExceptionMapper(),
        new TrekULNotFoundExceptionMapper(),
        new AuthBinder(),
        this.authenticationRequestFilterMocker,
        this.permissionRequestFilterMocker,
        hostingResource);
    this.client = newClient();
  }

  @AfterEach
  void tearDown() throws Exception {
    APP.stopServer();
    this.client.close();
  }

  @Test
  void whenFindHostsInRegion_thenOk() {
    // GIVEN
    WebTarget target =
        this.client.target(SEARCH_HOSTS_URL).queryParam("tripID", TRIP_ID.toString());

    when(this.hostingService.searchCompatibleHosts(TRIP_ID)).thenReturn(HOSTING_DTO_LIST);
    when(this.searchHostingResponseAssembler.assemble(HOSTING_DTO_LIST))
        .thenReturn(SEARCH_HOSTING_RESPONSE_LIST);

    // WHEN
    Response response = target.request(APPLICATION_JSON_TYPE).get();

    // THEN
    List<HashMap<String, Object>> hosts = mapListResponse(response);
    HashMap<String, Object> hosting = hosts.getFirst();

    assertEquals(OK.getStatusCode(), response.getStatus());
    assertEquals(1, hosts.size());
    assertEquals(HOSTING_NAME, hosting.get("hostingName"));
  }

  @Test
  void givenBadTripId_whenFindHostsInRegion_thenNotFound() {
    // GIVEN
    WebTarget target =
        this.client.target(SEARCH_HOSTS_URL).queryParam("tripID", TRIP_ID.toString());

    when(this.hostingService.searchCompatibleHosts(TRIP_ID)).thenThrow(TripNotFoundException.class);

    // WHEN
    Response response = target.request(APPLICATION_JSON_TYPE).get();

    // THEN
    assertEquals(NOT_FOUND.getStatusCode(), response.getStatus());
  }

  @Test
  void whenBookHosting_thenOk() {
    // GIVEN
    WebTarget target = this.client.target(BOOK_HOST_URL);

    when(this.hostingReservationService.bookHosting(TRIP_ID, HOSTING_ID))
        .thenReturn(CREATED_HOSTING_RESERVATION_DTO);
    when(this.createHostingReservationResponseAssembler.assemble(CREATED_HOSTING_RESERVATION_DTO))
        .thenReturn(HOSTING_RESERVATION_RESPONSE);

    // WHEN
    Response response = target.request(APPLICATION_JSON_TYPE).post(json(null));

    // THEN
    HostingReservationResponse reservation = response.readEntity(HostingReservationResponse.class);

    assertNotNull(reservation);
    assertEquals(OK.getStatusCode(), response.getStatus());
    assertEquals(HOSTING_ID.toString(), reservation.hostingID());
    assertEquals(TRIP_ID.toString(), reservation.tripID());
  }

  @Test
  void givenBadPermission_whenBookHosting_thenForbidden() {
    // GIVEN
    WebTarget target = this.client.target(BOOK_HOST_URL);

    this.permissionRequestFilterMocker.verifyPermission(HOSTING_RESERVATION);

    // WHEN
    Response response = target.request(APPLICATION_JSON_TYPE).post(json(null));

    // THEN
    assertEquals(FORBIDDEN.getStatusCode(), response.getStatus());
  }

  @Test
  void givenBadTripId_whenBookHosting_thenNotFound() {
    // GIVEN
    WebTarget target = this.client.target(BOOK_HOST_URL);

    when(this.hostingReservationService.bookHosting(TRIP_ID, HOSTING_ID))
        .thenThrow(TripNotFoundException.class);

    // WHEN
    Response response = target.request(APPLICATION_JSON_TYPE).post(json(null));

    // THEN
    assertEquals(NOT_FOUND.getStatusCode(), response.getStatus());
  }

  @Test
  void givenInvalidHosting_whenBookHosting_thenNotFound() {
    // GIVEN
    WebTarget target = this.client.target(BOOK_HOST_URL);

    when(this.hostingReservationService.bookHosting(TRIP_ID, HOSTING_ID))
        .thenThrow(HostingNotFoundException.class);

    // WHEN
    Response response = target.request(APPLICATION_JSON_TYPE).post(json(null));

    // THEN
    assertEquals(NOT_FOUND.getStatusCode(), response.getStatus());
  }

  @Test
  void givenHostingFull_whenBookHosting_thenBadRequest() {
    // GIVEN
    WebTarget target = this.client.target(BOOK_HOST_URL);

    when(this.hostingReservationService.bookHosting(TRIP_ID, HOSTING_ID))
        .thenThrow(HostingNoCapacityAvailableException.class);

    // WHEN
    Response response = target.request(APPLICATION_JSON_TYPE).post(json(null));

    // THEN
    assertEquals(BAD_REQUEST.getStatusCode(), response.getStatus());
  }

  @Test
  void whenBookHosting_thenUnauthorized() {
    // GIVEN
    WebTarget target = this.client.target(BOOK_HOST_URL);
    this.authenticationRequestFilterMocker.asUnauthorized();

    // WHEN
    Response response = target.request(APPLICATION_JSON_TYPE).post(json(null));

    // THEN
    assertEquals(UNAUTHORIZED.getStatusCode(), response.getStatus());
  }
}
