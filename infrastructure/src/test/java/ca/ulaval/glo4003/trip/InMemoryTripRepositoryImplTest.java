package ca.ulaval.glo4003.trip;

import static ca.ulaval.glo4003.trip.TripStatus.AVAILABLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4003.group.Group;
import ca.ulaval.glo4003.hiking.Hiking;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.reservation.Reservation;
import ca.ulaval.glo4003.transport.TransportCollection;
import ca.ulaval.glo4003.traveler.PersonName;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.guest.Guest;
import java.time.LocalDate;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryTripRepositoryImplTest {
  private static final int ANY_MAXIMUM_SEATS = 30391;
  private static final TripID A_NON_EXISTING_TRIP_ID = TripID.generate();
  private static final TripID AN_EXISTING_TRIP_ID = TripID.generate();
  private static final TravelerID A_NON_EXISTING_TRAVELER_ID = new TravelerID("123456");
  private static final TravelerID AN_EXISTING_TRAVELER_ID = new TravelerID("7891011");
  private static final TravelerID AN_UNAUTHENTICATED_TRAVELER_ID = new TravelerID("415486");
  private static final Money ANY_MONEY = new Money("504.2");
  private static final Group A_GROUP = new Group(AN_EXISTING_TRAVELER_ID);
  private static final PersonName A_GUEST_NAME = new PersonName("guest", "doe");
  private static final Guest A_GUEST =
      new Guest(A_GUEST_NAME, AN_UNAUTHENTICATED_TRAVELER_ID, "guest@example.com");
  private Trip anyTrip;
  private TransportCollection transportCollectionMock;
  private Reservation anyReservationMock;
  private InMemoryTripRepositoryImpl tripRepository;

  @BeforeEach
  void setUp() {
    this.transportCollectionMock = mock(TransportCollection.class);
    this.anyReservationMock = mock(Reservation.class);
    this.tripRepository = new InMemoryTripRepositoryImpl();
    this.anyTrip =
        new Trip(
            AN_EXISTING_TRIP_ID,
            ANY_MAXIMUM_SEATS,
            LocalDate.now(),
            mock(Hiking.class),
            ANY_MONEY,
            AVAILABLE,
            this.transportCollectionMock);
    this.tripRepository.save(this.anyTrip);
    when(this.anyReservationMock.getGroup()).thenReturn(A_GROUP);
  }

  @Test
  void givenTripIsNotInRepository_whenGettingTheTrip_thenReceiveNoTrip() {
    Optional<Trip> result = this.tripRepository.get(A_NON_EXISTING_TRIP_ID);

    assertTrue(result.isEmpty());
  }

  @Test
  void givenTripIsInRepository_whenGettingTheTrip_thenReceiveTheTrip() {
    Optional<Trip> result = this.tripRepository.get(AN_EXISTING_TRIP_ID);

    assertTrue(result.isPresent());
    assertEquals(this.anyTrip, result.get());
  }

  @Test
  void givenTripIsInRepository_whenGettingTheTrip_thenReceiveTheTripAsDifferentInstance() {
    Optional<Trip> result = this.tripRepository.get(AN_EXISTING_TRIP_ID);

    assertTrue(result.isPresent());
    assertNotSame(this.anyTrip, result.get());
  }

  @Test
  void givenAllTripsInRepository_whenGettingBunch_thenGetAllTripsFromIDs() {
    List<TripID> existingTrips = new ArrayList<>();
    existingTrips.add(AN_EXISTING_TRIP_ID);
    List<Trip> result = this.tripRepository.getBunch(existingTrips);

    assertEquals(1, result.size());
    assertEquals(this.anyTrip, result.getFirst());
  }

  @Test
  void givenSomeMissingTrips_whenGettingBunch_thenGetOnlyExistingTrips() {
    List<TripID> mixedTrips = new ArrayList<>();
    mixedTrips.add(A_NON_EXISTING_TRIP_ID);
    mixedTrips.add(AN_EXISTING_TRIP_ID);
    List<Trip> result = this.tripRepository.getBunch(mixedTrips);

    assertEquals(1, result.size());
    assertEquals(this.anyTrip, result.getFirst());
  }

  @Test
  void givenTripIsNotInRepository_whenGettingTheTripByIdAndTravelerId_thenReceiveNoTrip() {
    Optional<Trip> result =
        this.tripRepository.getByIdAndTravelerId(
            A_NON_EXISTING_TRIP_ID, A_NON_EXISTING_TRAVELER_ID);

    assertTrue(result.isEmpty());
  }

  @Test
  void givenTripIsInRepository_whenGettingTheTripByIdAndTravelerId_thenReceiveTheTrip() {
    A_GROUP.addGuest(A_GUEST);
    this.anyTrip.reserveSeat(this.anyReservationMock);
    this.tripRepository.save(this.anyTrip);

    Optional<Trip> result =
        this.tripRepository.getByIdAndTravelerId(
            AN_EXISTING_TRIP_ID, AN_UNAUTHENTICATED_TRAVELER_ID);

    assertTrue(result.isPresent());
    assertEquals(this.anyTrip, result.get());
  }
}
