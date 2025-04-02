package ca.ulaval.glo4003.trip;

import static ca.ulaval.glo4003.hiking.HikingDifficulty.INTERMEDIATE;
import static ca.ulaval.glo4003.hiking.HikingRegion.GASPESIE;
import static jakarta.ws.rs.client.ClientBuilder.newClient;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static jakarta.ws.rs.core.Response.Status.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4003.ApplicationMocker;
import ca.ulaval.glo4003.auth.AuthBinder;
import ca.ulaval.glo4003.auth.authentication.AuthenticationRequestFilterMocker;
import ca.ulaval.glo4003.exception.TrekULAuthenticationExceptionMapper;
import ca.ulaval.glo4003.hiking.response.HikingResponse;
import ca.ulaval.glo4003.hosting.response.HostingResponse;
import ca.ulaval.glo4003.invoice.response.InvoiceShortResponse;
import ca.ulaval.glo4003.profile.response.ProfileTripResponse;
import ca.ulaval.glo4003.reservation.response.ReservationStatusResponse;
import ca.ulaval.glo4003.token.Guest2FAService;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.trip.usecase.SearchTripUnauthenticatedUseCase;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TripGuestResourceIT {
  private static final TripID TRIP_ID = TripID.generate();
  private static final TravelerID GUEST_ID = new TravelerID("111222333");
  private static final String ACTIVATE_2FA_URL =
      String.format("http://localhost:8080/api/trekul/trips/%s/guests/%s/2FA", TRIP_ID, GUEST_ID);
  private static final String SEARCH_TRIP_FOR_GUEST_URL =
      String.format("http://localhost:8080/api/trekul/trips/%s:search", TRIP_ID);

  private static final ApplicationMocker APP = new ApplicationMocker();
  private static final HostingResponse HOSTING_RESPONSE = mock(HostingResponse.class);
  private static final String START_DATE = "2024-11-01";
  private static final HikingResponse HIKING_RESPONSE =
      new HikingResponse(GASPESIE.toString(), INTERMEDIATE.toString(), "10km");
  private static final InvoiceShortResponse INVOICE_RESPONSE = mock(InvoiceShortResponse.class);
  private static final ReservationStatusResponse RESERVATION_RESPONSE =
      mock(ReservationStatusResponse.class);

  private static final ProfileTripResponse SEARCH_TRIP_UNAUTHENTICATED_RESPONSE =
      new ProfileTripResponse(
          TRIP_ID.toString(),
          HIKING_RESPONSE,
          START_DATE,
          INVOICE_RESPONSE,
          RESERVATION_RESPONSE,
          HOSTING_RESPONSE);

  private SearchTripUnauthenticatedUseCase searchTripUnauthenticatedUseCaseMock;
  private Guest2FAService guest2FAServiceMock;
  private AuthenticationRequestFilterMocker authenticationRequestFilterMocker;

  private Client client;

  @BeforeEach
  void setUp() throws Exception {
    this.searchTripUnauthenticatedUseCaseMock = mock(SearchTripUnauthenticatedUseCase.class);
    this.guest2FAServiceMock = mock(Guest2FAService.class);
    this.authenticationRequestFilterMocker = new AuthenticationRequestFilterMocker(GUEST_ID);

    TripGuestResourceImpl tripGuestResourceImpl =
        new TripGuestResourceImpl(
            this.searchTripUnauthenticatedUseCaseMock, this.guest2FAServiceMock);

    APP.startServer(
        new TrekULAuthenticationExceptionMapper(),
        tripGuestResourceImpl,
        new TripResourceImpl(null),
        new TripAuthenticatedResourceImpl(null, null, null),
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
  void whenGetTripsForTraveler_thenSuccess() {
    // GIVEN
    WebTarget target = this.client.target(SEARCH_TRIP_FOR_GUEST_URL);

    when(this.searchTripUnauthenticatedUseCaseMock.searchTripUnauthenticated(TRIP_ID, GUEST_ID))
        .thenReturn(SEARCH_TRIP_UNAUTHENTICATED_RESPONSE);

    // WHEN
    Response response = target.request(APPLICATION_JSON_TYPE).get();

    // THEN
    assertEquals(OK.getStatusCode(), response.getStatus());

    ProfileTripResponse searchTripUnauthenticatedResponse =
        response.readEntity(ProfileTripResponse.class);
    assertEquals(TRIP_ID.toString(), searchTripUnauthenticatedResponse.tripId());
  }

  @Test
  void whenGetTripsForTraveler_thenUnauthorized() {
    // GIVEN
    WebTarget target = this.client.target(SEARCH_TRIP_FOR_GUEST_URL);
    this.authenticationRequestFilterMocker.asUnauthorized();

    // WHEN
    Response response = target.request(APPLICATION_JSON_TYPE).get();

    // THEN
    assertEquals(UNAUTHORIZED.getStatusCode(), response.getStatus());
  }

  @Test
  void whenActivating2FA_thenAccepted() {
    // GIVEN
    WebTarget target = this.client.target(ACTIVATE_2FA_URL);

    // WHEN
    Response response = target.request(APPLICATION_JSON_TYPE).get();

    // THEN
    assertEquals(NO_CONTENT.getStatusCode(), response.getStatus());
  }
}
