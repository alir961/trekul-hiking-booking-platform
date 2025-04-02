package ca.ulaval.glo4003.payment.assembler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.invoice.TripInvoice;
import ca.ulaval.glo4003.invoice.assembler.InvoiceDtoAssembler;
import ca.ulaval.glo4003.invoice.dto.InvoiceDto;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.payment.PaymentInformation;
import ca.ulaval.glo4003.payment.dto.PaymentConfirmationDto;
import ca.ulaval.glo4003.traveler.TravelerID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PaymentConfirmationDtoAssemblerTest {
  private static final TravelerID ANY_TRAVELER_ID = new TravelerID("221023");
  private static final Money BALANCE_PAID = new Money("770");
  private static final Money BALANCE_TO_PAY = new Money("45021,2");
  private static final Money ANY_PAYMENT = new Money("100.00");
  private static final InvoiceDto DUMMY_INVOICE_DTO = mock(InvoiceDto.class);

  private TripInvoice tripInvoiceMock;
  private PaymentConfirmationDtoAssembler paymentConfirmationDtoAssembler;
  private PaymentInformation paymentInformationMock;

  @BeforeEach
  void setUp() {
    this.tripInvoiceMock = mock(TripInvoice.class);
    when(this.tripInvoiceMock.getBalancePaid(ANY_TRAVELER_ID)).thenReturn(BALANCE_PAID);
    when(this.tripInvoiceMock.calculateBalanceToPay(ANY_TRAVELER_ID)).thenReturn(BALANCE_TO_PAY);

    InvoiceDtoAssembler invoiceDtoAssemblerMock = mock(InvoiceDtoAssembler.class);
    when(invoiceDtoAssemblerMock.assemble(this.tripInvoiceMock, ANY_TRAVELER_ID))
        .thenReturn(DUMMY_INVOICE_DTO);
    this.paymentConfirmationDtoAssembler =
        new PaymentConfirmationDtoAssembler(invoiceDtoAssemblerMock);

    this.paymentInformationMock = mock(PaymentInformation.class);
    when(this.paymentInformationMock.payment()).thenReturn(ANY_PAYMENT);
  }

  @Test
  void whenAssembling_thenReservationConfirmationDtoAssemblesCorrectly() {
    PaymentConfirmationDto paymentConfirmationDto =
        this.paymentConfirmationDtoAssembler.assemble(
            this.paymentInformationMock, this.tripInvoiceMock, ANY_TRAVELER_ID);

    assertEquals(ANY_PAYMENT, paymentConfirmationDto.paymentReceived());
    assertEquals(BALANCE_PAID, paymentConfirmationDto.totalPaid());
    assertEquals(BALANCE_TO_PAY, paymentConfirmationDto.totalLeft());
    assertSame(DUMMY_INVOICE_DTO, paymentConfirmationDto.invoice());
  }
}
