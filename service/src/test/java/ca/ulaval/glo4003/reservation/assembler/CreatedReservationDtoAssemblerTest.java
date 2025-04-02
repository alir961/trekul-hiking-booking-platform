package ca.ulaval.glo4003.reservation.assembler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4003.invoice.TripInvoice;
import ca.ulaval.glo4003.invoice.assembler.InvoiceDtoAssembler;
import ca.ulaval.glo4003.invoice.dto.InvoiceDto;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.reservation.Reservation;
import ca.ulaval.glo4003.reservation.dto.CreatedReservationDto;
import ca.ulaval.glo4003.traveler.TravelerID;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CreatedReservationDtoAssemblerTest {
  private static final TravelerID ANY_TRAVELER_ID = new TravelerID("221023");
  private static final Money RESERVATION_INVOICE_COST = new Money("344.3");
  private static final ZonedDateTime RESERVATION_EXPIRATION = ZonedDateTime.now();
  private static final InvoiceDto DUMMY_INVOICE_DTO = mock(InvoiceDto.class);
  private Reservation reservationMock;
  private TripInvoice reservationInvoiceMock;
  private InvoiceDtoAssembler invoiceDtoAssemblerMock = mock(InvoiceDtoAssembler.class);
  private CreatedReservationDtoAssembler createdReservationDtoAssembler;

  @BeforeEach
  void setUp() {
    this.reservationInvoiceMock = mock(TripInvoice.class);
    when(this.reservationInvoiceMock.calculateMinimumToPay(ANY_TRAVELER_ID))
        .thenReturn(RESERVATION_INVOICE_COST);

    this.reservationMock = mock(Reservation.class);
    when(this.reservationMock.getInvoice()).thenReturn(this.reservationInvoiceMock);
    when(this.reservationMock.getExpiration()).thenReturn(RESERVATION_EXPIRATION);
    when(this.reservationMock.getUserID()).thenReturn(ANY_TRAVELER_ID);

    when(this.invoiceDtoAssemblerMock.assemble(this.reservationInvoiceMock, ANY_TRAVELER_ID))
        .thenReturn(DUMMY_INVOICE_DTO);

    this.createdReservationDtoAssembler =
        new CreatedReservationDtoAssembler(this.invoiceDtoAssemblerMock);
  }

  @Test
  void whenAssembling_thenCreatedReservationDtoContainsReservationInvoiceCostAsMinimumDue() {
    CreatedReservationDto createdReservationDto =
        this.createdReservationDtoAssembler.assemble(this.reservationMock);

    assertEquals(RESERVATION_INVOICE_COST, createdReservationDto.minimumDue());
  }

  @Test
  void whenAssembling_thenCreatedReservationDtoContainsInvoiceDtoFromAssembler() {
    CreatedReservationDto createdReservationDto =
        this.createdReservationDtoAssembler.assemble(this.reservationMock);

    assertSame(DUMMY_INVOICE_DTO, createdReservationDto.invoiceDto());
  }
}
