package ca.ulaval.glo4003.payment;

import static ca.ulaval.glo4003.payment.PaymentMethod.CREDIT;

import ca.ulaval.glo4003.payment.exception.PaymentCheckoutException;
import java.time.LocalDate;

public class FakePaymentApi implements PaymentApi {
  @Override
  public PaymentTrace checkout(PaymentInformation paymentInformation) {
    if (this.isCardExpired(paymentInformation)) {
      throw new PaymentCheckoutException();
    }
    return new PaymentTrace(LocalDate.now(), paymentInformation.payment(), CREDIT);
  }

  private boolean isCardExpired(PaymentInformation paymentInformation) {
    return LocalDate.now().isAfter(paymentInformation.expirationDate());
  }
}
