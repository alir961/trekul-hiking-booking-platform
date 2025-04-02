package ca.ulaval.glo4003.trip;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ca.ulaval.glo4003.money.Money;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TripConfigTest {
  private static final int ANY_MAXIMUM_SEAT = 5;
  private static final Money ANY_DEFAULT_COST = new Money(5.0);
  private static final long ANY_TIME_BEFORE_BLOCK = 5;
  private static final LocalDate ANY_DATE = LocalDate.of(2020, 1, 1);
  private static final long ANY_NOTIFICATION_THRESHOLD = 5;
  private TripConfig tripConfig;

  @BeforeEach
  void setUp() {
    this.tripConfig =
        new TripConfig(
            ANY_MAXIMUM_SEAT,
            ANY_DEFAULT_COST,
            ANY_TIME_BEFORE_BLOCK,
            ANY_NOTIFICATION_THRESHOLD,
            ANY_DATE);
  }

  @Test
  void whenGettingMaximumSeats_thenCorrectAmountIsReturned() {
    int receivedAmount = this.tripConfig.getMaximumSeats();

    assertEquals(ANY_MAXIMUM_SEAT, receivedAmount);
  }

  @Test
  void whenGettingDefaultTripCost_thenCorrectCostIsReturned() {
    Money receivedCost = this.tripConfig.getDefaultTripCost();

    assertEquals(ANY_DEFAULT_COST, receivedCost);
  }

  @Test
  void whenGettingDefaultStartDate_thenCorrectDateIsReturned() {
    LocalDate receivedDate = this.tripConfig.getDefaultStartDate();

    assertEquals(ANY_DATE, receivedDate);
  }
}
