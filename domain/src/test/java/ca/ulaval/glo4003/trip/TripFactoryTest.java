package ca.ulaval.glo4003.trip;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.hiking.Hiking;
import ca.ulaval.glo4003.transport.TransportCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TripFactoryTest {
  private Hiking hikingMock;
  private TripConfig tripConfigMock;
  private TransportCollection transportCollectionMock;
  private TripFactory tripFactory;

  @BeforeEach
  void setUp() {
    this.transportCollectionMock = mock(TransportCollection.class);
    this.hikingMock = mock(Hiking.class);
    this.tripConfigMock = mock(TripConfig.class);
    this.tripFactory = new TripFactory(this.tripConfigMock);
  }

  @Test
  void whenCreatingTrip_thenGetMaxSeatsFromConfig() {
    this.tripFactory.createTrip(this.hikingMock, this.transportCollectionMock);

    verify(this.tripConfigMock).getMaximumSeats();
  }

  @Test
  void whenCreatingTrip_thenGetDefaultStartDateFromConfig() {
    this.tripFactory.createTrip(this.hikingMock, this.transportCollectionMock);

    verify(this.tripConfigMock).getDefaultStartDate();
  }

  @Test
  void whenCreatingTrip_thenGetDefaultTripCostFromConfig() {
    this.tripFactory.createTrip(this.hikingMock, this.transportCollectionMock);

    verify(this.tripConfigMock).getDefaultTripCost();
  }

  @Test
  void givenMoreThanOnce_whenCreatingTrip_thenAllIDsAreDifferent() {
    Trip first = this.tripFactory.createTrip(this.hikingMock, this.transportCollectionMock);
    Trip second = this.tripFactory.createTrip(this.hikingMock, this.transportCollectionMock);

    assertNotEquals(first.getId(), second.getId());
  }
}
