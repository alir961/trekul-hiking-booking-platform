package ca.ulaval.glo4003.trip;

import static ca.ulaval.glo4003.trip.TripStatus.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.group.Group;
import ca.ulaval.glo4003.hiking.Hiking;
import ca.ulaval.glo4003.hosting.Hosting;
import ca.ulaval.glo4003.hosting.reservation.exception.HostingReservationExistingForTripException;
import ca.ulaval.glo4003.invoice.BillableItems;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.reservation.Reservation;
import ca.ulaval.glo4003.transport.TransportCollection;
import ca.ulaval.glo4003.transport.TransportType;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.user.User;
import ca.ulaval.glo4003.trip.exception.TripAlreadyContainsTravelerException;
import ca.ulaval.glo4003.trip.exception.TripNotAvailableException;
import ca.ulaval.glo4003.trip.exception.TripNotEnoughSeatsException;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TripTest {
  private static final TransportType ANY_TRANSPORT_TYPE = TransportType.ORGANIZED_BUS;
  private static final TripID A_TRIP_ID = TripID.generate();
  private static final Hiking A_HIKING_DUMMY = mock(Hiking.class);
  private static final int A_MAXIMUM_SEATS = 371;
  private static final int ONE_SEAT = 1;
  private static final int A_DIFFERENT_MAXIMUM_SEATS = 1204;
  private static final Money A_MONEY = new Money("24.5");
  private static final TripStatus INITIAL_TRIP_STATUS = AVAILABLE;
  private static final int ANY_SEATS_AVAILABLE = 1;
  private static final int ANY_SEATS_AVAILABLE_FOR_GROUP = 5;
  private static final LocalDate A_DATE = LocalDate.now();
  private static final TravelerID ANY_USER_ID = new TravelerID("24g9ug2");
  private static final int TOTAL_TRAVELERS = 2;
  private static final Money ANY_HOSTING_COST = new Money(452.);
  private TransportCollection anyTransportCollectionMock;
  private Reservation anyReservationMock;
  private Group groupMock;
  private Hosting hostingMock;
  private TripTravelerFetcher tripTravelerFetcherMock;
  private User userMock;
  private Trip anyTrip;

  @BeforeEach
  void setUp() {
    this.anyTransportCollectionMock = mock(TransportCollection.class);
    this.anyReservationMock = mock(Reservation.class);
    this.anyTrip =
        new Trip(
            A_TRIP_ID,
            A_MAXIMUM_SEATS,
            A_DATE,
            A_HIKING_DUMMY,
            A_MONEY,
            INITIAL_TRIP_STATUS,
            this.anyTransportCollectionMock);
    this.groupMock = mock(Group.class);
    this.hostingMock = mock(Hosting.class);
    this.tripTravelerFetcherMock = mock(TripTravelerFetcher.class);
    this.userMock = mock(User.class);
    when(this.anyReservationMock.getGroup()).thenReturn(this.groupMock);
  }

  @Test
  void givenSameObject_whenCallingEquals_thenObjectIsEqual() {
    assertEquals(this.anyTrip, this.anyTrip);
  }

  @Test
  void givenAnotherObjectWithSamePropertiesButDifferentId_whenCallingEquals_thenObjectIsNotEqual() {
    TripID differentID = TripID.generate();
    Trip sameTripDifferentID =
        new Trip(
            differentID,
            A_MAXIMUM_SEATS,
            A_DATE,
            A_HIKING_DUMMY,
            A_MONEY,
            INITIAL_TRIP_STATUS,
            this.anyTransportCollectionMock);

    assertNotEquals(this.anyTrip, sameTripDifferentID);
  }

  @Test
  void givenAnotherObjectWithDifferentPropertiesButSameId_whenCallingEquals_thenObjectIsEqual() {
    Hiking differentHikingDummy = mock(Hiking.class);
    LocalDate differentDate = LocalDate.now().minusDays(1);
    Money differentMoney = A_MONEY.add(new Money("10"));
    TripStatus differentInitialStatus = BLOCKED;
    Trip differentTripSameID =
        new Trip(
            A_TRIP_ID,
            A_DIFFERENT_MAXIMUM_SEATS,
            differentDate,
            differentHikingDummy,
            differentMoney,
            differentInitialStatus,
            this.anyTransportCollectionMock);

    assertEquals(this.anyTrip, differentTripSameID);
  }

  @Test
  void whenCopying_thenCopyIsEqualToOriginal() {
    Trip copied = this.anyTrip.copy();

    assertEquals(this.anyTrip, copied);
  }

  @Test
  void whenCopying_thenCopyIsNotSameInstanceThanOriginal() {
    Trip copied = this.anyTrip.copy();

    assertNotSame(this.anyTrip, copied);
  }

  @Test
  void givenTripDateBetweenStartAndEnd_whenCallingIsBetween_thenReturnTrue() {
    final int dateToAddStart = 1;
    final int dateToAddEnd = 1;
    LocalDate start = A_DATE.minusDays(dateToAddStart);
    LocalDate end = A_DATE.plusDays(dateToAddEnd);

    assertTrue(this.anyTrip.isBetweenDates(start, end));
  }

  @Test
  void givenTripDateNotBetweenStartAndEnd_whenCallingIsBetween_thenReturnFalse() {
    final int dateToAddStart = 1;
    final int dateToAddEnd = 5;
    LocalDate start = A_DATE.plusDays(dateToAddStart);
    LocalDate end = A_DATE.plusDays(dateToAddEnd);

    assertFalse(this.anyTrip.isBetweenDates(start, end));
  }

  @Test
  void whenReleaseSeats_thenAvailableSeatsIncreaseByGroupSize() {
    when(this.groupMock.getUserInCharge()).thenReturn(ANY_USER_ID);
    when(this.groupMock.calculateTotalTravelers()).thenReturn(ANY_SEATS_AVAILABLE);
    this.anyTrip.reserveSeat(this.anyReservationMock);
    int initialSeats = this.anyTrip.getAvailableSeats();

    this.anyTrip.releaseSeat(this.anyReservationMock);

    int expectedSeatsAvailable = initialSeats + ANY_SEATS_AVAILABLE;

    assertEquals(expectedSeatsAvailable, this.anyTrip.getAvailableSeats());
  }

  @Test
  void givenTripPreviouslyFull_whenReleaseSeats_thenTripIsNowAvailable() {
    when(this.groupMock.getUserInCharge()).thenReturn(ANY_USER_ID);
    int oneMaxSeats = 1;
    Trip tripWithOneSeat =
        new Trip(
            A_TRIP_ID,
            oneMaxSeats,
            A_DATE,
            A_HIKING_DUMMY,
            A_MONEY,
            AVAILABLE,
            this.anyTransportCollectionMock);
    tripWithOneSeat.reserveSeat(this.anyReservationMock);

    tripWithOneSeat.releaseSeat(this.anyReservationMock);

    assertTrue(tripWithOneSeat.hasStatus(AVAILABLE));
  }

  @Test
  void givenATripWithEnoughAvailableSeats_whenReservingSeat_thenSeatIsReservedForGroup() {
    when(this.groupMock.calculateTotalTravelers()).thenReturn(TOTAL_TRAVELERS);
    when(this.groupMock.getUserInCharge()).thenReturn(ANY_USER_ID);
    Trip anyTrip =
        new Trip(
            A_TRIP_ID,
            ANY_SEATS_AVAILABLE_FOR_GROUP,
            A_DATE,
            A_HIKING_DUMMY,
            A_MONEY,
            INITIAL_TRIP_STATUS,
            this.anyTransportCollectionMock);

    anyTrip.reserveSeat(this.anyReservationMock);

    assertEquals(ANY_SEATS_AVAILABLE_FOR_GROUP - TOTAL_TRAVELERS, anyTrip.getAvailableSeats());
    assertTrue(anyTrip.retrieveGroups().contains(this.groupMock));
  }

  @Test
  void
      givenATripWithoutEnoughAvailableSeats_whenReservingSeat_thenThrowTripNotEnoughSeatsException() {
    when(this.groupMock.calculateTotalTravelers()).thenReturn(TOTAL_TRAVELERS);
    when(this.groupMock.getUserInCharge()).thenReturn(ANY_USER_ID);
    Trip tripWithoutEnoughSeats =
        new Trip(
            A_TRIP_ID,
            ONE_SEAT,
            A_DATE,
            A_HIKING_DUMMY,
            A_MONEY,
            INITIAL_TRIP_STATUS,
            this.anyTransportCollectionMock);

    assertThrows(
        TripNotEnoughSeatsException.class,
        () -> tripWithoutEnoughSeats.reserveSeat(this.anyReservationMock));
    assertFalse(tripWithoutEnoughSeats.retrieveGroups().contains(this.groupMock));
    assertEquals(ONE_SEAT, tripWithoutEnoughSeats.getAvailableSeats());
  }

  @Test
  void givenTripBlocked_whenReservingSeat_thenThrowTripNotAvailableException() {
    when(this.groupMock.calculateTotalTravelers()).thenReturn(ONE_SEAT);
    when(this.groupMock.getUserInCharge()).thenReturn(ANY_USER_ID);
    this.anyTrip.blockTrip();

    assertThrows(
        TripNotAvailableException.class, () -> this.anyTrip.reserveSeat(this.anyReservationMock));
    assertFalse(this.anyTrip.retrieveGroups().contains(this.groupMock));
    assertEquals(A_MAXIMUM_SEATS, this.anyTrip.getAvailableSeats());
  }

  @Test
  void givenTripIsFull_whenReservingSeat_thenThrowTripNotAvailableException() {
    when(this.groupMock.calculateTotalTravelers()).thenReturn(TOTAL_TRAVELERS);
    when(this.groupMock.getUserInCharge()).thenReturn(ANY_USER_ID);
    int numberOfSeatsLeft = 0;
    Trip tripFull =
        new Trip(
            A_TRIP_ID,
            numberOfSeatsLeft,
            A_DATE,
            A_HIKING_DUMMY,
            A_MONEY,
            FULL,
            this.anyTransportCollectionMock);

    assertThrows(
        TripNotAvailableException.class, () -> tripFull.reserveSeat(this.anyReservationMock));
    assertFalse(tripFull.retrieveGroups().contains(this.groupMock));
    assertEquals(numberOfSeatsLeft, tripFull.getAvailableSeats());
  }

  @Test
  void
      givenTripContainsSomeTravelersInNewGroup_whenReservingSeat_thenThrowTripAlreadyContainsTravelerException() {
    Group newGroupMock = mock(Group.class);
    Reservation newReservationMock = mock(Reservation.class);
    when(newReservationMock.getGroup()).thenReturn(newGroupMock);
    when(newGroupMock.isSharingTravelers(this.groupMock)).thenReturn(true);
    when(newGroupMock.calculateTotalTravelers()).thenReturn(ANY_SEATS_AVAILABLE);

    this.anyTrip.reserveSeat(newReservationMock);

    assertThrows(
        TripAlreadyContainsTravelerException.class,
        () -> this.anyTrip.reserveSeat(this.anyReservationMock));
  }

  @Test
  void givenATripWithJustEnoughSeats_whenReservingSeat_thenTripIsMarkedAsFull() {
    when(this.groupMock.calculateTotalTravelers()).thenReturn(ONE_SEAT);
    when(this.groupMock.getUserInCharge()).thenReturn(ANY_USER_ID);
    Trip tripWithJustEnoughSeats =
        new Trip(
            A_TRIP_ID,
            ONE_SEAT,
            A_DATE,
            A_HIKING_DUMMY,
            A_MONEY,
            INITIAL_TRIP_STATUS,
            this.anyTransportCollectionMock);

    tripWithJustEnoughSeats.reserveSeat(this.anyReservationMock);

    assertTrue(tripWithJustEnoughSeats.hasStatus(FULL));
  }

  @Test
  void givenTripBlocked_whenCallingIsBlocked_thenTripIsBlocked() {
    this.anyTrip.blockTrip();

    boolean result = this.anyTrip.isBlocked();

    assertTrue(result);
  }

  @Test
  void givenTripNotBlocked_whenCallingIsBlocked_thenTripIsNotBlocked() {
    boolean result = this.anyTrip.isBlocked();

    assertFalse(result);
  }

  @Test
  void whenGettingRegion_thenGetsRegionOfHiking() {
    this.anyTrip.getRegion();

    verify(A_HIKING_DUMMY).getRegion();
  }

  @Test
  void givenDateBeforeTrip_whenCallingIsBeforeDate_thenDateOfTripIsNotBefore() {
    LocalDate dateBeforetrip = A_DATE.minusDays(1);

    boolean result = this.anyTrip.isBeforeDate(dateBeforetrip);

    assertFalse(result);
  }

  @Test
  void givenAfterBeforeTrip_whenCallingIsBeforeDate_thenDateOfTripIsBefore() {
    LocalDate dateAftertrip = A_DATE.plusDays(1);

    boolean result = this.anyTrip.isBeforeDate(dateAftertrip);

    assertTrue(result);
  }

  @Test
  void givenTripPreviouslyFull_whenReleaseSeats_thenTransportCollectionCancelsSeats() {
    when(this.groupMock.getUserInCharge()).thenReturn(ANY_USER_ID);
    int oneMaxSeats = 1;
    Trip tripWithOneSeat =
        new Trip(
            A_TRIP_ID,
            oneMaxSeats,
            A_DATE,
            A_HIKING_DUMMY,
            A_MONEY,
            AVAILABLE,
            this.anyTransportCollectionMock);
    tripWithOneSeat.reserveSeat(this.anyReservationMock);

    tripWithOneSeat.releaseSeat(this.anyReservationMock);

    verify(this.anyTransportCollectionMock).cancelSeat(this.anyReservationMock);
  }

  @Test
  void givenATripWithJustEnoughSeats_whenReservingSeat_thenTransportCollectionReservesSeats() {
    when(this.groupMock.calculateTotalTravelers()).thenReturn(ONE_SEAT);
    when(this.groupMock.getUserInCharge()).thenReturn(ANY_USER_ID);
    Trip tripWithJustEnoughSeats =
        new Trip(
            A_TRIP_ID,
            ONE_SEAT,
            A_DATE,
            A_HIKING_DUMMY,
            A_MONEY,
            INITIAL_TRIP_STATUS,
            this.anyTransportCollectionMock);

    tripWithJustEnoughSeats.reserveSeat(this.anyReservationMock);

    verify(this.anyTransportCollectionMock).reserve(this.anyReservationMock);
  }

  @Test
  void whenGettingTransportOptions_thenGetsTransportOptionsFromTransportCollection() {
    this.anyTrip.readCurrentTransportOptions();

    verify(this.anyTransportCollectionMock).readCurrentTransportOptions();
  }

  @Test
  void whenGettingAllReservableTransport_thenGetAllTransportFromTransportCollection() {
    this.anyTrip.getReservableTransports(ANY_TRANSPORT_TYPE);

    verify(this.anyTransportCollectionMock).getTransportsOfType(ANY_TRANSPORT_TYPE);
  }

  @Test
  void
      givenTripWithExistingReservation_whenReservingHosting_thenHostingReservationExistingForTripExceptionThrown() {
    this.anyTrip.reserveHosting(this.hostingMock, this.tripTravelerFetcherMock);

    assertThrows(
        HostingReservationExistingForTripException.class,
        () -> this.anyTrip.reserveHosting(this.hostingMock, this.tripTravelerFetcherMock));
  }

  @Test
  void givenTripWithNoReservation_whenReservingHosting_thenTripTravelerFetcherFetchersUsers() {
    this.anyTrip.reserveHosting(this.hostingMock, this.tripTravelerFetcherMock);

    verify(this.tripTravelerFetcherMock).fetchUsers(this.anyTrip);
  }

  @Test
  void givenTripWithNoReservation_whenReservingHosting_thenFetchedUsersSaveItemsForTrip() {
    when(this.tripTravelerFetcherMock.fetchUsers(this.anyTrip)).thenReturn(List.of(this.userMock));
    when(this.hostingMock.getMinimalCost()).thenReturn(ANY_HOSTING_COST);

    this.anyTrip.reserveHosting(this.hostingMock, this.tripTravelerFetcherMock);

    verify(this.userMock).saveItemForTrip(A_TRIP_ID, BillableItems.HOSTING, ANY_HOSTING_COST);
  }

  @Test
  void givenTripWithNoReservation_whenReservingHosting_thenHostingReservesHost() {
    when(this.tripTravelerFetcherMock.fetchUsers(this.anyTrip)).thenReturn(List.of(this.userMock));
    when(this.hostingMock.getMinimalCost()).thenReturn(ANY_HOSTING_COST);

    this.anyTrip.reserveHosting(this.hostingMock, this.tripTravelerFetcherMock);

    verify(this.hostingMock).reserveHost(this.anyTrip.getReservedSeats());
  }

  @Test
  void givenTripWithNoReservation_whenReservingHosting_thenTripHasReservation() {
    when(this.tripTravelerFetcherMock.fetchUsers(this.anyTrip)).thenReturn(List.of(this.userMock));
    when(this.hostingMock.getMinimalCost()).thenReturn(ANY_HOSTING_COST);

    this.anyTrip.reserveHosting(this.hostingMock, this.tripTravelerFetcherMock);

    assertTrue(this.anyTrip.hasHostingReservation());
  }
}
