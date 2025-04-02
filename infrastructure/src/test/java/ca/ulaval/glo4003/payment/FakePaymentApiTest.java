package ca.ulaval.glo4003.payment;

import static org.junit.jupiter.api.Assertions.*;

import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.payment.exception.PaymentCheckoutException;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

public class FakePaymentApiTest {
  private static final String ANY_CARD_NUMBER = "123 123 123 132";
  private static final String ANY_CARD_HOLDER_NAME = "Bob";
  private static final Integer ANY_CCV = 123;
  private static final Money ANY_PAYMENT = new Money();
  private static final int CURRENT_YEAR = LocalDate.now().getYear();
  private static final LocalDate PAST_EXPIRATION = LocalDate.of(CURRENT_YEAR, 10, 1);
  private static final LocalDate FUTURE_EXPIRATION = LocalDate.now().plusYears(1);
  private static final PaymentInformation EXPIRED_CREDIT_CARD_INFO =
      new PaymentInformation(
          ANY_CARD_NUMBER, ANY_CARD_HOLDER_NAME, ANY_CCV, PAST_EXPIRATION, ANY_PAYMENT);
  private static final PaymentInformation VALID_CREDIT_CARD_INFO =
      new PaymentInformation(
          ANY_CARD_NUMBER, ANY_CARD_HOLDER_NAME, ANY_CCV, FUTURE_EXPIRATION, ANY_PAYMENT);
  private final PaymentApi paymentApi = new FakePaymentApi();

  @Test
  void givenCreditCardIsExpired_whenCheckout_thenThrowPaymentCheckoutException() {
    assertThrows(
        PaymentCheckoutException.class, () -> this.paymentApi.checkout(EXPIRED_CREDIT_CARD_INFO));
  }

  @Test
  void givenCreditCardHasNotExpired_whenCheckout_thenPaymentTraceContainsSamePaymentFromInput() {
    PaymentTrace paymentTrace = this.paymentApi.checkout(VALID_CREDIT_CARD_INFO);

    assertSame(paymentTrace.paid(), ANY_PAYMENT);
  }
}
