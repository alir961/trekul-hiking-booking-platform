package ca.ulaval.glo4003.payment.usecase;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4003.invoice.InvoiceID;
import ca.ulaval.glo4003.invoice.dto.InvoiceDto;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.payment.PaymentInformation;
import ca.ulaval.glo4003.payment.PaymentService;
import ca.ulaval.glo4003.payment.assembler.PaymentRequestAssembler;
import ca.ulaval.glo4003.payment.assembler.PaymentResponseAssembler;
import ca.ulaval.glo4003.payment.dto.PaymentConfirmationDto;
import ca.ulaval.glo4003.payment.request.PaymentRequest;
import ca.ulaval.glo4003.traveler.TravelerID;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PaymentUseCaseTest {

  private static final TravelerID ANY_PAYER_USER_ID = new TravelerID("userID");
  private static final TravelerID ANY_OWNER_USER_ID = new TravelerID("anyUserID");
  private static final InvoiceID ANY_INVOICE_ID = InvoiceID.generate();
  private static final String ANY_CARD_NUMBER = "123";
  private static final Integer ANY_CARD_CCV = 421;
  private static final String ANY_CARD_HOLDER_NAME = "Jo";
  private static final LocalDate ANY_EXPIRATION_DATE = LocalDate.now();
  private static final Double ANY_PAYMENT_STRING = 67.8;
  private static final PaymentRequest ANY_PAYMENT_REQUEST =
      new PaymentRequest(
          ANY_CARD_NUMBER,
          ANY_EXPIRATION_DATE.toString(),
          ANY_CARD_CCV,
          ANY_CARD_HOLDER_NAME,
          ANY_PAYMENT_STRING);
  private static final Money ANY_PAYMENT = new Money(ANY_PAYMENT_STRING);
  private static final PaymentInformation ANY_PAYMENT_INFORMATION =
      new PaymentInformation(
          ANY_CARD_NUMBER, ANY_CARD_HOLDER_NAME, ANY_CARD_CCV, ANY_EXPIRATION_DATE, ANY_PAYMENT);
  private static final InvoiceDto ANY_INVOICE_DTO = mock(InvoiceDto.class);
  private static final PaymentConfirmationDto ANY_RESERVATION_CONFIRMATION_DTO =
      new PaymentConfirmationDto(ANY_PAYMENT, ANY_PAYMENT, ANY_PAYMENT, ANY_INVOICE_DTO);
  private PaymentService paymentServiceMock;
  private PaymentRequestAssembler paymentRequestAssemblerMock;
  private PaymentResponseAssembler paymentResponseAssemblerMock;
  private PaymentUseCase paymentUseCase;

  @BeforeEach
  void setUp() {
    this.paymentServiceMock = mock(PaymentService.class);
    this.paymentRequestAssemblerMock = mock(PaymentRequestAssembler.class);
    this.paymentResponseAssemblerMock = mock(PaymentResponseAssembler.class);
    this.paymentUseCase =
        new PaymentUseCase(
            this.paymentServiceMock,
            this.paymentRequestAssemblerMock,
            this.paymentResponseAssemblerMock);
  }

  @Test
  void whenPayingInvoice_thenAssemblePaymentCheckoutDto() {
    this.paymentUseCase.payInvoice(
        ANY_PAYMENT_REQUEST, ANY_INVOICE_ID, ANY_OWNER_USER_ID, ANY_PAYER_USER_ID);

    verify(this.paymentRequestAssemblerMock).assemble(ANY_PAYMENT_REQUEST);
  }

  @Test
  void whenPayingInvoice_thenConfirmReservation() {
    when(this.paymentRequestAssemblerMock.assemble(ANY_PAYMENT_REQUEST))
        .thenReturn(ANY_PAYMENT_INFORMATION);

    this.paymentUseCase.payInvoice(
        ANY_PAYMENT_REQUEST, ANY_INVOICE_ID, ANY_OWNER_USER_ID, ANY_PAYER_USER_ID);

    verify(this.paymentServiceMock)
        .payInvoice(ANY_PAYMENT_INFORMATION, ANY_INVOICE_ID, ANY_OWNER_USER_ID, ANY_PAYER_USER_ID);
  }

  @Test
  void whenPayingInvoice_thenAssemblePaymentResponse() {
    when(this.paymentRequestAssemblerMock.assemble(ANY_PAYMENT_REQUEST))
        .thenReturn(ANY_PAYMENT_INFORMATION);
    when(this.paymentServiceMock.payInvoice(
            ANY_PAYMENT_INFORMATION, ANY_INVOICE_ID, ANY_OWNER_USER_ID, ANY_PAYER_USER_ID))
        .thenReturn(ANY_RESERVATION_CONFIRMATION_DTO);

    this.paymentUseCase.payInvoice(
        ANY_PAYMENT_REQUEST, ANY_INVOICE_ID, ANY_OWNER_USER_ID, ANY_PAYER_USER_ID);

    verify(this.paymentResponseAssemblerMock).assemble(ANY_RESERVATION_CONFIRMATION_DTO);
  }
}
