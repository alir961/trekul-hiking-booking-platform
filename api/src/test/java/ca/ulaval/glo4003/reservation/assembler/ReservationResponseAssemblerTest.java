package ca.ulaval.glo4003.reservation.assembler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4003.group.Group;
import ca.ulaval.glo4003.group.assembler.GroupResponseAssembler;
import ca.ulaval.glo4003.group.reponse.GroupResponse;
import ca.ulaval.glo4003.invoice.assembler.InvoiceResponseAssembler;
import ca.ulaval.glo4003.invoice.dto.InvoiceDto;
import ca.ulaval.glo4003.invoice.response.InvoiceResponse;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.reservation.ReservationID;
import ca.ulaval.glo4003.reservation.ReservationStatus;
import ca.ulaval.glo4003.reservation.dto.CreatedReservationDto;
import ca.ulaval.glo4003.reservation.response.ReservationResponse;
import ca.ulaval.glo4003.transport.TransportType;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ReservationResponseAssemblerTest {
  private static final InvoiceDto INVOICE_DTO_DUMMY = mock(InvoiceDto.class);
  private static final InvoiceResponse INVOICE_RESPONSE_DUMMY = mock(InvoiceResponse.class);
  private static final GroupResponse GROUP_RESPONSE_DUMMY = mock(GroupResponse.class);
  private static final ZonedDateTime RESERVATION_EXPIRATION = ZonedDateTime.now();
  private static final Money RESERVATION_MINIMAL_PAYMENT = new Money("12.5");
  private static final ReservationID ANY_RESERVATION_ID = ReservationID.generate();
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
  InvoiceResponseAssembler invoiceResponseAssemblerMock;
  GroupResponseAssembler groupResponseAssemblerMock;
  private ReservationResponseAssembler reservationResponseAssembler;

  @BeforeEach
  void setUp() {
    this.invoiceResponseAssemblerMock = mock(InvoiceResponseAssembler.class);
    this.groupResponseAssemblerMock = mock(GroupResponseAssembler.class);
    when(this.invoiceResponseAssemblerMock.assemble(INVOICE_DTO_DUMMY))
        .thenReturn(INVOICE_RESPONSE_DUMMY);
    when(this.groupResponseAssemblerMock.assemble(ANY_GROUP)).thenReturn(GROUP_RESPONSE_DUMMY);
    this.reservationResponseAssembler =
        new ReservationResponseAssembler(
            this.invoiceResponseAssemblerMock, this.groupResponseAssemblerMock);
  }

  @Test
  void whenAssembling_thenReservationResponseAssembleWithMinimalPaymentFromReservation() {
    ReservationResponse response =
        this.reservationResponseAssembler.assembleConfirmation(ANY_CREATED_RESERVATION_DTO);

    assertEquals(RESERVATION_MINIMAL_PAYMENT.toString(), response.minimumToPay());
  }

  @Test
  void whenAssembling_thenReservationResponseAssembleWithInvoiceFromReservation() {
    ReservationResponse response =
        this.reservationResponseAssembler.assembleConfirmation(ANY_CREATED_RESERVATION_DTO);

    assertSame(INVOICE_RESPONSE_DUMMY, response.invoice());
  }

  @Test
  void whenAssembling_thenReservationResponseAssembleWithGroupFromReservation() {
    ReservationResponse response =
        this.reservationResponseAssembler.assembleConfirmation(ANY_CREATED_RESERVATION_DTO);

    assertSame(GROUP_RESPONSE_DUMMY, response.group());
  }

  @Test
  void whenAssembling_thenReservationResponseAssembleWithExpirationFromReservation() {
    ReservationResponse response =
        this.reservationResponseAssembler.assembleConfirmation(ANY_CREATED_RESERVATION_DTO);

    assertEquals(RESERVATION_EXPIRATION, response.expiration());
  }
}
