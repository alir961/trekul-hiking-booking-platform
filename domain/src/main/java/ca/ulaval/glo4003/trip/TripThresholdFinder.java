package ca.ulaval.glo4003.trip;

import ca.ulaval.glo4003.clock.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

public class TripThresholdFinder {
  private final Clock clock;
  private final TripRepository tripRepository;
  private final Duration thresholdInSeconds;

  public TripThresholdFinder(
      Clock clock, TripRepository tripRepository, Duration thresholdInSeconds) {
    this.clock = clock;
    this.tripRepository = tripRepository;
    this.thresholdInSeconds = thresholdInSeconds;
  }

  public List<Trip> find() {
    LocalDate startDate = this.clock.today();
    LocalDate notificationDay = startDate.plusDays(this.thresholdInSeconds.toDays());
    return this.tripRepository.findBetweenDates(startDate, notificationDay);
  }
}
