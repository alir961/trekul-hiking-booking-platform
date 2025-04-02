package ca.ulaval.glo4003.transport.organizedbus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.group.Group;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.reservation.Reservation;
import ca.ulaval.glo4003.transport.TransportID;
import ca.ulaval.glo4003.transport.TransportType;
import ca.ulaval.glo4003.transport.exception.TransportFullyBookedException;
import ca.ulaval.glo4003.transport.exception.TransportStopDoesNotExistException;
import ca.ulaval.glo4003.transport.exception.TransportWithoutEnoughPlacesException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OrganizedBusTest {
  private static final int ANY_AVAILABLE_SEATS = 40;
  private static final int THREE_SEATS_AVAILABLE = 3;
  private static final Money ANY_ORGANIZED_BUS_COST = new Money("30.00");
  private static final String ANY_STOP_NAME = "Chez Diallo";
  private static final TransportID ANY_TRANSPORT_ID = TransportID.generate();
  private final ArrayList<TransportStop> busStops = this.createBusStops();
  private Reservation onGoingReservationMock;
  private Group groupMock;
  private OrganizedBus organizedBus;

  private ArrayList<TransportStop> createBusStops() {
    ArrayList<TransportStop> stops = new ArrayList<>();
    LocalDateTime anyDepartureTime = LocalDateTime.now();
    TransportStopLocation anyBusStopLocation = new TransportStopLocation(ANY_STOP_NAME);
    TransportStop firstBusStop = new TransportStop(anyBusStopLocation, anyDepartureTime);
    stops.add(firstBusStop);
    return stops;
  }

  @BeforeEach
  void setUp() {
    this.onGoingReservationMock = mock(Reservation.class);
    this.groupMock = mock(Group.class);
    this.organizedBus =
        new OrganizedBus(
            ANY_TRANSPORT_ID, ANY_AVAILABLE_SEATS, ANY_ORGANIZED_BUS_COST, this.busStops);
  }

  @Test
  void givenOrganizedBusFullyBooked_whenReserveSeat_thenThrowTransportFullyBookedException() {
    int noAvailableSeats = 0;
    this.organizedBus =
        new OrganizedBus(ANY_TRANSPORT_ID, noAvailableSeats, ANY_ORGANIZED_BUS_COST, this.busStops);

    assertThrows(
        TransportFullyBookedException.class,
        () -> this.organizedBus.reserveSeat(ANY_STOP_NAME, this.onGoingReservationMock));
  }

  @Test
  void
      givenOrganizedBusWithoutEnoughAvailablePlaces_whenReserveSeat_thenThrowTransportWithoutEnoughPlacesException() {
    when(this.onGoingReservationMock.calculateTotalTravelers()).thenReturn(ANY_AVAILABLE_SEATS + 1);
    this.organizedBus =
        new OrganizedBus(
            ANY_TRANSPORT_ID, ANY_AVAILABLE_SEATS, ANY_ORGANIZED_BUS_COST, this.busStops);

    assertThrows(
        TransportWithoutEnoughPlacesException.class,
        () -> this.organizedBus.reserveSeat(ANY_STOP_NAME, this.onGoingReservationMock));
  }

  @Test
  void givenBusStopDoesNotExist_whenReserveSeat_thenThrowTransportStopDoesNotExistException() {
    String stopNameNotInBusStops = "Chez Olivier";

    assertThrows(
        TransportStopDoesNotExistException.class,
        () -> this.organizedBus.reserveSeat(stopNameNotInBusStops, this.onGoingReservationMock));
  }

  @Test
  void whenReserveSeat_thenAllTravelersInGroupHaveAReservedSeat() {
    when(this.onGoingReservationMock.getGroup()).thenReturn(this.groupMock);
    when(this.onGoingReservationMock.calculateTotalTravelers()).thenReturn(THREE_SEATS_AVAILABLE);

    this.organizedBus =
        new OrganizedBus(
            ANY_TRANSPORT_ID, THREE_SEATS_AVAILABLE, ANY_ORGANIZED_BUS_COST, this.busStops);

    this.organizedBus.reserveSeat(ANY_STOP_NAME, this.onGoingReservationMock);

    assertThrows(
        TransportFullyBookedException.class,
        () -> this.organizedBus.reserveSeat(ANY_STOP_NAME, this.onGoingReservationMock));
  }

  @Test
  void whenReserveSeat_thenAddTransportCost() {
    when(this.onGoingReservationMock.getGroup()).thenReturn(this.groupMock);
    TransportType organizedBusType = TransportType.ORGANIZED_BUS;
    this.organizedBus.reserveSeat(ANY_STOP_NAME, this.onGoingReservationMock);

    verify(this.onGoingReservationMock).setTransportCost(ANY_ORGANIZED_BUS_COST);
  }

  @Test
  void whenCancelSeat_thenAllReservedSeatsForReservationAreNowAvailable() {
    when(this.onGoingReservationMock.calculateTotalTravelers()).thenReturn(THREE_SEATS_AVAILABLE);
    when(this.onGoingReservationMock.getGroup()).thenReturn(this.groupMock);
    this.organizedBus =
        new OrganizedBus(
            ANY_TRANSPORT_ID, THREE_SEATS_AVAILABLE, ANY_ORGANIZED_BUS_COST, this.busStops);

    this.organizedBus.reserveSeat(ANY_STOP_NAME, this.onGoingReservationMock);
    this.organizedBus.cancelSeat(this.onGoingReservationMock);

    assertDoesNotThrow(
        () -> this.organizedBus.reserveSeat(ANY_STOP_NAME, this.onGoingReservationMock));
  }

  @Test
  void whenCopyOrganizedBus_thenObjectIsDeepCopied() {
    OrganizedBus copiedOrganizedBus = this.organizedBus.copy();

    assertEquals(copiedOrganizedBus, this.organizedBus);
  }
}
