package ca.ulaval.glo4003.trip.usecase;

import static ca.ulaval.glo4003.trip.TripStatus.FULL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

import ca.ulaval.glo4003.hiking.HikingRegion;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.trip.*;
import ca.ulaval.glo4003.trip.assembler.SearchTripRequestAssembler;
import ca.ulaval.glo4003.trip.assembler.SearchTripResponseAssembler;
import ca.ulaval.glo4003.trip.dto.SearchTripDto;
import ca.ulaval.glo4003.trip.dto.TripDto;
import ca.ulaval.glo4003.trip.request.SearchTripRequest;
import ca.ulaval.glo4003.trip.response.SearchTripResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SearchTripUseCaseTest {
  private static final HikingRegion ANY_REGION = HikingRegion.SAGLAC;
  private static final LocalDate ANY_START_DATE = LocalDate.now();
  private static final LocalDate ANY_END_DATE = LocalDate.now();
  private static final float ANY_PRICE_LIMIT = 12.5f;
  private static final TripStatus ANY_STATUS = FULL;
  private static final SearchTripRequest ANY_SEARCH_TRIP_REQUEST =
      new SearchTripRequest(
          ANY_REGION.toString(),
          ANY_START_DATE.toString(),
          ANY_END_DATE.toString(),
          ANY_PRICE_LIMIT,
          ANY_STATUS.toString());
  private static final SearchTripDto ANY_SEARCH_TRIP_DTO =
      new SearchTripDto(
          new Money(String.valueOf(ANY_PRICE_LIMIT)),
          ANY_START_DATE,
          ANY_END_DATE,
          ANY_STATUS,
          Optional.of(ANY_REGION));
  private TripService tripServiceMock;
  private SearchTripRequestAssembler searchTripRequestAssemblerMock;
  private SearchTripResponseAssembler searchTripResponseAssemblerMock;
  private SearchTripUseCase searchTripUseCase;

  @BeforeEach
  void setUp() {
    this.tripServiceMock = mock(TripService.class);
    this.searchTripResponseAssemblerMock = mock(SearchTripResponseAssembler.class);
    this.searchTripRequestAssemblerMock = mock(SearchTripRequestAssembler.class);
    this.searchTripUseCase =
        new SearchTripUseCase(
            this.tripServiceMock,
            this.searchTripRequestAssemblerMock,
            this.searchTripResponseAssemblerMock);
  }

  @Test
  void whenSearchTrip_thenReturnAssembledTrips() {
    List<SearchTripResponse> responses = this.givenSearchingForTrip();

    List<SearchTripResponse> returnedResponse =
        this.searchTripUseCase.searchTrip(ANY_SEARCH_TRIP_REQUEST);

    assertEquals(responses, returnedResponse);
  }

  private List<SearchTripResponse> givenSearchingForTrip() {
    List<TripDto> anyTripsDto = new ArrayList<>();
    List<SearchTripResponse> anySearchTripResponses = new ArrayList<>();
    when(this.searchTripRequestAssemblerMock.assemble(ANY_SEARCH_TRIP_REQUEST))
        .thenReturn(ANY_SEARCH_TRIP_DTO);
    when(this.tripServiceMock.searchTripByPreferences(ANY_SEARCH_TRIP_DTO)).thenReturn(anyTripsDto);
    when(this.searchTripResponseAssemblerMock.assemble(anyTripsDto))
        .thenReturn(anySearchTripResponses);
    return anySearchTripResponses;
  }
}
