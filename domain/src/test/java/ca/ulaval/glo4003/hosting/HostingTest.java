package ca.ulaval.glo4003.hosting;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import ca.ulaval.glo4003.hiking.HikingRegion;
import ca.ulaval.glo4003.hosting.exception.HostingNoCapacityAvailableException;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.trip.Trip;
import java.time.LocalDate;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class HostingTest {
  private static final HostingID A_HOSTING_ID = new HostingID(1);
  private static final String A_HOSTING_NAME = "AUBERGE DU LAC-COMMANDANT";
  private static final HikingRegion A_REGION = HikingRegion.GASPESIE;
  private static final int TEST_YEAR_2024 = 2024;
  private static final int TEST_MONTH_OCTOBER = 10;
  private static final int TEST_MONTH_NOVEMBER = 11;
  private static final int TEST_DAY_30 = 30;
  private static final int TEST_DAY_15 = 15;
  private static final int TEST_DAY_20 = 20;
  private static final int RESERVED_PLACES = 2;
  private static final Map<LocalDate, LocalDate> AVAILABILITY =
      Map.of(
          LocalDate.of(TEST_YEAR_2024, TEST_MONTH_OCTOBER, 1),
          LocalDate.of(TEST_YEAR_2024, TEST_MONTH_OCTOBER, 31));
  private static final LocalDate COMPATIBLE_TRIP_DATE =
      LocalDate.of(TEST_YEAR_2024, TEST_MONTH_OCTOBER, TEST_DAY_15);
  private static final LocalDate INCOMPATIBLE_TRIP_DATE =
      LocalDate.of(TEST_YEAR_2024, TEST_MONTH_NOVEMBER, TEST_DAY_15);
  private static final Money MIN_PRICE = new Money("100");
  private static final Money MAX_PRICE = new Money("200");
  private static final Map<Money, Money> PRICE_RANGE = Map.of(MIN_PRICE, MAX_PRICE);
  private static final int A_MAX_CAPACITY = 5;
  private static final Hosting A_HOSTING =
      new Hosting(
          A_HOSTING_ID, A_HOSTING_NAME, A_REGION, PRICE_RANGE, A_MAX_CAPACITY, AVAILABILITY);
  private Trip trip;

  @BeforeEach
  void setUp() {
    this.trip = mock(Trip.class);
  }

  @Test
  void givenSameObject_whenCallingEquals_thenObjectIsEqual() {
    assertEquals(A_HOSTING, A_HOSTING);
  }

  @Test
  void givenAnotherObjectWithSamePropertiesButDifferentId_whenCallingEquals_thenObjectIsNotEqual() {
    HostingID differentID = new HostingID(2);
    Hosting sameHostDifferentID =
        new Hosting(
            differentID, A_HOSTING_NAME, A_REGION, PRICE_RANGE, A_MAX_CAPACITY, AVAILABILITY);

    assertNotEquals(A_HOSTING, sameHostDifferentID);
  }

  @Test
  void givenAnotherObjectWithDifferentPropertiesButSameId_whenCallingEquals_thenObjectIsEqual() {
    Map<LocalDate, LocalDate> differentAvailability =
        Map.of(
            LocalDate.of(TEST_YEAR_2024, TEST_MONTH_NOVEMBER, 1),
            LocalDate.of(TEST_YEAR_2024, TEST_MONTH_NOVEMBER, TEST_DAY_30));
    Hosting differentHostingSameID =
        new Hosting(
            A_HOSTING_ID,
            A_HOSTING_NAME,
            A_REGION,
            PRICE_RANGE,
            A_MAX_CAPACITY,
            differentAvailability);

    assertEquals(A_HOSTING, differentHostingSameID);
  }

  @Test
  void whenCopying_thenCopyIsEqualToOriginal() {
    Hosting copied = A_HOSTING.copy();

    assertEquals(A_HOSTING, copied);
  }

  @Test
  void whenCopying_thenCopyIsNotSameInstanceAsOriginal() {
    Hosting copied = A_HOSTING.copy();

    assertNotSame(A_HOSTING, copied);
  }

  @Test
  void givenHostingWithAvailableCapacity_whenReservingHost_thenCapacityIsReduced() {
    A_HOSTING.reserveHost(RESERVED_PLACES);

    assertEquals(A_MAX_CAPACITY - RESERVED_PLACES, A_HOSTING.getAvailableCapacity());
  }

  @Test
  void givenHostingWithoutCapacity_whenReservingHost_thenThrowHostNoCapacityAvailableException() {
    Hosting hostingWithoutCapacity =
        new Hosting(A_HOSTING_ID, A_HOSTING_NAME, A_REGION, PRICE_RANGE, 0, AVAILABILITY);

    assertThrows(
        HostingNoCapacityAvailableException.class,
        () -> hostingWithoutCapacity.reserveHost(RESERVED_PLACES));
  }

  @Test
  void givenHostingDuringAvailablePeriod_whenCheckingAvailability_thenReturnsTrue() {
    LocalDate startDate = LocalDate.of(TEST_YEAR_2024, TEST_MONTH_OCTOBER, TEST_DAY_15);
    LocalDate endDate = LocalDate.of(TEST_YEAR_2024, TEST_MONTH_OCTOBER, TEST_DAY_20);

    assertTrue(A_HOSTING.canBeReservedForDates(startDate, endDate));
  }

  @Test
  void givenHostingNotDuringAvailablePeriod_whenCheckingAvailability_thenReturnsTrue() {
    LocalDate startDate = LocalDate.of(TEST_YEAR_2024, TEST_MONTH_OCTOBER, TEST_DAY_15);
    LocalDate endDate = LocalDate.of(TEST_YEAR_2024, TEST_MONTH_NOVEMBER, TEST_DAY_20);

    assertFalse(A_HOSTING.canBeReservedForDates(startDate, endDate));
  }

  @Test
  void givenHostingWithPriceRange_whenCheckingPriceRange_thenReturnsTrue() {
    Money minPrice = new Money("150");
    Money maxPrice = new Money("180");

    assertTrue(A_HOSTING.isBetweenPriceRange(minPrice, maxPrice));
  }

  @Test
  void givenHostingWithoutPriceRange_whenCheckingPriceRange_thenReturnsFalse() {
    Money minPrice = new Money("250");
    Money maxPrice = new Money("300");

    assertFalse(A_HOSTING.isBetweenPriceRange(minPrice, maxPrice));
  }

  @Test
  void givenTripCompatibleWithHosting_whenCheckingIfCompatible_thenReturnTrue() {
    Mockito.when(this.trip.getDate()).thenReturn(COMPATIBLE_TRIP_DATE);
    Mockito.when(this.trip.getRegion()).thenReturn(A_REGION);
    Mockito.when(this.trip.getReservedSeats()).thenReturn(A_MAX_CAPACITY);

    boolean compatible = A_HOSTING.isCompatibleWithTrip(this.trip);

    assertTrue(compatible);
  }

  @Test
  void givenTripIncompatibleWithHosting_whenCheckingIfCompatible_thenReturnFalse() {
    Mockito.when(this.trip.getDate()).thenReturn(INCOMPATIBLE_TRIP_DATE);
    Mockito.when(this.trip.getRegion()).thenReturn(A_REGION);
    Mockito.when(this.trip.getReservedSeats()).thenReturn(A_MAX_CAPACITY);

    boolean compatible = A_HOSTING.isCompatibleWithTrip(this.trip);

    assertFalse(compatible);
  }
}
