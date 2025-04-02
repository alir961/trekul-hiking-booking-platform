package ca.ulaval.glo4003.profile;

import static ca.ulaval.glo4003.reservation.ReservationStatus.CANCELLED;
import static ca.ulaval.glo4003.reservation.ReservationStatus.CONFIRMED;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

import ca.ulaval.glo4003.invoice.InvoiceID;
import ca.ulaval.glo4003.invoice.TripInvoice;
import ca.ulaval.glo4003.profile.assembler.ProfileTripDtoAssembler;
import ca.ulaval.glo4003.profile.dto.ProfileTripDto;
import ca.ulaval.glo4003.reservation.Reservation;
import ca.ulaval.glo4003.reservation.ReservationStatus;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.user.User;
import ca.ulaval.glo4003.traveler.user.UserRepository;
import ca.ulaval.glo4003.traveler.user.exception.UserNotFoundException;
import ca.ulaval.glo4003.trip.*;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProfileTripsServiceTest {
  private static final TravelerID ANY_EXISTING_USER_ID = new TravelerID("120f9uiwec");
  private static final Reservation RESERVATION_OF_EXISTING_USER = mock(Reservation.class);
  private static final TripID TRIP_ID_OF_RESERVATION = TripID.generate();
  private static final InvoiceID INVOICE_ID_OF_RESERVATION = InvoiceID.generate();
  private static final Trip TRIP_DUMMY = mock(Trip.class);
  private static final TripInvoice INVOICE_DUMMY = mock(TripInvoice.class);
  private static final ProfileTripDto EXISTING_TRIP_DTO = mock(ProfileTripDto.class);
  private static final ReservationStatus ANY_STATUS = CONFIRMED;
  private ProfileTripDtoAssembler profileTripDtoAssemblerMock;
  private User anyExistingUser;
  private TripRepository tripRepositoryMock;
  private UserRepository userRepositoryMock;
  private ProfileTripsService profileTripsService;

  @BeforeAll
  static void setAll() {
    when(RESERVATION_OF_EXISTING_USER.getTripID()).thenReturn(TRIP_ID_OF_RESERVATION);
    when(RESERVATION_OF_EXISTING_USER.getInvoiceID()).thenReturn(INVOICE_ID_OF_RESERVATION);
    when(RESERVATION_OF_EXISTING_USER.getStatus()).thenReturn(ANY_STATUS);
  }

  @BeforeEach
  void setUp() {
    this.anyExistingUser = mock(User.class);
    this.tripRepositoryMock = mock(TripRepository.class);
    this.userRepositoryMock = mock(UserRepository.class);
    this.profileTripDtoAssemblerMock = mock(ProfileTripDtoAssembler.class);
    this.profileTripsService =
        new ProfileTripsService(
            this.tripRepositoryMock, this.userRepositoryMock, this.profileTripDtoAssemblerMock);
  }

  @Test
  void givenExistingUserWithValidReservationAndSameStatus_whenSearchingTrips_thenGetTrip() {
    this.mockWithExistingUser();
    this.mockWithExistingTrip();
    this.mockWithExistingInvoice();
    List<ProfileTripDto> result =
        this.profileTripsService.searchTrips(ANY_EXISTING_USER_ID, ANY_STATUS);

    assertFalse(result.isEmpty());
    assertSame(EXISTING_TRIP_DTO, result.getFirst());
  }

  @Test
  void
      givenExistingUserWithValidReservationButDifferentStatus_whenSearchingTrips_thenDoNotGetTrip() {
    this.mockWithExistingUser();
    List<ProfileTripDto> result =
        this.profileTripsService.searchTrips(ANY_EXISTING_USER_ID, CANCELLED);

    assertTrue(result.isEmpty());
  }

  @Test
  void givenExistingUserWithReservationOfNonExistingTrip_whenSearchingTrips_thenDoNotGetTrip() {
    this.mockWithExistingUser();
    this.mockWithExistingInvoice();
    List<ProfileTripDto> result =
        this.profileTripsService.searchTrips(ANY_EXISTING_USER_ID, ANY_STATUS);

    assertTrue(result.isEmpty());
  }

  @Test
  void givenExistingUserWithReservationOfNonExistingInvoice_whenSearchingTrips_thenDoNotGetTrip() {
    this.mockWithExistingUser();
    this.mockWithExistingTrip();
    List<ProfileTripDto> result =
        this.profileTripsService.searchTrips(ANY_EXISTING_USER_ID, ANY_STATUS);

    assertTrue(result.isEmpty());
  }

  @Test
  void givenNonExistingUser_whenSearchingTrips_thenThrowUserNotFoundException() {
    String anyNonExistingUserIDString = "10dx9d8cs";
    TravelerID nonExistingUserID = new TravelerID(anyNonExistingUserIDString);
    when(this.userRepositoryMock.getById(nonExistingUserID)).thenReturn(Optional.empty());
    assertThrows(
        UserNotFoundException.class,
        () -> this.profileTripsService.searchTrips(nonExistingUserID, ANY_STATUS));
  }

  private void mockWithExistingUser() {

    when(this.anyExistingUser.fetchReservationsOfStatus(ANY_STATUS))
        .thenReturn(List.of(RESERVATION_OF_EXISTING_USER));
    when(this.anyExistingUser.getId()).thenReturn(ANY_EXISTING_USER_ID);
    when(this.anyExistingUser.getReservations()).thenReturn(List.of(RESERVATION_OF_EXISTING_USER));
    when(RESERVATION_OF_EXISTING_USER.getInvoiceID()).thenReturn(INVOICE_ID_OF_RESERVATION);
    when(this.userRepositoryMock.getById(ANY_EXISTING_USER_ID))
        .thenReturn(Optional.of(this.anyExistingUser));
    when(RESERVATION_OF_EXISTING_USER.getInvoice()).thenReturn(INVOICE_DUMMY);
    when(this.profileTripDtoAssemblerMock.assemble(
            TRIP_DUMMY, RESERVATION_OF_EXISTING_USER, ANY_EXISTING_USER_ID))
        .thenReturn(EXISTING_TRIP_DTO);
  }

  private void mockWithExistingTrip() {
    when(this.tripRepositoryMock.get(TRIP_ID_OF_RESERVATION)).thenReturn(Optional.of(TRIP_DUMMY));
  }

  private void mockWithExistingInvoice() {
    when(this.anyExistingUser.getTripInvoice(RESERVATION_OF_EXISTING_USER.getInvoiceID()))
        .thenReturn(Optional.of(INVOICE_DUMMY));
  }
}
