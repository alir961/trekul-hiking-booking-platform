package ca.ulaval.glo4003.reservation;

import ca.ulaval.glo4003.trip.exception.TripDatePassedException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

public class DurationCancellationPenalty {
  private static final int HOURS_IN_A_DAY = 24;
  private static final int DAYS_IN_ONE_WEEK = 7;
  private static final int FIRST_THRESHOLD_IN_HOURS = DAYS_IN_ONE_WEEK * HOURS_IN_A_DAY * 3;
  private static final int SECOND_THRESHOLD_IN_HOURS = DAYS_IN_ONE_WEEK * HOURS_IN_A_DAY;
  private static final int THIRD_THRESHOLD_IN_HOURS = HOURS_IN_A_DAY;
  private static final int LAST_THRESHOLD_IN_HOURS = 0;

  private final List<Integer> penaltyThresholds =
      List.of(
          FIRST_THRESHOLD_IN_HOURS,
          SECOND_THRESHOLD_IN_HOURS,
          THIRD_THRESHOLD_IN_HOURS,
          LAST_THRESHOLD_IN_HOURS);
  private final Map<Integer, Double> penaltyAmounts =
      Map.of(
          FIRST_THRESHOLD_IN_HOURS,
          0.0,
          SECOND_THRESHOLD_IN_HOURS,
          0.25,
          THIRD_THRESHOLD_IN_HOURS,
          0.35,
          LAST_THRESHOLD_IN_HOURS,
          0.50);

  public Double calculateCancellationPenalty(LocalDateTime tripStartOfDayDate) {
    LocalDateTime now = LocalDateTime.now();
    long hoursBetween = ChronoUnit.HOURS.between(now, tripStartOfDayDate);

    for (Integer threshold : this.penaltyThresholds) {
      if (hoursBetween >= threshold) {
        return this.penaltyAmounts.get(threshold);
      }
    }

    throw new TripDatePassedException();
  }
}
