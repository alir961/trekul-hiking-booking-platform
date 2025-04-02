package ca.ulaval.glo4003.trip.assembler;

import static ca.ulaval.glo4003.hiking.HikingRegion.SAGLAC;
import static ca.ulaval.glo4003.trip.TripStatus.AVAILABLE;
import static org.junit.jupiter.api.Assertions.*;

import ca.ulaval.glo4003.hiking.HikingRegion;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.trip.TripStatus;
import ca.ulaval.glo4003.trip.dto.SearchTripDto;
import ca.ulaval.glo4003.trip.request.SearchTripRequest;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SearchTripRequestAssemblerTest {
  private static final double ANY_MONEY_VALUE = 604.f;
  private static final Money ANY_MONEY = new Money(ANY_MONEY_VALUE);
  private static final HikingRegion ANY_REGION = SAGLAC;
  private static final TripStatus ANY_TRIP_STATUS = AVAILABLE;
  private static final LocalDate ANY_START_DATE = LocalDate.now().minusDays(10);
  private static final LocalDate ANY_END_DATE = LocalDate.now();
  private static final SearchTripRequest ANY_SEARCH_TRIP_REQUEST =
      new SearchTripRequest(
          ANY_REGION.toString(),
          ANY_START_DATE.toString(),
          ANY_END_DATE.toString(),
          ANY_MONEY_VALUE,
          ANY_TRIP_STATUS.toString());
  private SearchTripRequestAssembler searchTripRequestAssembler;

  @BeforeEach
  void setUp() {
    this.searchTripRequestAssembler = new SearchTripRequestAssembler();
  }

  @Test
  void whenAssembling_thenSearchTripDtoIsAssembledCorrectly() {
    SearchTripDto result = this.searchTripRequestAssembler.assemble(ANY_SEARCH_TRIP_REQUEST);

    assertEquals(ANY_MONEY, result.priceLimit());
    assertEquals(ANY_START_DATE, result.startDate());
    assertEquals(ANY_END_DATE, result.endDate());
    assertEquals(ANY_TRIP_STATUS, result.status());
    assertTrue(result.region().isPresent());
    assertEquals(ANY_REGION, result.region().get());
  }

  @Test
  void givenRegionIsNull_whenAssembling_thenRegionInDtoIsEmpty() {
    SearchTripRequest nullRegionRequest =
        new SearchTripRequest(
            null,
            ANY_START_DATE.toString(),
            ANY_END_DATE.toString(),
            ANY_MONEY_VALUE,
            ANY_TRIP_STATUS.toString());
    SearchTripDto result = this.searchTripRequestAssembler.assemble(nullRegionRequest);

    assertTrue(result.region().isEmpty());
  }
}
