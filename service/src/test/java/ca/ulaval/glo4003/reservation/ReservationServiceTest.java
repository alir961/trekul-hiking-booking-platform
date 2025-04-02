package ca.ulaval.glo4003.reservation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.group.Group;
import ca.ulaval.glo4003.reservation.assembler.CreatedReservationDtoAssembler;
import ca.ulaval.glo4003.reservation.dto.ReservationDto;
import ca.ulaval.glo4003.transport.TransportType;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.guest.dto.GuestDto;
import ca.ulaval.glo4003.traveler.user.User;
import ca.ulaval.glo4003.traveler.user.UserRepository;
import ca.ulaval.glo4003.traveler.user.exception.UserNotFoundException;
import ca.ulaval.glo4003.trip.Trip;
import ca.ulaval.glo4003.trip.TripID;
import ca.ulaval.glo4003.trip.TripRepository;
import ca.ulaval.glo4003.trip.exception.TripNotFoundException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ReservationServiceTest {
  private static final TransportType ANY_TRANSPORT_TYPE = TransportType.ORGANIZED_BUS;
  private static final String ANY_BUS_STOP = "anyBusStop";
  private static final TravelerID ANY_USER_ID = new TravelerID("user01");
  private static final TripID ANY_TRIP_ID = TripID.generate();
  private static final int A_MAXIMUM_SEATS = 371;
  private static final int ANY_MAXIMUM_GROUP_SIZE = 2;
  private static final String ANY_GUEST_FIRSTNAME = "Alice";
  private static final String ANY_GUEST_LASTNAME = "Wonders";
  private static final String ANY_GUEST_IDUL = "444444444";
  private static final String ANY_GUEST_EMAIL = "email@ulaval.ca";
  private static final GuestDto ANY_GUEST_DTO =
      new GuestDto(ANY_GUEST_FIRSTNAME, ANY_GUEST_LASTNAME, ANY_GUEST_IDUL, ANY_GUEST_EMAIL);
  private static final List<GuestDto> ANY_GUEST_DTO_LIST = List.of(ANY_GUEST_DTO);
  private static final ReservationDto ANY_RESERVATION_DTO =
      new ReservationDto(
          ANY_USER_ID, ANY_TRIP_ID, ANY_TRANSPORT_TYPE, ANY_BUS_STOP, ANY_GUEST_DTO_LIST);
  private final TripRepository tripRepositoryMock = mock(TripRepository.class);
  private final ReservationFactory reservationFactoryMock = mock(ReservationFactory.class);
  private final GroupFactory groupFactoryMock = mock(GroupFactory.class);
  private final UserRepository userRepositoryMock = mock(UserRepository.class);
  private final CreatedReservationDtoAssembler createdReservationDtoAssemblerMock =
      mock(CreatedReservationDtoAssembler.class);
  private Reservation reservationMock;
  private User anyUserMock;
  private ReservationService reservationService;
  private Trip tripMock;
  private Group groupMock;

  @BeforeEach
  void setUp() {
    this.reservationService =
        new ReservationService(
            this.userRepositoryMock,
            this.tripRepositoryMock,
            this.reservationFactoryMock,
            this.groupFactoryMock,
            this.createdReservationDtoAssemblerMock);
    this.anyUserMock = mock(User.class);
    this.tripMock = mock(Trip.class);
    this.groupMock = mock(Group.class);
    this.reservationMock = mock(Reservation.class);

    when(this.tripMock.isBlocked()).thenReturn(false);
    when(this.tripMock.getAvailableSeats()).thenReturn(A_MAXIMUM_SEATS);
  }

  @Test
  void givenAUserDoesntFound_whenCreatingReservation_thenThrowUserNotFoundException() {
    when(this.userRepositoryMock.getById(ANY_USER_ID)).thenReturn(Optional.empty());
    when(this.groupMock.getNumberOfGuests()).thenReturn(ANY_MAXIMUM_GROUP_SIZE);
    when(this.tripRepositoryMock.get(ANY_TRIP_ID)).thenReturn(Optional.empty());

    assertThrows(
        UserNotFoundException.class,
        () -> this.reservationService.createReservation(ANY_RESERVATION_DTO));
  }

  @Test
  void givenTripNotFound_whenCreatingReservation_thenThrowTripNotFoundException() {
    when(this.userRepositoryMock.getById(ANY_USER_ID))
        .thenReturn(Optional.ofNullable(this.anyUserMock));
    when(this.groupMock.getNumberOfGuests()).thenReturn(ANY_MAXIMUM_GROUP_SIZE);
    when(this.tripRepositoryMock.get(ANY_TRIP_ID)).thenReturn(Optional.empty());

    assertThrows(
        TripNotFoundException.class,
        () -> this.reservationService.createReservation(ANY_RESERVATION_DTO));
  }

  @Test
  void whenCreatingReservation_thenGroupFactoryCreatesGroup() {
    when(this.userRepositoryMock.getById(ANY_USER_ID))
        .thenReturn(Optional.ofNullable(this.anyUserMock));
    when(this.tripRepositoryMock.get(ANY_TRIP_ID)).thenReturn(Optional.of(this.tripMock));

    this.reservationService.createReservation(ANY_RESERVATION_DTO);

    verify(this.groupFactoryMock).create(ANY_USER_ID, ANY_GUEST_DTO_LIST);
  }

  @Test
  void givenTripHasAvailableSeats_whenCreatingReservation_thenReservationIsCreated() {
    this.givenGroupReservation();

    this.reservationService.createReservation(ANY_RESERVATION_DTO);

    verify(this.tripMock).reserveSeat(this.reservationMock);
    verify(this.reservationFactoryMock)
        .create(ANY_USER_ID, this.tripMock, this.groupMock, ANY_BUS_STOP, ANY_TRANSPORT_TYPE);
    verify(this.anyUserMock).addReservation(this.reservationMock);
    verify(this.userRepositoryMock).save(this.anyUserMock);
  }

  private void givenGroupReservation() {
    when(this.userRepositoryMock.getById(ANY_USER_ID))
        .thenReturn(Optional.ofNullable(this.anyUserMock));
    when(this.tripRepositoryMock.get(ANY_TRIP_ID)).thenReturn(Optional.of(this.tripMock));
    when(this.groupFactoryMock.create(ANY_USER_ID, ANY_GUEST_DTO_LIST)).thenReturn(this.groupMock);
    when(this.groupMock.getNumberOfGuests()).thenReturn(ANY_MAXIMUM_GROUP_SIZE);
    when(this.reservationFactoryMock.create(
            ANY_USER_ID, this.tripMock, this.groupMock, ANY_BUS_STOP, ANY_TRANSPORT_TYPE))
        .thenReturn(this.reservationMock);
    doNothing().when(this.tripMock).reserveSeat(this.reservationMock);
  }
}
