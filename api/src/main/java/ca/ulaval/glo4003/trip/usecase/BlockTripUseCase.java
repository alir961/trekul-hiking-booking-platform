package ca.ulaval.glo4003.trip.usecase;

import ca.ulaval.glo4003.trip.TripBlockingService;
import ca.ulaval.glo4003.trip.TripID;
import jakarta.inject.Inject;

public class BlockTripUseCase {
  private final TripBlockingService tripBlockingService;

  @Inject
  public BlockTripUseCase(TripBlockingService tripBlockingService) {
    this.tripBlockingService = tripBlockingService;
  }

  public void blockTrip(TripID tripID) {
    this.tripBlockingService.blockTrip(tripID);
  }
}
