package ca.ulaval.glo4003.payment.exception;

import ca.ulaval.glo4003.exception.TrekULException;
import ca.ulaval.glo4003.money.Money;

public class PaymentLowerThanMinimumException extends TrekULException {
  public PaymentLowerThanMinimumException(Money limit) {
    super("PAYMENT_TOO_LOW", String.format("Payment should be at least %s", limit));
  }
}
