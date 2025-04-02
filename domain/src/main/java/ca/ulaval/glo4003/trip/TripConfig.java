package ca.ulaval.glo4003.trip;

import ca.ulaval.glo4003.money.Money;
import java.time.Duration;
import java.time.LocalDate;

public class TripConfig {
  private final int maximumSeats;
  private final Money defaultTripCost;
  private final long timeInSecondsBeforeTripToBlock;
  private final long notificationThresholdInSeconds;
  private final LocalDate defaultStartDate;

  public TripConfig(
      int maximumSeats,
      Money defaultTripCost,
      long timeInSecondsBeforeTripToBlock,
      long notificationThresholdInSeconds,
      LocalDate defaultStartDate) {
    this.maximumSeats = maximumSeats;
    this.defaultTripCost = defaultTripCost;
    this.timeInSecondsBeforeTripToBlock = timeInSecondsBeforeTripToBlock;
    this.notificationThresholdInSeconds = notificationThresholdInSeconds;
    this.defaultStartDate = defaultStartDate;
  }

  public int getMaximumSeats() {
    return this.maximumSeats;
  }

  public Money getDefaultTripCost() {
    return this.defaultTripCost;
  }

  public Duration getDurationBeforeBlockingTrip() {
    return Duration.ofSeconds(this.timeInSecondsBeforeTripToBlock);
  }

  public Duration getDurationBeforeNotifyingTravelers() {
    return Duration.ofSeconds(this.notificationThresholdInSeconds);
  }

  public LocalDate getDefaultStartDate() {
    return this.defaultStartDate;
  }
}
