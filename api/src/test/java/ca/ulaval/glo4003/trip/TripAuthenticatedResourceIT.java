package ca.ulaval.glo4003.trip;

import static ca.ulaval.glo4003.transport.TransportType.ORGANIZED_BUS;
import static ca.ulaval.glo4003.traveler.user.role.Permission.TRIP_UPDATE;
import static jakarta.ws.rs.client.ClientBuilder.newClient;
import static jakarta.ws.rs.client.Entity.json;
import static jakarta.ws.rs.client.Entity.text;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static jakarta.ws.rs.core.Response.Status.*;
import static jakarta.ws.rs.core.Response.Status.UNAUTHORIZED;
import static java.time.ZonedDateTime.now;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;

import ca.ulaval.glo4003.ApplicationMocker;
import ca.ulaval.glo4003.auth.AuthBinder;
import ca.ulaval.glo4003.auth.authentication.AuthenticationRequestFilterMocker;
import ca.ulaval.glo4003.auth.authorisation.PermissionRequestFilterMocker;
import ca.ulaval.glo4003.exception.TrekULAuthenticationExceptionMapper;
import ca.ulaval.glo4003.exception.TrekULDeniedExceptionMapper;
import ca.ulaval.glo4003.exception.TrekULExceptionMapper;
import ca.ulaval.glo4003.exception.TrekULNotFoundExceptionMapper;
import ca.ulaval.glo4003.group.reponse.GroupResponse;
import ca.ulaval.glo4003.guest.request.GuestRequest;
import ca.ulaval.glo4003.guest.response.GuestResponse;
import ca.ulaval.glo4003.invoice.InvoiceID;
import ca.ulaval.glo4003.invoice.response.InvoiceResponse;
import ca.ulaval.glo4003.reservation.ReservationID;
import ca.ulaval.glo4003.reservation.request.ReservationRequest;
import ca.ulaval.glo4003.reservation.response.ReservationResponse;
import ca.ulaval.glo4003.reservation.usecase.ReservationCreationUseCase;
import ca.ulaval.glo4003.transport.TransportType;
import ca.ulaval.glo4003.transport.exception.TransportFullyBookedException;
import ca.ulaval.glo4003.transport.exception.TransportNotAvailableException;
import ca.ulaval.glo4003.transport.exception.TransportStopDoesNotExistException;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.trip.exception.TripNotAvailableException;
import ca.ulaval.glo4003.trip.exception.TripNotFoundException;
import ca.ulaval.glo4003.trip.usecase.BlockTripUseCase;
import ca.ulaval.glo4003.trip.usecase.VerifyTripUseCase;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TripAuthenticatedResourceIT {
  private static final TripID TRIP_ID = TripID.generate();
  private static final String BLOCK_REQUEST_URL =
      String.format("http://localhost:8080/api/trekul/trips/%s:block", TRIP_ID);
  private static final String CREATE_RESERVATION_REQUEST_URL =
      String.format("http://localhost:8080/api/trekul/trips/%s:reserve", TRIP_ID);

  private static final ApplicationMocker APP = new ApplicationMocker();
  private static final TransportType TRANSPORT_TYPE = ORGANIZED_BUS;
  private static final String STOP = "";
  private static final String GUEST_FIRSTNAME = "Alice";
  private static final String GUEST_LASTNAME = "Lost in Wonders";
  private static final String GUEST_EMAIL = "email@ulaval.ca";
  private static final String GUEST_IDUL = "111222333";
  private static final String MINIMUM_TO_PAY = "123";
  private static final GuestRequest GUEST_DTO =
      new GuestRequest(GUEST_FIRSTNAME, GUEST_LASTNAME, GUEST_IDUL, GUEST_EMAIL);
  private static final List<GuestRequest> GUEST_DTO_LIST = List.of(GUEST_DTO);
  private static final ReservationRequest RESERVATION_REQUEST =
      new ReservationRequest(TRANSPORT_TYPE.toString(), STOP, GUEST_DTO_LIST);
  private static final InvoiceID INVOICE_ID = InvoiceID.generate();
  private static final InvoiceResponse INVOICE_RESPONSE =
      new InvoiceResponse(INVOICE_ID.toString(), emptyMap(), emptyMap(), "100", "100", emptyList());
  private static final GuestResponse GUEST_RESPONSE =
      new GuestResponse(GUEST_IDUL, GUEST_FIRSTNAME, GUEST_EMAIL);
  private static final List<GuestResponse> GUEST_RESPONSE_LIST = List.of(GUEST_RESPONSE);
  private static final GroupResponse GROUP_RESPONSE = new GroupResponse(GUEST_RESPONSE_LIST);
  private static final ReservationID ANY_RESERVATION_ID = ReservationID.generate();
  private static final ReservationResponse RESERVATION_RESPONSE =
      new ReservationResponse(
          ANY_RESERVATION_ID.toString(), INVOICE_RESPONSE, GROUP_RESPONSE, MINIMUM_TO_PAY, now());
  private static final TravelerID ANY_TRAVELER_ID = new TravelerID("6u0ihg3yh");

  private ReservationCreationUseCase reservationCreationUseCaseMock;
  private VerifyTripUseCase verifyTripUseCaseMock;
  private BlockTripUseCase blockTripUseCaseMock;
  private AuthenticationRequestFilterMocker authenticationRequestFilterMocker;
  private PermissionRequestFilterMocker permissionRequestFilterMocker;

  private Client client;

  @BeforeEach
  void setUp() throws Exception {
    this.verifyTripUseCaseMock = mock(VerifyTripUseCase.class);
    this.reservationCreationUseCaseMock = mock(ReservationCreationUseCase.class);
    this.blockTripUseCaseMock = mock(BlockTripUseCase.class);
    this.authenticationRequestFilterMocker = new AuthenticationRequestFilterMocker(ANY_TRAVELER_ID);
    this.permissionRequestFilterMocker = new PermissionRequestFilterMocker();

    TripAuthenticatedResourceImpl tripAuthenticatedResourceImpl =
        new TripAuthenticatedResourceImpl(
            this.blockTripUseCaseMock,
            this.reservationCreationUseCaseMock,
            this.verifyTripUseCaseMock);

    APP.startServer(
        new TrekULDeniedExceptionMapper(),
        new TrekULAuthenticationExceptionMapper(),
        new TrekULExceptionMapper(),
        new TrekULNotFoundExceptionMapper(),
        tripAuthenticatedResourceImpl,
        new TripResourceImpl(null),
        new AuthBinder(),
        this.authenticationRequestFilterMocker,
        this.permissionRequestFilterMocker);
    this.client = newClient();
  }

  @AfterEach
  void tearDown() throws Exception {
    APP.stopServer();
    this.client.close();
  }

  @Test
  void whenBlockTrip_thenSuccess() {
    // GIVEN
    WebTarget target = this.client.target(BLOCK_REQUEST_URL);

    doNothing().when(this.blockTripUseCaseMock).blockTrip(TRIP_ID);

    // WHEN
    Response response = target.request(APPLICATION_JSON_TYPE).put(text(""));

    // THEN
    assertEquals(NO_CONTENT.getStatusCode(), response.getStatus());
  }

  @Test
  void givenBadTripId_whenBlockTrip_thenNotFound() {
    // GIVEN
    WebTarget target = this.client.target(BLOCK_REQUEST_URL);

    doThrow(TripNotFoundException.class).when(this.blockTripUseCaseMock).blockTrip(TRIP_ID);

    // WHEN
    Response response = target.request(APPLICATION_JSON_TYPE).put(text(""));

    // THEN
    assertEquals(NOT_FOUND.getStatusCode(), response.getStatus());
  }

  @Test
  void givenBadPermission_whenBlockTrip_thenForbidden() {
    // GIVEN
    WebTarget target = this.client.target(BLOCK_REQUEST_URL);

    this.permissionRequestFilterMocker.verifyPermission(TRIP_UPDATE);

    // WHEN
    Response response = target.request(APPLICATION_JSON_TYPE).put(text(""));

    // THEN
    assertEquals(FORBIDDEN.getStatusCode(), response.getStatus());
  }

  @Test
  void whenBlockTrip_thenUnauthorized() {
    // GIVEN
    WebTarget target = this.client.target(BLOCK_REQUEST_URL);

    this.authenticationRequestFilterMocker.asUnauthorized();

    // WHEN
    Response response = target.request(APPLICATION_JSON_TYPE).put(text(""));

    // THEN
    assertEquals(UNAUTHORIZED.getStatusCode(), response.getStatus());
  }

  @Test
  void whenCreateReservation_thenCreated() {
    // GIVEN
    WebTarget target = this.client.target(CREATE_RESERVATION_REQUEST_URL);

    when(this.reservationCreationUseCaseMock.createReservation(
            RESERVATION_REQUEST, ANY_TRAVELER_ID, TRIP_ID))
        .thenReturn(RESERVATION_RESPONSE);

    // WHEN
    Response response = target.request(APPLICATION_JSON_TYPE).post(json(RESERVATION_REQUEST));

    // THEN
    ReservationResponse reservation = response.readEntity(ReservationResponse.class);

    assertEquals(CREATED.getStatusCode(), response.getStatus());
    assertEquals(INVOICE_ID.toString(), reservation.invoice().invoiceID());
  }

  @Test
  void givenInvalidTripId_whenCreateReservation_thenNotFound() {
    // GIVEN
    WebTarget target = this.client.target(CREATE_RESERVATION_REQUEST_URL);

    when(this.reservationCreationUseCaseMock.createReservation(
            RESERVATION_REQUEST, ANY_TRAVELER_ID, TRIP_ID))
        .thenThrow(TripNotFoundException.class);

    // WHEN
    Response response = target.request(APPLICATION_JSON_TYPE).post(json(RESERVATION_REQUEST));

    // THEN
    assertEquals(NOT_FOUND.getStatusCode(), response.getStatus());
  }

  @Test
  void givenTripNotAvailable_whenCreateReservation_thenBadRequest() {
    // GIVEN
    WebTarget target = this.client.target(CREATE_RESERVATION_REQUEST_URL);

    when(this.reservationCreationUseCaseMock.createReservation(
            RESERVATION_REQUEST, ANY_TRAVELER_ID, TRIP_ID))
        .thenThrow(TripNotAvailableException.class);

    // WHEN
    Response response = target.request(APPLICATION_JSON_TYPE).post(json(RESERVATION_REQUEST));

    // THEN
    assertEquals(BAD_REQUEST.getStatusCode(), response.getStatus());
  }

  @Test
  void givenTransportUnavailable_whenCreateReservation_thenBadRequest() {
    // GIVEN
    WebTarget target = this.client.target(CREATE_RESERVATION_REQUEST_URL);

    when(this.reservationCreationUseCaseMock.createReservation(
            RESERVATION_REQUEST, ANY_TRAVELER_ID, TRIP_ID))
        .thenThrow(TransportNotAvailableException.class);

    // WHEN
    Response response = target.request(APPLICATION_JSON_TYPE).post(json(RESERVATION_REQUEST));

    // THEN
    assertEquals(BAD_REQUEST.getStatusCode(), response.getStatus());
  }

  @Test
  void givenTransportFull_whenCreateReservation_thenForbidden() {
    // GIVEN
    WebTarget target = this.client.target(CREATE_RESERVATION_REQUEST_URL);

    when(this.reservationCreationUseCaseMock.createReservation(
            RESERVATION_REQUEST, ANY_TRAVELER_ID, TRIP_ID))
        .thenThrow(TransportFullyBookedException.class);

    // WHEN
    Response response = target.request(APPLICATION_JSON_TYPE).post(json(RESERVATION_REQUEST));

    // THEN
    assertEquals(FORBIDDEN.getStatusCode(), response.getStatus());
  }

  @Test
  void givenStopDoesNotExist_whenCreateReservation_thenNotFound() {
    // GIVEN
    WebTarget target = this.client.target(CREATE_RESERVATION_REQUEST_URL);

    when(this.reservationCreationUseCaseMock.createReservation(
            RESERVATION_REQUEST, ANY_TRAVELER_ID, TRIP_ID))
        .thenThrow(TransportStopDoesNotExistException.class);

    // WHEN
    Response response = target.request(APPLICATION_JSON_TYPE).post(json(RESERVATION_REQUEST));

    // THEN
    assertEquals(NOT_FOUND.getStatusCode(), response.getStatus());
  }

  @Test
  void givenUserNotAuthenticated_whenCreateReservation_thenUnauthorized() {
    // GIVEN
    WebTarget target = this.client.target(CREATE_RESERVATION_REQUEST_URL);

    this.authenticationRequestFilterMocker.asUnauthorized();

    // WHEN
    Response response = target.request(APPLICATION_JSON_TYPE).post(json(RESERVATION_REQUEST));

    // THEN
    assertEquals(UNAUTHORIZED.getStatusCode(), response.getStatus());
  }
}
