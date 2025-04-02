package ca.ulaval.glo4003.etl.trip;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.inOrder;

import ca.ulaval.glo4003.hiking.Hiking;
import ca.ulaval.glo4003.transport.TransportCollection;
import ca.ulaval.glo4003.transport.TransportCollectionFactory;
import ca.ulaval.glo4003.transport.TransportType;
import ca.ulaval.glo4003.transport.organizedbus.OrganizedBus;
import ca.ulaval.glo4003.transport.organizedbus.OrganizedBusFactory;
import ca.ulaval.glo4003.transport.organizedbus.TransportStop;
import ca.ulaval.glo4003.trip.Trip;
import ca.ulaval.glo4003.trip.TripFactory;
import ca.ulaval.glo4003.trip.TripRepository;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

public class TripLoaderTest {
  private static final TransportType ORGANIZED_BUS_TYPE = TransportType.ORGANIZED_BUS;
  private static final Collection<TransportStop> ANY_BUS_STOPS = List.of(mock(TransportStop.class));
  private static final OrganizedBus ANY_BUS = mock(OrganizedBus.class);
  private static final Hiking ANY_HIKING = mock(Hiking.class);
  private TransportCollection transportCollectionMock;
  private TransportCollectionFactory transportCollectionFactoryMock;
  private OrganizedBusFactory organizedBusFactoryMock;
  private Trip tripMock;
  private TripRepository tripRepositoryMock;
  private TripFactory tripFactoryMock;
  private TripLoader tripLoader;

  @BeforeEach
  void setUp() {
    this.transportCollectionMock = mock(TransportCollection.class);
    this.transportCollectionFactoryMock = mock(TransportCollectionFactory.class);
    this.tripFactoryMock = mock(TripFactory.class);
    this.tripRepositoryMock = mock(TripRepository.class);
    this.organizedBusFactoryMock = mock(OrganizedBusFactory.class);
    this.tripLoader =
        new TripLoader(
            this.tripFactoryMock,
            this.tripRepositoryMock,
            this.organizedBusFactoryMock,
            this.transportCollectionFactoryMock);

    this.tripMock = this.mockTripFromHiking(ANY_HIKING);
    when(this.organizedBusFactoryMock.createOrganizedBus(ANY_BUS_STOPS)).thenReturn(ANY_BUS);
    when(this.transportCollectionFactoryMock.create()).thenReturn(this.transportCollectionMock);
  }

  @Test
  void givenMultipleHiking_whenLoading_thenSaveAllNewTrips() {
    Hiking anotherHikingMock = mock(Hiking.class);
    Trip anotherTripMock = this.mockTripFromHiking(anotherHikingMock);
    this.tripLoader.load(List.of(ANY_HIKING, anotherHikingMock), ANY_BUS_STOPS);

    verify(this.tripRepositoryMock).save(this.tripMock);
    verify(this.tripRepositoryMock).save(anotherTripMock);
  }

  @Test
  void whenLoading_thenCreateTransportCollectionAndAddBusBeforeSaving() {
    this.tripLoader.load(List.of(ANY_HIKING), ANY_BUS_STOPS);
    InOrder order =
        inOrder(
            this.tripMock,
            this.tripRepositoryMock,
            this.transportCollectionFactoryMock,
            this.transportCollectionMock);

    order.verify(this.transportCollectionFactoryMock).create();
    order.verify(this.transportCollectionMock).addTransport(ORGANIZED_BUS_TYPE, ANY_BUS);
    order.verify(this.tripRepositoryMock).save(this.tripMock);
  }

  private Trip mockTripFromHiking(Hiking hiking) {
    Trip newTripMock = mock(Trip.class);
    when(this.tripFactoryMock.createTrip(hiking, this.transportCollectionMock))
        .thenReturn(newTripMock);

    return newTripMock;
  }
}
