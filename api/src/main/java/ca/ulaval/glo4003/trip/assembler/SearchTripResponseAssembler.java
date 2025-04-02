package ca.ulaval.glo4003.trip.assembler;

import ca.ulaval.glo4003.hiking.assembler.HikingResponseAssembler;
import ca.ulaval.glo4003.hiking.response.HikingResponse;
import ca.ulaval.glo4003.trip.dto.TripDto;
import ca.ulaval.glo4003.trip.response.SearchTripResponse;
import jakarta.inject.Inject;
import java.util.List;

public class SearchTripResponseAssembler {
  private final HikingResponseAssembler hikingResponseAssembler;

  @Inject
  public SearchTripResponseAssembler(HikingResponseAssembler hikingResponseAssembler) {
    this.hikingResponseAssembler = hikingResponseAssembler;
  }

  public List<SearchTripResponse> assemble(List<TripDto> trips) {
    return trips.stream().map(this::assemble).toList();
  }

  private SearchTripResponse assemble(TripDto trip) {
    HikingResponse hikingResponse = this.hikingResponseAssembler.assemble(trip.hiking());
    return new SearchTripResponse(
        hikingResponse,
        trip.availableSeats(),
        trip.status().toString(),
        trip.date().toString(),
        trip.cost().toString(),
        trip.tripID().toString());
  }
}
