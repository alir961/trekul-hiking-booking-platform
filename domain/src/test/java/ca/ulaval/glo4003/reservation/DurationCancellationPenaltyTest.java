package ca.ulaval.glo4003.reservation;

import static org.junit.jupiter.api.Assertions.*;

import ca.ulaval.glo4003.trip.exception.TripDatePassedException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class DurationCancellationPenaltyTest {
  private static final int ONE_MORE_DAY = 1;
  private static final int SEVEN_MORE_DAYS = 7;
  private static final int TWENTY_ONE_MORE_DAYS = 21;
  private static final int TWENTY_TWO_MORE_DAYS = 22;
  private static final double HALF_PENALTY = 0.5;
  private static final double THIRTY_FIVE_PERCENT_PENALTY = 0.35;
  private static final double TWENTY_FIVE_PERCENT_PENALTY = 0.25;
  private static final double NO_PENALTY = 0.;

  private final DurationCancellationPenalty durationCancellationPenalty =
      new DurationCancellationPenalty();

  @Test
  void givenTripDateInLessThanADay_whenCalculateCancellationPenalty_thenReturnFiftyPercent() {
    LocalDateTime tripDateInLessThanADay = LocalDate.now().plusDays(ONE_MORE_DAY).atStartOfDay();
    Double penaltyPercentage =
        this.durationCancellationPenalty.calculateCancellationPenalty(tripDateInLessThanADay);

    assertEquals(HALF_PENALTY, penaltyPercentage);
  }

  @Test
  void
      givenTripDateInLessThanOneWeek_whenCalculateCancellationPenalty_thenReturnThirtyFivePercent() {
    LocalDateTime tripDateInLessThanAWeek =
        LocalDate.now().plusDays(SEVEN_MORE_DAYS).atStartOfDay();

    Double penaltyPercentage =
        this.durationCancellationPenalty.calculateCancellationPenalty(tripDateInLessThanAWeek);

    assertEquals(THIRTY_FIVE_PERCENT_PENALTY, penaltyPercentage);
  }

  @Test
  void
      givenTripDateInLessThanThreeWeeks_whenCalculateCancellationPenalty_thenReturnTwentyFivePercent() {
    LocalDateTime tripDateInLessThanThreeWeeks =
        LocalDate.now().plusDays(TWENTY_ONE_MORE_DAYS).atStartOfDay();

    Double penaltyPercentage =
        this.durationCancellationPenalty.calculateCancellationPenalty(tripDateInLessThanThreeWeeks);

    assertEquals(TWENTY_FIVE_PERCENT_PENALTY, penaltyPercentage);
  }

  @Test
  void givenTripDateInMoreThanThreeWeeks_whenCalculateCancellationPenalty_thenReturnZeroPercent() {
    LocalDateTime tripDateInMoreThanThreeWeeks =
        LocalDate.now().plusDays(TWENTY_TWO_MORE_DAYS).atStartOfDay();

    Double penaltyPercentage =
        this.durationCancellationPenalty.calculateCancellationPenalty(tripDateInMoreThanThreeWeeks);

    assertEquals(NO_PENALTY, penaltyPercentage);
  }

  @Test
  void givenTripHasPassed_whenCalculateCancellationPenalty_thenThrowTripDatePassedException() {
    LocalDateTime tripDateInThePast = LocalDateTime.now().minusDays(1);

    assertThrows(
        TripDatePassedException.class,
        () -> this.durationCancellationPenalty.calculateCancellationPenalty(tripDateInThePast));
  }
}
