package ca.ulaval.glo4003.trip.usecase;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.trip.TripBlockingService;
import ca.ulaval.glo4003.trip.TripID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BlockTripUseCaseTest {
  private static final TripID ANY_TRIP_ID = TripID.generate();
  private TripBlockingService tripBlockingServiceMock;
  private BlockTripUseCase blockTripUseCase;

  @BeforeEach
  void setUp() {
    this.tripBlockingServiceMock = mock(TripBlockingService.class);
    this.blockTripUseCase = new BlockTripUseCase(this.tripBlockingServiceMock);
  }

  @Test
  void givenAuthenticatedUser_whenBlockingTrip_thenBlockTrip() {
    this.blockTripUseCase.blockTrip(ANY_TRIP_ID);

    verify(this.tripBlockingServiceMock).blockTrip(ANY_TRIP_ID);
  }
}
