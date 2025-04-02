package ca.ulaval.glo4003.hosting.reservation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.hosting.Hosting;
import ca.ulaval.glo4003.hosting.HostingID;
import ca.ulaval.glo4003.hosting.HostingRepository;
import ca.ulaval.glo4003.hosting.exception.HostingNotFoundException;
import ca.ulaval.glo4003.hosting.reservation.assembler.CreatedHostingReservationDtoAssembler;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.user.User;
import ca.ulaval.glo4003.traveler.user.UserRepository;
import ca.ulaval.glo4003.trip.Trip;
import ca.ulaval.glo4003.trip.TripID;
import ca.ulaval.glo4003.trip.TripRepository;
import ca.ulaval.glo4003.trip.TripTravelerFetcher;
import ca.ulaval.glo4003.trip.exception.TripNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.internal.InOrderImpl;

class HostingReservationServiceTest {
  private static final TravelerID ANY_USER_ID = new TravelerID("12345gh");
  private static final TripID ANY_TRIP_ID = TripID.generate();
  private static final HostingID ANY_HOSTING_ID = new HostingID(Integer.valueOf("12345"));
  private User userMock;
  private Trip tripMock;
  private Hosting hostingMock;
  private UserRepository userRepositoryMock;
  private HostingRepository hostingRepositoryMock;
  private TripRepository tripRepositoryMock;
  private CreatedHostingReservationDtoAssembler assemblerMock;
  private HostingNotificationSender hostingNotificationSenderMock;
  private TripTravelerFetcher tripTravelerFetcherMock;
  private TripTravelerFetcherFactory tripTravelerFetcherFactoryMock;
  private HostingReservationService hostingReservationService;

  @BeforeEach
  void setUp() {
    this.userMock = mock(User.class);
    this.tripMock = mock(Trip.class);
    this.hostingMock = mock(Hosting.class);
    this.userRepositoryMock = mock(UserRepository.class);
    this.tripRepositoryMock = mock(TripRepository.class);
    this.hostingRepositoryMock = mock(HostingRepository.class);
    this.assemblerMock = mock(CreatedHostingReservationDtoAssembler.class);
    this.hostingNotificationSenderMock = mock(HostingNotificationSender.class);

    this.tripTravelerFetcherMock = mock(TripTravelerFetcher.class);
    this.tripTravelerFetcherFactoryMock = mock(TripTravelerFetcherFactory.class);

    this.hostingReservationService =
        new HostingReservationService(
            this.userRepositoryMock,
            this.hostingRepositoryMock,
            this.tripRepositoryMock,
            this.assemblerMock,
            this.hostingNotificationSenderMock,
            this.tripTravelerFetcherFactoryMock);

    when(this.userRepositoryMock.getById(ANY_USER_ID)).thenReturn(Optional.of(this.userMock));
  }

  @Test
  void givenHostingNotFound_whenBookingHostingReservation_thenHostingNotFoundException() {
    assertThrows(
        HostingNotFoundException.class,
        () -> this.hostingReservationService.bookHosting(ANY_TRIP_ID, ANY_HOSTING_ID));
  }

  @Test
  void givenHostingFoundAndTripNotFound_whenBookingHostingReservation_thenTripNotFoundException() {
    this.givenHostingFound();

    assertThrows(
        TripNotFoundException.class,
        () -> this.hostingReservationService.bookHosting(ANY_TRIP_ID, ANY_HOSTING_ID));
  }

  @Test
  void
      givenHostingAndTripFound_whenBookingHostingReservation_thenSaveTripAfterTripReservesHosting() {
    when(this.tripTravelerFetcherFactoryMock.create(this.userRepositoryMock))
        .thenReturn(this.tripTravelerFetcherMock);
    this.givenHostingFound();
    this.givenTripFound();

    this.hostingReservationService.bookHosting(ANY_TRIP_ID, ANY_HOSTING_ID);

    InOrder inOrder = new InOrderImpl(List.of(this.tripMock, this.tripRepositoryMock));
    inOrder.verify(this.tripMock).reserveHosting(this.hostingMock, this.tripTravelerFetcherMock);
    inOrder.verify(this.tripRepositoryMock).save(this.tripMock);
  }

  @Test
  void givenHostingAndTripFound_whenBookingHostingReservation_thenSaveUpdatedUsers() {
    when(this.tripTravelerFetcherFactoryMock.create(this.userRepositoryMock))
        .thenReturn(this.tripTravelerFetcherMock);
    when(this.tripTravelerFetcherMock.fetchUpdatedUsers()).thenReturn(Set.of(this.userMock));
    this.givenHostingFound();
    this.givenTripFound();

    this.hostingReservationService.bookHosting(ANY_TRIP_ID, ANY_HOSTING_ID);

    verify(this.userRepositoryMock).save(Set.of(this.userMock));
  }

  @Test
  void
      givenHostingAndTripFound_whenBookingHostingReservation_thenSaveHostingAfterTripReservesHosting() {
    when(this.tripTravelerFetcherFactoryMock.create(this.userRepositoryMock))
        .thenReturn(this.tripTravelerFetcherMock);
    this.givenHostingFound();
    this.givenTripFound();

    this.hostingReservationService.bookHosting(ANY_TRIP_ID, ANY_HOSTING_ID);

    verify(this.hostingRepositoryMock).save(this.hostingMock);
    InOrder inOrder = new InOrderImpl(List.of(this.tripMock, this.hostingRepositoryMock));
    inOrder.verify(this.tripMock).reserveHosting(this.hostingMock, this.tripTravelerFetcherMock);
    inOrder.verify(this.hostingRepositoryMock).save(this.hostingMock);
  }

  private void givenHostingFound() {
    when(this.hostingRepositoryMock.get(ANY_HOSTING_ID)).thenReturn(Optional.of(this.hostingMock));
  }

  private void givenTripFound() {
    when(this.tripRepositoryMock.get(ANY_TRIP_ID)).thenReturn(Optional.of(this.tripMock));
  }
}
