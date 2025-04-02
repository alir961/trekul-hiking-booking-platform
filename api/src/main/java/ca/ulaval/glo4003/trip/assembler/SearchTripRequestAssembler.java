package ca.ulaval.glo4003.trip.assembler;

import ca.ulaval.glo4003.hiking.HikingRegion;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.trip.TripStatus;
import ca.ulaval.glo4003.trip.dto.SearchTripDto;
import ca.ulaval.glo4003.trip.request.SearchTripRequest;
import java.util.Optional;

public class SearchTripRequestAssembler {
  public SearchTripDto assemble(SearchTripRequest searchTripRequest) {
    TripStatus status = TripStatus.valueOf(searchTripRequest.getStatus().toUpperCase());
    Optional<HikingRegion> region = Optional.empty();
    if (searchTripRequest.getRegion() != null) {
      region = Optional.of(HikingRegion.valueOf(searchTripRequest.getRegion().toUpperCase()));
    }

    return new SearchTripDto(
        new Money(searchTripRequest.getPriceLimit()),
        searchTripRequest.getStartDate(),
        searchTripRequest.getEndDate(),
        status,
        region);
  }
}
