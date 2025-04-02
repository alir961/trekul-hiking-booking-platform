package ca.ulaval.glo4003.payment.assembler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4003.invoice.assembler.InvoiceResponseAssembler;
import ca.ulaval.glo4003.invoice.dto.InvoiceDto;
import ca.ulaval.glo4003.invoice.response.InvoiceResponse;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.payment.dto.PaymentConfirmationDto;
import ca.ulaval.glo4003.payment.response.PaymentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaymentResponseAssemblerTest {
  private static final InvoiceDto INVOICE_DTO_DUMMY = mock(InvoiceDto.class);
  private static final InvoiceResponse INVOICE_RESPONSE_DUMMY = mock(InvoiceResponse.class);
  private static final String PAYMENT = "100.00";
  private static final String TOTAL_PAID = "150.25";
  private static final String TOTAL_LEFT_TO_PAY = "115.25";
  private static final PaymentConfirmationDto ANY_RESERVATION_CONFIRMATION_DTO =
      new PaymentConfirmationDto(
          new Money(PAYMENT),
          new Money(TOTAL_PAID),
          new Money(TOTAL_LEFT_TO_PAY),
          INVOICE_DTO_DUMMY);
  private InvoiceResponseAssembler invoiceResponseAssemblerMock =
      mock(InvoiceResponseAssembler.class);
  private PaymentResponseAssembler paymentResponseAssembler;

  @BeforeEach
  public void setUp() {
    when(this.invoiceResponseAssemblerMock.assemble(INVOICE_DTO_DUMMY))
        .thenReturn(INVOICE_RESPONSE_DUMMY);
    this.paymentResponseAssembler = new PaymentResponseAssembler(this.invoiceResponseAssemblerMock);
  }

  @Test
  void givenReservationConfirmed_whenAssemble_thenReturnsPaymentResponseWithCorrectValues() {
    PaymentResponse response =
        this.paymentResponseAssembler.assemble(ANY_RESERVATION_CONFIRMATION_DTO);

    assertEquals(PAYMENT, response.paymentReceived());
    assertEquals(TOTAL_PAID, response.totalPaid());
    assertEquals(TOTAL_LEFT_TO_PAY, response.totalLeft());
    assertSame(INVOICE_RESPONSE_DUMMY, response.invoice());
  }
}
