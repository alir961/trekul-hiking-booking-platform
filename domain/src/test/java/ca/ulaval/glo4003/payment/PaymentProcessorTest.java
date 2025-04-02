package ca.ulaval.glo4003.payment;

import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.invoice.TripInvoice;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.traveler.TravelerID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

public class PaymentProcessorTest {
  private static final Money INVOICE_MINIMUM_TO_PAY = new Money("4023");
  private static final Money INVOICE_MAXIMUM_PAYABLE = new Money("43230");
  private static final Money ANY_PAYMENT = new Money("4654");
  private static final PaymentInformation ANY_PAYMENT_INFORMATION_MOCK =
      mock(PaymentInformation.class);
  private static final PaymentTrace CONFIRMED_PAYMENT_TRACE = mock(PaymentTrace.class);
  private static final TravelerID ANY_TRAVELER_ID = new TravelerID("42t02");
  private TripInvoice tripInvoiceMock;
  private PaymentQuantityValidator paymentQuantityValidatorMock;
  private PaymentApi paymentApiMock;
  private PaymentProcessor paymentProcessor;

  @BeforeAll
  static void setAll() {
    when(ANY_PAYMENT_INFORMATION_MOCK.payment()).thenReturn(ANY_PAYMENT);
  }

  @BeforeEach
  void setUp() {
    this.tripInvoiceMock = mock(TripInvoice.class);
    this.paymentApiMock = mock(PaymentApi.class);
    this.paymentQuantityValidatorMock = mock(PaymentQuantityValidator.class);
    this.paymentProcessor =
        new PaymentProcessor(this.paymentQuantityValidatorMock, this.paymentApiMock);

    when(this.tripInvoiceMock.calculateMinimumToPay(ANY_TRAVELER_ID))
        .thenReturn(INVOICE_MINIMUM_TO_PAY);
    when(this.tripInvoiceMock.calculateBalanceToPay(ANY_TRAVELER_ID))
        .thenReturn(INVOICE_MAXIMUM_PAYABLE);
    when(this.paymentApiMock.checkout(ANY_PAYMENT_INFORMATION_MOCK))
        .thenReturn(CONFIRMED_PAYMENT_TRACE);
  }

  @Test
  void whenProcessingPayment_thenPaymentTraceIsAddedToInvoiceAfterCheckout() {
    this.paymentProcessor.processPayment(
        ANY_PAYMENT_INFORMATION_MOCK, this.tripInvoiceMock, ANY_TRAVELER_ID);

    InOrder inOrder = inOrder(this.paymentApiMock, this.tripInvoiceMock);

    inOrder.verify(this.paymentApiMock).checkout(ANY_PAYMENT_INFORMATION_MOCK);
    inOrder.verify(this.tripInvoiceMock).addPayment(CONFIRMED_PAYMENT_TRACE, ANY_TRAVELER_ID);
  }

  @Test
  void whenProcessingPayment_thenPaymentIsValidatedWithMinimumNeededToBePaidAndMaximumPayable() {
    this.paymentProcessor.processPayment(
        ANY_PAYMENT_INFORMATION_MOCK, this.tripInvoiceMock, ANY_TRAVELER_ID);

    verify(this.paymentQuantityValidatorMock)
        .validatePaymentInRange(INVOICE_MINIMUM_TO_PAY, INVOICE_MAXIMUM_PAYABLE, ANY_PAYMENT);
  }
}
