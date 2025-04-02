package ca.ulaval.glo4003.reservation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.group.Group;
import ca.ulaval.glo4003.invoice.InvoiceID;
import ca.ulaval.glo4003.invoice.TripInvoice;
import ca.ulaval.glo4003.invoice.assembler.InvoiceDtoAssembler;
import ca.ulaval.glo4003.invoice.dto.InvoiceDto;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.user.User;
import ca.ulaval.glo4003.traveler.user.UserRepository;
import ca.ulaval.glo4003.traveler.user.exception.UserNotFoundException;
import ca.ulaval.glo4003.trip.Trip;
import ca.ulaval.glo4003.trip.TripID;
import ca.ulaval.glo4003.trip.TripRepository;
import ca.ulaval.glo4003.trip.exception.TripNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CancelReservationServiceTest {
  private static final ReservationID ANY_RESERVATION_ID = ReservationID.generate();
  private static final TravelerID ANY_TRAVELER_ID = new TravelerID("ANY_TRAVELER_ID");
  private static final TripID ANY_TRIP_ID = TripID.generate();
  private static final LocalDate ANY_TRIP_DATE = LocalDate.now();
  private static final InvoiceDto ANY_INVOICE_DTO = createAnyInvoiceDto();

  private CancelReservationService cancelReservationService;

  private UserRepository userRepositoryMock;
  private TripRepository tripRepositoryMock;
  private User userMock;
  private Reservation reservationMock;
  private InvoiceDtoAssembler invoiceDtoAssemblerMock;
  private Trip tripMock;
  private Group groupMock;

  @BeforeEach
  void setUp() {
    this.userRepositoryMock = mock(UserRepository.class);
    this.tripRepositoryMock = mock(TripRepository.class);
    this.userMock = mock(User.class);
    this.reservationMock = mock(Reservation.class);
    this.tripMock = mock(Trip.class);
    this.groupMock = mock(Group.class);
    this.invoiceDtoAssemblerMock = mock(InvoiceDtoAssembler.class);

    when(this.userRepositoryMock.getById(ANY_TRAVELER_ID)).thenReturn(Optional.of(this.userMock));
    when(this.reservationMock.getTripID()).thenReturn(ANY_TRIP_ID);
    when(this.tripRepositoryMock.get(ANY_TRIP_ID)).thenReturn(Optional.of(this.tripMock));
    when(this.tripMock.getDate()).thenReturn(ANY_TRIP_DATE);
    when(this.reservationMock.getGroup()).thenReturn(this.groupMock);
    when(this.userMock.cancelReservation(ANY_RESERVATION_ID, ANY_TRIP_DATE))
        .thenReturn(this.reservationMock);
    when(this.userMock.getTripIDForReservation(ANY_RESERVATION_ID)).thenReturn(ANY_TRIP_ID);

    this.cancelReservationService =
        new CancelReservationService(
            this.userRepositoryMock, this.tripRepositoryMock, this.invoiceDtoAssemblerMock);
  }

  @Test
  void givenUserDoesNotExist_whenCancelReservation_thenThrowUserNotFoundException() {
    final TravelerID travelerNotInRepository = new TravelerID("IdUserNotInRepository");
    when(this.userRepositoryMock.getById(travelerNotInRepository)).thenReturn(Optional.empty());

    assertThrows(
        UserNotFoundException.class,
        () ->
            this.cancelReservationService.cancelReservation(
                travelerNotInRepository, ANY_RESERVATION_ID));
  }

  @Test
  void givenTripNotInRepository_whenCancelReservation_thenThrowTripNotFoundException() {
    when(this.tripRepositoryMock.get(ANY_TRIP_ID)).thenReturn(Optional.empty());

    assertThrows(
        TripNotFoundException.class,
        () -> this.cancelReservationService.cancelReservation(ANY_TRAVELER_ID, ANY_RESERVATION_ID));
  }

  @Test
  void whenCancelReservation_thenUseCorrectReservation() {
    this.cancelReservationService.cancelReservation(ANY_TRAVELER_ID, ANY_RESERVATION_ID);

    verify(this.userMock).cancelReservation(ANY_RESERVATION_ID, ANY_TRIP_DATE);
  }

  @Test
  void whenCancelReservation_thenReleaseTripSeat() {
    this.cancelReservationService.cancelReservation(ANY_TRAVELER_ID, ANY_RESERVATION_ID);

    verify(this.tripMock).releaseSeat(this.reservationMock);
  }

  @Test
  void whenCancelReservation_thenAssembleInvoiceDto() {
    TripInvoice tripInvoiceForTrip = mock(TripInvoice.class);
    when(this.userMock.getInvoiceForTrip(ANY_TRIP_ID)).thenReturn(tripInvoiceForTrip);

    this.cancelReservationService.cancelReservation(ANY_TRAVELER_ID, ANY_RESERVATION_ID);

    verify(this.invoiceDtoAssemblerMock).assemble(tripInvoiceForTrip, ANY_TRAVELER_ID);
  }

  @Test
  void whenCancelReservation_thenReturnAssembledDto() {
    TripInvoice tripInvoiceForTrip = mock(TripInvoice.class);
    when(this.userMock.getInvoiceForTrip(ANY_TRIP_ID)).thenReturn(tripInvoiceForTrip);
    when(this.invoiceDtoAssemblerMock.assemble(tripInvoiceForTrip, ANY_TRAVELER_ID))
        .thenReturn(ANY_INVOICE_DTO);

    InvoiceDto returnedInvoiceDto =
        this.cancelReservationService.cancelReservation(ANY_TRAVELER_ID, ANY_RESERVATION_ID);

    assertEquals(ANY_INVOICE_DTO, returnedInvoiceDto);
  }

  private static InvoiceDto createAnyInvoiceDto() {
    return new InvoiceDto(
        InvoiceID.generate(),
        new HashMap<>(),
        new HashMap<>(),
        new Money("0"),
        new Money("0"),
        new ArrayList<>());
  }
}
