package ca.ulaval.glo4003.trip.usecase;

import ca.ulaval.glo4003.trip.TripService;
import ca.ulaval.glo4003.trip.assembler.SearchTripRequestAssembler;
import ca.ulaval.glo4003.trip.assembler.SearchTripResponseAssembler;
import ca.ulaval.glo4003.trip.dto.SearchTripDto;
import ca.ulaval.glo4003.trip.dto.TripDto;
import ca.ulaval.glo4003.trip.request.SearchTripRequest;
import ca.ulaval.glo4003.trip.response.SearchTripResponse;
import jakarta.inject.Inject;
import java.util.List;

public class SearchTripUseCase {
  private final TripService tripService;
  private final SearchTripRequestAssembler searchTripRequestAssembler;
  private final SearchTripResponseAssembler searchTripResponseAssembler;

  @Inject
  public SearchTripUseCase(
      TripService tripService,
      SearchTripRequestAssembler searchTripRequestAssembler,
      SearchTripResponseAssembler searchTripResponseAssembler) {
    this.tripService = tripService;
    this.searchTripRequestAssembler = searchTripRequestAssembler;
    this.searchTripResponseAssembler = searchTripResponseAssembler;
  }

  public List<SearchTripResponse> searchTrip(SearchTripRequest searchTripRequest) {
    SearchTripDto searchTripDto = this.searchTripRequestAssembler.assemble(searchTripRequest);
    List<TripDto> trips = this.tripService.searchTripByPreferences(searchTripDto);
    return this.searchTripResponseAssembler.assemble(trips);
  }
}
