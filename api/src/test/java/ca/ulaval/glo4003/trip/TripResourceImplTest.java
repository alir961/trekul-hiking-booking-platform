package ca.ulaval.glo4003.trip;

import static ca.ulaval.glo4003.trip.TripStatus.AVAILABLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.hiking.HikingRegion;
import ca.ulaval.glo4003.trip.request.SearchTripRequest;
import ca.ulaval.glo4003.trip.response.SearchTripResponse;
import ca.ulaval.glo4003.trip.usecase.SearchTripUseCase;
import jakarta.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TripResourceImplTest {
  private static final String ANY_REGION_AS_STR = HikingRegion.SAGLAC.toString();
  private static final String ANY_START_DATE_AS_STR = LocalDate.now().toString();
  private static final double ANY_PRICE_LIMIT = 12.5f;
  private static final String ANY_END_DATE_AS_STR = LocalDate.now().toString();
  private static final String ANY_TRIP_STATUS = AVAILABLE.toString();
  private static final SearchTripRequest ANY_SEARCH_TRIP_REQUEST =
      new SearchTripRequest(
          ANY_REGION_AS_STR,
          ANY_START_DATE_AS_STR,
          ANY_END_DATE_AS_STR,
          ANY_PRICE_LIMIT,
          ANY_TRIP_STATUS);
  private SearchTripUseCase searchTripUseCaseMock;
  private TripResourceImpl tripResource;

  @BeforeEach
  void setUp() {
    this.searchTripUseCaseMock = mock(SearchTripUseCase.class);
    this.tripResource = new TripResourceImpl(this.searchTripUseCaseMock);
  }

  @Test
  void whenSearchTrip_thenReturnAssembledTrips() {
    List<SearchTripResponse> responses = this.givenSearchingForTrip();

    Response returnedResponse = this.tripResource.searchTrip(ANY_SEARCH_TRIP_REQUEST);

    assertTrue(returnedResponse.hasEntity());
    assertEquals(responses, returnedResponse.getEntity());
  }

  @Test
  void whenSearchTrip_thenReturnHttpOk() {
    this.givenSearchingForTrip();

    Response returnedResponse = this.tripResource.searchTrip(ANY_SEARCH_TRIP_REQUEST);

    assertEquals(HttpStatus.OK_200, returnedResponse.getStatus());
  }

  private List<SearchTripResponse> givenSearchingForTrip() {
    List<SearchTripResponse> anySearchTripResponses = new ArrayList<>();
    when(this.searchTripUseCaseMock.searchTrip(ANY_SEARCH_TRIP_REQUEST))
        .thenReturn(anySearchTripResponses);
    return anySearchTripResponses;
  }
}
