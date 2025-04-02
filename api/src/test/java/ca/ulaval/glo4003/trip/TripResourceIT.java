package ca.ulaval.glo4003.trip;

import static ca.ulaval.glo4003.ListResponseMapper.mapListResponse;
import static ca.ulaval.glo4003.hiking.HikingDifficulty.INTERMEDIATE;
import static ca.ulaval.glo4003.hiking.HikingRegion.GASPESIE;
import static ca.ulaval.glo4003.trip.TripStatus.AVAILABLE;
import static jakarta.ws.rs.client.ClientBuilder.newClient;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static jakarta.ws.rs.core.Response.Status.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.ApplicationMocker;
import ca.ulaval.glo4003.hiking.response.HikingResponse;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.trip.request.SearchTripRequest;
import ca.ulaval.glo4003.trip.response.SearchTripResponse;
import ca.ulaval.glo4003.trip.usecase.SearchTripUseCase;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TripResourceIT {

  private static final ApplicationMocker APP = new ApplicationMocker();
  private static final String REGION = "";
  private static final String START_DATE = "2024-11-01";
  private static final String END_DATE = "2025-01-01";
  private static final float PRICE_LIMIT = 100;
  private static final String DEFAULT_TRIP_STATUS = AVAILABLE.toString();
  private static final SearchTripRequest SEARCH_TRIP_REQUEST =
      new SearchTripRequest(REGION, START_DATE, END_DATE, PRICE_LIMIT, DEFAULT_TRIP_STATUS);
  private static final HikingResponse HIKING_RESPONSE =
      new HikingResponse(GASPESIE.toString(), INTERMEDIATE.toString(), "10km");
  private static final TripID TRIP_ID = TripID.generate();
  private static final Money COST = new Money("100");
  private static final SearchTripResponse SEARCH_TRIP_RESPONSE =
      new SearchTripResponse(
          HIKING_RESPONSE, 1, "ANY STATUS", START_DATE, COST.toString(), TRIP_ID.toString());
  private static final List<SearchTripResponse> SEARCH_TRIP_RESPONSES =
      List.of(SEARCH_TRIP_RESPONSE);

  private SearchTripUseCase searchTripUseCase;

  private Client client;

  @BeforeEach
  void setUp() throws Exception {
    this.searchTripUseCase = mock(SearchTripUseCase.class);

    TripResource tripResource = new TripResourceImpl(this.searchTripUseCase);

    APP.startServer(tripResource);
    this.client = newClient();
  }

  @AfterEach
  void tearDown() throws Exception {
    APP.stopServer();
    this.client.close();
  }

  @Test
  void whenGetTrips_thenSuccess() {
    // GIVEN
    WebTarget target =
        this.client
            .target("http://localhost:8080/api/trekul/trips")
            .queryParam("priceLimit", PRICE_LIMIT)
            .queryParam("startDate", START_DATE)
            .queryParam("endDate", END_DATE)
            .queryParam("region", REGION);

    when(this.searchTripUseCase.searchTrip(refEq(SEARCH_TRIP_REQUEST)))
        .thenReturn(SEARCH_TRIP_RESPONSES);

    // WHEN
    Response response = target.request(APPLICATION_JSON_TYPE).get();

    // THEN
    List<HashMap<String, Object>> trips = mapListResponse(response);
    HashMap<String, Object> trip = trips.getFirst();

    assertEquals(OK.getStatusCode(), response.getStatus());
    assertEquals(1, trips.size());
    assertEquals(TRIP_ID.toString(), trip.get("id"));
  }
}
