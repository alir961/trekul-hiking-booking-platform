package ca.ulaval.glo4003.reservation.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4003.group.Group;
import ca.ulaval.glo4003.group.reponse.GroupResponse;
import ca.ulaval.glo4003.guest.request.GuestRequest;
import ca.ulaval.glo4003.guest.response.GuestResponse;
import ca.ulaval.glo4003.invoice.InvoiceID;
import ca.ulaval.glo4003.invoice.dto.InvoiceDto;
import ca.ulaval.glo4003.invoice.response.InvoiceResponse;
import ca.ulaval.glo4003.invoice.response.PaymentTraceResponse;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.reservation.*;
import ca.ulaval.glo4003.reservation.assembler.ReservationRequestAssembler;
import ca.ulaval.glo4003.reservation.assembler.ReservationResponseAssembler;
import ca.ulaval.glo4003.reservation.dto.CreatedReservationDto;
import ca.ulaval.glo4003.reservation.dto.ReservationDto;
import ca.ulaval.glo4003.reservation.request.ReservationRequest;
import ca.ulaval.glo4003.reservation.response.ReservationResponse;
import ca.ulaval.glo4003.transport.TransportType;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.guest.dto.GuestDto;
import ca.ulaval.glo4003.trip.TripID;
import java.time.ZonedDateTime;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ReservationCreationUseCaseTest {
  private static final String ANY_VALID_TRANSPORT_TYPE = "NONE";
  private static final String ANY_BUS_STOP = "busStop";
  private static final TransportType ANY_TRANSPORT_TYPE = TransportType.CARPOOL;
  private static final ReservationID ANY_RESERVATION_ID = ReservationID.generate();
  private static final InvoiceDto INVOICE_DTO_DUMMY = mock(InvoiceDto.class);
  private static final ZonedDateTime RESERVATION_EXPIRATION = ZonedDateTime.now();
  private static final Money RESERVATION_MINIMAL_PAYMENT = new Money("12.5");
  private static final Group ANY_GROUP = mock(Group.class);
  private static final ReservationStatus RESERVATION_STATUS = ReservationStatus.CONFIRMED;
  private static final TransportType RESERVATION_TRANSPORT_TYPE = TransportType.CARPOOL;
  private static final CreatedReservationDto ANY_CREATED_RESERVATION_DTO =
      new CreatedReservationDto(
          ANY_RESERVATION_ID,
          INVOICE_DTO_DUMMY,
          ANY_GROUP,
          RESERVATION_MINIMAL_PAYMENT,
          RESERVATION_EXPIRATION,
          RESERVATION_STATUS,
          RESERVATION_TRANSPORT_TYPE);
  private static final TravelerID ANY_AUTHENTICATED_USER_ID = new TravelerID("userID");
  private static final TripID ANY_TRIP_ID = TripID.generate();
  private static final InvoiceID ANY_INVOICE_ID = InvoiceID.generate();
  private static final List<GuestDto> ANY_GUEST_DTO_LIST = List.of();
  private static final ReservationDto ANY_RESERVATION_DTO =
      new ReservationDto(
          ANY_AUTHENTICATED_USER_ID,
          ANY_TRIP_ID,
          ANY_TRANSPORT_TYPE,
          ANY_BUS_STOP,
          ANY_GUEST_DTO_LIST);
  private static final List<GuestRequest> ANY_GUEST_REQUEST_LIST = List.of();
  private static final ReservationRequest ANY_RESERVATION_REQUEST =
      new ReservationRequest(ANY_VALID_TRANSPORT_TYPE, ANY_BUS_STOP, ANY_GUEST_REQUEST_LIST);
  private static final InvoiceResponse ANY_INVOICE_RESPONSE = createAnyInvoiceResponse();
  private static final ZonedDateTime ANY_EXPIRATION = ZonedDateTime.now();
  private static final List<GuestResponse> ANY_GUEST_RESPONSE_LIST = List.of();
  private static final GroupResponse ANY_GROUP_RESPONSE =
      new GroupResponse(ANY_GUEST_RESPONSE_LIST);
  private static final ReservationResponse ANY_RESERVATION_RESPONSE =
      new ReservationResponse(
          ANY_RESERVATION_ID.toString(),
          ANY_INVOICE_RESPONSE,
          ANY_GROUP_RESPONSE,
          RESERVATION_MINIMAL_PAYMENT.toString(),
          ANY_EXPIRATION);
  private ReservationService createReservationServiceMock;
  private ReservationRequestAssembler reservationRequestAssemblerMock;
  private ReservationResponseAssembler reservationResponseAssemblerMock;
  private ReservationCreationUseCase reservationCreationUseCase;

  @BeforeEach
  void setUp() {
    this.createReservationServiceMock = mock(ReservationService.class);
    this.reservationRequestAssemblerMock = mock(ReservationRequestAssembler.class);
    this.reservationResponseAssemblerMock = mock(ReservationResponseAssembler.class);
    this.reservationCreationUseCase =
        new ReservationCreationUseCase(
            this.createReservationServiceMock,
            this.reservationRequestAssemblerMock,
            this.reservationResponseAssemblerMock);
  }

  @Test
  void whenCreateReservation_thenAssembleToReservationDto() {
    this.reservationCreationUseCase.createReservation(
        ANY_RESERVATION_REQUEST, ANY_AUTHENTICATED_USER_ID, ANY_TRIP_ID);

    verify(this.reservationRequestAssemblerMock)
        .assemble(ANY_RESERVATION_REQUEST, ANY_AUTHENTICATED_USER_ID, ANY_TRIP_ID);
  }

  @Test
  void givenAuthenticatedUser_whenCreateReservation_thenCreateTheReservation() {
    when(this.reservationRequestAssemblerMock.assemble(
            ANY_RESERVATION_REQUEST, ANY_AUTHENTICATED_USER_ID, ANY_TRIP_ID))
        .thenReturn(ANY_RESERVATION_DTO);

    this.reservationCreationUseCase.createReservation(
        ANY_RESERVATION_REQUEST, ANY_AUTHENTICATED_USER_ID, ANY_TRIP_ID);

    verify(this.createReservationServiceMock).createReservation(ANY_RESERVATION_DTO);
  }

  @Test
  void givenAuthenticatedUser_whenCreateReservation_thenAssembleReservationResponse() {
    when(this.reservationRequestAssemblerMock.assemble(
            ANY_RESERVATION_REQUEST, ANY_AUTHENTICATED_USER_ID, ANY_TRIP_ID))
        .thenReturn(ANY_RESERVATION_DTO);
    when(this.createReservationServiceMock.createReservation(ANY_RESERVATION_DTO))
        .thenReturn(ANY_CREATED_RESERVATION_DTO);

    this.reservationCreationUseCase.createReservation(
        ANY_RESERVATION_REQUEST, ANY_AUTHENTICATED_USER_ID, ANY_TRIP_ID);

    verify(this.reservationResponseAssemblerMock).assembleConfirmation(ANY_CREATED_RESERVATION_DTO);
  }

  @Test
  void givenAuthenticatedUser_whenCreateReservation_thenReturnAssembledReservationResponse() {
    when(this.reservationRequestAssemblerMock.assemble(
            ANY_RESERVATION_REQUEST, ANY_AUTHENTICATED_USER_ID, ANY_TRIP_ID))
        .thenReturn(ANY_RESERVATION_DTO);
    when(this.createReservationServiceMock.createReservation(ANY_RESERVATION_DTO))
        .thenReturn(ANY_CREATED_RESERVATION_DTO);
    when(this.reservationResponseAssemblerMock.assembleConfirmation(ANY_CREATED_RESERVATION_DTO))
        .thenReturn(ANY_RESERVATION_RESPONSE);

    ReservationResponse returnedResponse =
        this.reservationCreationUseCase.createReservation(
            ANY_RESERVATION_REQUEST, ANY_AUTHENTICATED_USER_ID, ANY_TRIP_ID);

    assertEquals(ANY_RESERVATION_RESPONSE, returnedResponse);
  }

  private static InvoiceResponse createAnyInvoiceResponse() {
    Map<String, String> anyItems = new HashMap<>();
    Map<String, String> anyTaxes = new HashMap<>();
    String anyTotal = "200.50";
    List<PaymentTraceResponse> anyPaymentTraceResponses = new ArrayList<>();
    return new InvoiceResponse(
        ANY_INVOICE_ID.toString(),
        anyItems,
        anyTaxes,
        anyTotal,
        anyTotal,
        anyPaymentTraceResponses);
  }
}
