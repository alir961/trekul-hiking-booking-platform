package ca.ulaval.glo4003.payment.exception;

import ca.ulaval.glo4003.exception.TrekULException;
import ca.ulaval.glo4003.money.Money;

public class PaymentHigherThanMaximumException extends TrekULException {
  public PaymentHigherThanMaximumException(Money limit) {
    super("PAYMENT_TOO_HIGH", String.format("Payment should not exceed %s", limit));
  }
}
