package ca.ulaval.glo4003.transport;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.reservation.Reservation;
import ca.ulaval.glo4003.transport.exception.TransportFullyBookedException;
import ca.ulaval.glo4003.transport.exception.TransportNotAvailableException;
import ca.ulaval.glo4003.transport.organizedbus.OrganizedBus;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TransportCollectionTest {
  private static final String ANY_STOP = "anyStop";
  private static final ReservableTransport ANY_RESERVABLE_TRANSPORT_MOCK = mock(OrganizedBus.class);
  private static final TransportOption ANY_TRANSPORT_OPTION_MOCK = mock(TransportOption.class);
  private static final TransportType ANY_RESERVABLE_TRANSPORT_TYPE = TransportType.ORGANIZED_BUS;
  private static final Money ZERO_MONEY = new Money();
  private Reservation reservationMock;
  private TransportCollectionFactory transportCollectionFactory;
  private TransportCollection transportCollection;

  @BeforeEach
  void setUp() {
    this.reservationMock = mock(Reservation.class);
    this.transportCollectionFactory = new TransportCollectionFactory();
    this.transportCollection = this.transportCollectionFactory.create();
    when(this.reservationMock.getTransportType()).thenReturn(ANY_RESERVABLE_TRANSPORT_TYPE);
    when(this.reservationMock.getTransportStop()).thenReturn(ANY_STOP);
    when(ANY_RESERVABLE_TRANSPORT_MOCK.isAvailable()).thenReturn(true);
    when(ANY_RESERVABLE_TRANSPORT_MOCK.getTransportOption()).thenReturn(ANY_TRANSPORT_OPTION_MOCK);
  }

  @Test
  void givenReservationForSelfTransport_whenReserving_thenSetTransportCostToZero() {
    when(this.reservationMock.getTransportType()).thenReturn(TransportType.NONE);

    this.transportCollection.reserve(this.reservationMock);

    verify(this.reservationMock).setTransportCost(ZERO_MONEY);
  }

  @Test
  void givenTransportChoiceIsEmpty_whenReserving_thenThrowsNotAvailableException() {
    assertThrows(
        TransportNotAvailableException.class,
        () -> this.transportCollection.reserve(this.reservationMock));
  }

  @Test
  void
      givenTransportChoiceIsNotEmptyAndNotAvailable_whenReserving_thenThrowsTransportIsFullyBookedException() {
    this.givenChoiceNotEmpty();
    when(ANY_RESERVABLE_TRANSPORT_MOCK.isAvailable()).thenReturn(false);

    assertThrows(
        TransportFullyBookedException.class,
        () -> this.transportCollection.reserve(this.reservationMock));
  }

  @Test
  void givenTransportChoiceIsNotEmptyAndAvailable_whenReserving_thenReserveTransport() {
    this.givenChoiceNotEmpty();

    this.transportCollection.reserve(this.reservationMock);

    verify(ANY_RESERVABLE_TRANSPORT_MOCK).reserveSeat(ANY_STOP, this.reservationMock);
  }

  @Test
  void whenCanceling_thenCancelAllTransportAvailableForReservation() {
    this.givenChoiceNotEmpty();
    this.transportCollection.cancelSeat(this.reservationMock);

    verify(ANY_RESERVABLE_TRANSPORT_MOCK).cancelSeat(this.reservationMock);
  }

  @Test
  void givenNoTransportAddedYet_whenGettingAllTransport_thenThrowTransportNotAvailableException() {
    assertThrows(
        TransportNotAvailableException.class,
        () -> this.transportCollection.getTransportsOfType(ANY_RESERVABLE_TRANSPORT_TYPE));
  }

  @Test
  void whenAddingTransport_thenTransportIsAddedToCorrectList() {
    int expectedSizeValueAfterAdding = 1;

    this.transportCollection.addTransport(
        ANY_RESERVABLE_TRANSPORT_TYPE, ANY_RESERVABLE_TRANSPORT_MOCK);
    int result = this.transportCollection.getTransportsOfType(ANY_RESERVABLE_TRANSPORT_TYPE).size();

    assertEquals(expectedSizeValueAfterAdding, result);
  }

  @Test
  void whenGettingCurrentTransportOptions_thenGetTransportOptions() {
    int expectedSize = 1;
    this.givenChoiceNotEmpty();

    List<TransportOption> result = this.transportCollection.readCurrentTransportOptions();

    assertEquals(expectedSize, result.size());
  }

  private void givenChoiceNotEmpty() {
    this.transportCollection.addTransport(
        ANY_RESERVABLE_TRANSPORT_TYPE, ANY_RESERVABLE_TRANSPORT_MOCK);
  }
}
