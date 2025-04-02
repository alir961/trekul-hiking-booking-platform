package ca.ulaval.glo4003.trip;

import ca.ulaval.glo4003.trip.assembler.TripDtoAssembler;
import ca.ulaval.glo4003.trip.dto.SearchTripDto;
import ca.ulaval.glo4003.trip.dto.TripDto;
import ca.ulaval.glo4003.trip.filter.SearchTripFilterBuilder;
import java.util.List;

public class TripService {
  private final TripRepository tripRepository;
  private final TripSearchFilterer tripSearchFilterer;
  private final TripDtoAssembler tripDtoAssembler;

  public TripService(
      TripRepository tripRepository,
      TripSearchFilterer tripSearchFilterer,
      TripDtoAssembler tripDtoAssembler) {
    this.tripRepository = tripRepository;
    this.tripSearchFilterer = tripSearchFilterer;
    this.tripDtoAssembler = tripDtoAssembler;
  }

  public List<TripDto> searchTripByPreferences(SearchTripDto searchTripDto) {
    List<Trip> tripList = this.tripRepository.getAll();
    SearchTripFilterBuilder searchTripFilterBuilder = this.prepareBuilder(searchTripDto);

    List<Trip> filtered =
        this.tripSearchFilterer.filterSearch(tripList, searchTripFilterBuilder.build());
    return this.tripDtoAssembler.assemble(filtered);
  }

  private SearchTripFilterBuilder prepareBuilder(SearchTripDto searchTripDto) {
    SearchTripFilterBuilder searchTripFilterBuilder = new SearchTripFilterBuilder();
    searchTripFilterBuilder
        .addSearchTripCostFilter(searchTripDto.priceLimit())
        .addSearchTripDateFilter(searchTripDto.startDate(), searchTripDto.endDate())
        .addSearchTripStatusFilter(searchTripDto.status());
    searchTripDto.region().ifPresent(searchTripFilterBuilder::addSearchTripRegionFilter);

    return searchTripFilterBuilder;
  }
}
