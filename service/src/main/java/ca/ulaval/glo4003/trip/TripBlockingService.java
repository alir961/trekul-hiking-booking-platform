package ca.ulaval.glo4003.trip;

import ca.ulaval.glo4003.trip.exception.TripNotFoundException;
import java.time.Duration;
import java.time.LocalDate;

public class TripBlockingService {
  private final TripRepository tripRepository;
  private final Duration durationBeforeBlockingTrip;

  public TripBlockingService(TripRepository tripRepository, Duration durationBeforeBlockingTrip) {
    this.tripRepository = tripRepository;
    this.durationBeforeBlockingTrip = durationBeforeBlockingTrip;
  }

  public void blockUpcomingTrips() {
    LocalDate yesterday = LocalDate.now().minusDays(1);
    LocalDate blockingDay = LocalDate.now().plusDays(this.durationBeforeBlockingTrip.toDays());
    this.tripRepository.findBetweenDates(yesterday, blockingDay).forEach(this::blockAndSaveTrip);
  }

  public void blockTrip(TripID tripId) {
    Trip trip = this.tripRepository.get(tripId).orElseThrow(TripNotFoundException::new);
    this.blockAndSaveTrip(trip);
  }

  private void blockAndSaveTrip(Trip trip) {
    trip.blockTrip();
    this.tripRepository.save(trip);
  }
}
