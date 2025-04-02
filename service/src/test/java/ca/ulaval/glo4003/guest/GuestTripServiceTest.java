package ca.ulaval.glo4003.guest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.confirmationcode.ConfirmationCode;
import ca.ulaval.glo4003.group.Group;
import ca.ulaval.glo4003.group.exception.GroupNotFoundException;
import ca.ulaval.glo4003.invoice.InvoiceID;
import ca.ulaval.glo4003.invoice.TripInvoice;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.profile.assembler.ProfileTripDtoAssembler;
import ca.ulaval.glo4003.profile.dto.ProfileTripDto;
import ca.ulaval.glo4003.reservation.Reservation;
import ca.ulaval.glo4003.reservation.ReservationID;
import ca.ulaval.glo4003.reservation.ReservationStatus;
import ca.ulaval.glo4003.reservation.exception.ReservationNotFoundException;
import ca.ulaval.glo4003.transport.TransportType;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.user.User;
import ca.ulaval.glo4003.traveler.user.UserRepository;
import ca.ulaval.glo4003.traveler.user.exception.UserNotFoundException;
import ca.ulaval.glo4003.trip.Trip;
import ca.ulaval.glo4003.trip.TripID;
import ca.ulaval.glo4003.trip.TripRepository;
import ca.ulaval.glo4003.trip.exception.TripNotFoundException;
import java.time.ZonedDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GuestTripServiceTest {
  private static final TransportType ANY_TRANSPORT_TYPE = TransportType.ORGANIZED_BUS;
  private static final String ANY_BUS_STOP = "anyBusStop";
  private static final TripID A_TRIP_ID = TripID.generate();
  private static final TravelerID A_TRAVELER_ID = new TravelerID("traveler123");
  private static final Trip A_TRIP = mock(Trip.class);
  private static final Group A_GROUP = mock(Group.class);
  private static final User A_USER = mock(User.class);
  private static final TripInvoice AN_INVOICE = mock(TripInvoice.class);
  private static final InvoiceID AN_INVOICE_ID = InvoiceID.generate();
  private static final ReservationID AN_RESERVATION_ID = ReservationID.generate();
  private static final ProfileTripDto A_PROFILE_TRIP_DTO = mock(ProfileTripDto.class);
  private static final ZonedDateTime A_RESERVATION_CREATION_DATE = ZonedDateTime.now();
  private static final ZonedDateTime A_RESERVATION_EXPIRATION_DATE =
      A_RESERVATION_CREATION_DATE.plusMinutes(50);
  private static final Money A_TRANSPORT_COST = new Money(40.);
  private static final ConfirmationCode A_CONFIRMATION_CODE =
      new ConfirmationCode("confirmationCode");

  private Reservation reservation;
  private TripRepository tripRepository;
  private UserRepository userRepository;
  private ProfileTripDtoAssembler profileTripDtoAssembler;
  private GuestTripService guestTripService;

  @BeforeEach
  void setUp() {
    this.tripRepository = mock(TripRepository.class);
    this.userRepository = mock(UserRepository.class);
    this.profileTripDtoAssembler = mock(ProfileTripDtoAssembler.class);

    this.reservation =
        new Reservation(
            AN_RESERVATION_ID,
            AN_INVOICE,
            A_GROUP,
            A_RESERVATION_CREATION_DATE,
            A_RESERVATION_EXPIRATION_DATE,
            A_CONFIRMATION_CODE,
            ANY_BUS_STOP,
            ANY_TRANSPORT_TYPE);
    this.reservation.setTransportCost(A_TRANSPORT_COST);
    this.reservation.setStatus(ReservationStatus.CONFIRMED);

    this.guestTripService =
        new GuestTripService(
            this.tripRepository, this.userRepository, this.profileTripDtoAssembler);

    when(A_TRIP.getId()).thenReturn(A_TRIP_ID);
    when(A_TRIP.findFirstGroupOfTraveler(A_TRAVELER_ID)).thenReturn(Optional.ofNullable(A_GROUP));
    when(A_USER.findFirstReservationOfTrip(A_TRIP_ID)).thenReturn(Optional.of(this.reservation));
    when(this.reservation.getInvoiceID()).thenReturn(AN_INVOICE_ID);
    when(this.tripRepository.getByIdAndTravelerId(A_TRIP_ID, A_TRAVELER_ID))
        .thenReturn(Optional.of(A_TRIP));
    when(this.userRepository.getById(A_GROUP.getUserInCharge())).thenReturn(Optional.of(A_USER));
    when(A_USER.getTripInvoice(AN_INVOICE_ID)).thenReturn(Optional.of(AN_INVOICE));
    when(this.profileTripDtoAssembler.assemble(A_TRIP, this.reservation, A_TRAVELER_ID))
        .thenReturn(A_PROFILE_TRIP_DTO);
  }

  @Test
  void whenSearchingTripByIdAndTravelerId_thenGetTripFromRepository() {
    this.guestTripService.searchTripByIdAndTravelerId(A_TRIP_ID, A_TRAVELER_ID);

    verify(this.tripRepository).getByIdAndTravelerId(A_TRIP_ID, A_TRAVELER_ID);
  }

  @Test
  void givenNoTripFound_whenSearchingTripByIdAndTravelerId_thenThrowsTripNotFoundException() {
    when(this.tripRepository.getByIdAndTravelerId(A_TRIP_ID, A_TRAVELER_ID))
        .thenReturn(Optional.empty());

    assertThrows(
        TripNotFoundException.class,
        () -> this.guestTripService.searchTripByIdAndTravelerId(A_TRIP_ID, A_TRAVELER_ID));
  }

  @Test
  void
      givenNoGroupWithTraveler_whenSearchingTripByIdAndTravelerId_thenThrowsGroupNotFoundException() {
    when(A_TRIP.findFirstGroupOfTraveler(A_TRAVELER_ID)).thenReturn(Optional.empty());

    assertThrows(
        GroupNotFoundException.class,
        () -> this.guestTripService.searchTripByIdAndTravelerId(A_TRIP_ID, A_TRAVELER_ID));
  }

  @Test
  void givenNoUserInCharge_whenSearchingTripByIdAndTravelerId_thenThrowsUserNotFoundException() {
    when(this.userRepository.getById(A_GROUP.getUserInCharge())).thenReturn(Optional.empty());

    assertThrows(
        UserNotFoundException.class,
        () -> this.guestTripService.searchTripByIdAndTravelerId(A_TRIP_ID, A_TRAVELER_ID));
  }

  @Test
  void
      givenNoReservationForTrip_whenSearchingTripByIdAndTravelerId_thenThrowsReservationNotFoundException() {
    when(A_USER.findFirstReservationOfTrip(A_TRIP_ID)).thenReturn(Optional.empty());

    assertThrows(
        ReservationNotFoundException.class,
        () -> this.guestTripService.searchTripByIdAndTravelerId(A_TRIP_ID, A_TRAVELER_ID));
  }

  @Test
  void whenSearchingTripByIdAndTravelerId_thenReturnAssembledProfileTripDto() {
    ProfileTripDto result =
        this.guestTripService.searchTripByIdAndTravelerId(A_TRIP_ID, A_TRAVELER_ID);

    verify(this.profileTripDtoAssembler).assemble(A_TRIP, this.reservation, A_TRAVELER_ID);
    assertEquals(A_PROFILE_TRIP_DTO, result);
  }
}
