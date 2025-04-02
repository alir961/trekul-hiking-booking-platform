package ca.ulaval.glo4003.trip;

import static ca.ulaval.glo4003.hiking.HikingRegion.GASPESIE;
import static ca.ulaval.glo4003.trip.TripStatus.BLOCKED;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.hiking.HikingRegion;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.trip.assembler.TripDtoAssembler;
import ca.ulaval.glo4003.trip.dto.SearchTripDto;
import ca.ulaval.glo4003.trip.dto.TripDto;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TripServiceTest {
  private static final List<Trip> ANY_TRIP_LIST = new ArrayList<>();
  private static final List<TripDto> ASSEMBLED_TRIP_DTOS = List.of(mock(TripDto.class));
  private static final Money ANY_MONEY = new Money("702");
  private static final LocalDate ANY_DATE = LocalDate.now();
  private static final TripStatus ANY_TRIP_STATUS = BLOCKED;
  private static final HikingRegion ANY_HIKING_REGION = GASPESIE;
  private static final SearchTripDto ANY_FULL_SEARCH_TRIP_DTO =
      new SearchTripDto(
          ANY_MONEY, ANY_DATE, ANY_DATE, ANY_TRIP_STATUS, Optional.of(ANY_HIKING_REGION));

  private TripSearchFilterer tripSearchFilterer;
  private TripRepository tripRepository;
  private TripService tripService;

  @BeforeEach
  void setUp() {

    this.tripRepository = mock(TripRepository.class);
    this.tripSearchFilterer = mock(TripSearchFilterer.class);
    final TripDtoAssembler tripDtoAssembler = mock(TripDtoAssembler.class);
    this.tripService =
        new TripService(this.tripRepository, this.tripSearchFilterer, tripDtoAssembler);

    when(tripDtoAssembler.assemble(ANY_TRIP_LIST)).thenReturn(ASSEMBLED_TRIP_DTOS);
    when(this.tripRepository.getAll()).thenReturn(ANY_TRIP_LIST);
  }

  @Test
  void whenSearchingByPreferences_thenReturnFilteredListOfTrips() {
    List<TripDto> result = this.tripService.searchTripByPreferences(ANY_FULL_SEARCH_TRIP_DTO);

    assertFalse(result.isEmpty());
    assertSame(ASSEMBLED_TRIP_DTOS, result);
  }
}
