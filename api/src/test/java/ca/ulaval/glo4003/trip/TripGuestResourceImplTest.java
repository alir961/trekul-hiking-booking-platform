package ca.ulaval.glo4003.trip;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.profile.response.ProfileTripResponse;
import ca.ulaval.glo4003.token.Guest2FAService;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.trip.usecase.SearchTripUnauthenticatedUseCase;
import jakarta.ws.rs.core.Response;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TripGuestResourceImplTest {
  private static final String ANY_AUTH_TOKEN = "h943hnn";
  private static final TripID ANY_TRIP_ID = TripID.generate();
  private static final TravelerID ANY_TRAVELER_ID = new TravelerID("60012");
  private SearchTripUnauthenticatedUseCase searchTripUnauthenticatedUseCaseMock;
  private Guest2FAService guest2FAServiceMock;
  private TripGuestResourceImpl tripGuestResourceImpl;

  @BeforeEach
  void setUp() {
    this.guest2FAServiceMock = mock(Guest2FAService.class);
    this.searchTripUnauthenticatedUseCaseMock = mock(SearchTripUnauthenticatedUseCase.class);
    this.tripGuestResourceImpl =
        new TripGuestResourceImpl(
            this.searchTripUnauthenticatedUseCaseMock, this.guest2FAServiceMock);
  }

  @Test
  void whenSearchTripByIdAndTravelerId_thenReturnAssembledTrip() {
    ProfileTripResponse response = this.givenSearchingTripUnauthenticated();

    Response returnedResponse =
        this.tripGuestResourceImpl.searchTripByIdAndTravelerId(ANY_TRIP_ID, ANY_TRAVELER_ID);

    assertTrue(returnedResponse.hasEntity());
    assertEquals(response, returnedResponse.getEntity());
  }

  @Test
  void whenSearchTrip_thenReturnHttpOk() {
    this.givenSearchingTripUnauthenticated();

    Response returnedResponse =
        this.tripGuestResourceImpl.searchTripByIdAndTravelerId(ANY_TRIP_ID, ANY_TRAVELER_ID);

    assertEquals(HttpStatus.OK_200, returnedResponse.getStatus());
  }

  private ProfileTripResponse givenSearchingTripUnauthenticated() {
    ProfileTripResponse anyProfileTripResponse = mock(ProfileTripResponse.class);
    when(this.searchTripUnauthenticatedUseCaseMock.searchTripUnauthenticated(
            ANY_TRIP_ID, ANY_TRAVELER_ID))
        .thenReturn(anyProfileTripResponse);
    return anyProfileTripResponse;
  }

  @Test
  void whenActivating2FAForGuestOfTrip_thenReturnHttpAccepted() {

    Response returnedResponse =
        this.tripGuestResourceImpl.activate2FAForGuestOfTrip(
            ANY_TRIP_ID, ANY_TRAVELER_ID.toString());

    assertEquals(HttpStatus.NO_CONTENT_204, returnedResponse.getStatus());
  }

  @Test
  void whenActivating2FAForGuestOfTrip_thenGuest2FAServiceIsTriggered() {
    this.tripGuestResourceImpl.activate2FAForGuestOfTrip(ANY_TRIP_ID, ANY_TRAVELER_ID.toString());

    verify(this.guest2FAServiceMock).send2FAToken(ANY_TRIP_ID, ANY_TRAVELER_ID);
  }
}
