package ca.ulaval.glo4003.notification;

import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.notification.filters.TripNotificationFilters;
import ca.ulaval.glo4003.notification.types.trip.upcoming.UpcomingTripNotificationProcessor;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.user.User;
import ca.ulaval.glo4003.traveler.user.UserRepository;
import ca.ulaval.glo4003.trip.Trip;
import ca.ulaval.glo4003.trip.TripThresholdFinder;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TripNotificationServiceTest {
  private static final TravelerID ANY_TRAVELER_ID = new TravelerID("1");
  private static final TravelerID ANY_SECOND_TRAVELER_ID = new TravelerID("2");
  private static final List<TravelerID> ANY_TRIP_TRAVELERS = List.of(ANY_TRAVELER_ID);
  private static final List<TravelerID> ANY_SECOND_TRIP_TRAVELERS = List.of(ANY_SECOND_TRAVELER_ID);
  private UserRepository userRepositoryMock;
  private TripNotificationFilters tripNotificationFiltersMock;
  private UpcomingTripNotificationProcessor tripUpcomingTripNotificationProcessorMock;
  private TripThresholdFinder tripThresholdFinderMock;
  private TripNotificationService tripNotificationService;

  private Trip tripInThresholdMock;
  private Trip secondTripInThresholdMock;
  private User userInTripMock;
  private List<User> usersRetrievedFromRepo;

  @BeforeEach
  void setUp() {
    this.userRepositoryMock = mock(UserRepository.class);
    this.tripNotificationFiltersMock = mock(TripNotificationFilters.class);
    this.tripThresholdFinderMock = mock(TripThresholdFinder.class);
    this.tripUpcomingTripNotificationProcessorMock = mock(UpcomingTripNotificationProcessor.class);
    this.tripInThresholdMock = mock(Trip.class);
    this.userInTripMock = mock(User.class);
    this.secondTripInThresholdMock = mock(Trip.class);

    this.tripNotificationService =
        new TripNotificationService(
            this.userRepositoryMock,
            this.tripNotificationFiltersMock,
            this.tripThresholdFinderMock,
            this.tripUpcomingTripNotificationProcessorMock);

    List<Trip> listOfTripsInThreshold = new ArrayList<>();
    listOfTripsInThreshold.add(this.tripInThresholdMock);
    this.usersRetrievedFromRepo = new ArrayList<>();
    this.usersRetrievedFromRepo.add(this.userInTripMock);

    when(this.tripThresholdFinderMock.find()).thenReturn(listOfTripsInThreshold);
    listOfTripsInThreshold.add(this.secondTripInThresholdMock);
    User secondUserInTripMock = mock(User.class);
    this.usersRetrievedFromRepo.add(secondUserInTripMock);
    this.whenGetBunchReturnUsers(
        this.tripInThresholdMock, ANY_TRIP_TRAVELERS, this.usersRetrievedFromRepo);
    this.whenGetBunchReturnUsers(
        this.secondTripInThresholdMock, ANY_SECOND_TRIP_TRAVELERS, this.usersRetrievedFromRepo);
  }

  @Test
  void whenNotifyUpcomingTrips_thenUseThresholdFinderToFindTrips() {
    this.tripNotificationService.notifyUpComingTrips();

    verify(this.tripThresholdFinderMock).find();
  }

  @Test
  void whenNotifyUpcomingTrips_thenGetUsersForEachTrip() {
    this.tripNotificationService.notifyUpComingTrips();

    verify(this.userRepositoryMock).getBunch(ANY_TRIP_TRAVELERS);
    verify(this.userRepositoryMock).getBunch(ANY_SECOND_TRIP_TRAVELERS);
  }

  @Test
  void whenNotifyUpcomingTrips_thenApplyFiltersOnEachTrip() {
    this.tripNotificationService.notifyUpComingTrips();

    verify(this.tripNotificationFiltersMock)
        .applyFiltersForUsers(this.usersRetrievedFromRepo, this.tripInThresholdMock);
    verify(this.tripNotificationFiltersMock)
        .applyFiltersForUsers(this.usersRetrievedFromRepo, this.secondTripInThresholdMock);
  }

  @Test
  void givenTripsAreInThreshold_whenNotifyUpcomingTrips_thenNotifyUsersForEachTrip() {
    List<User> filteredUsers = List.of(this.userInTripMock);
    when(this.tripNotificationFiltersMock.applyFiltersForUsers(
            this.usersRetrievedFromRepo, this.tripInThresholdMock))
        .thenReturn(filteredUsers);
    when(this.tripNotificationFiltersMock.applyFiltersForUsers(
            this.usersRetrievedFromRepo, this.secondTripInThresholdMock))
        .thenReturn(filteredUsers);

    this.tripNotificationService.notifyUpComingTrips();

    verify(this.tripUpcomingTripNotificationProcessorMock)
        .notify(filteredUsers, this.tripInThresholdMock);
    verify(this.tripUpcomingTripNotificationProcessorMock)
        .notify(filteredUsers, this.secondTripInThresholdMock);
  }

  private void whenGetBunchReturnUsers(
      Trip tripMock, List<TravelerID> bunch, List<User> usersToReturn) {
    when(tripMock.retrieveReservedUsers()).thenReturn(bunch);
    when(this.userRepositoryMock.getBunch(bunch)).thenReturn(usersToReturn);
  }
}
