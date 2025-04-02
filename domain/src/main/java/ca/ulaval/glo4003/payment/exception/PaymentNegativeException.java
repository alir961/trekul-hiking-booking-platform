package ca.ulaval.glo4003.payment.exception;

import ca.ulaval.glo4003.exception.TrekULException;

public class PaymentNegativeException extends TrekULException {
  public PaymentNegativeException() {
    super("PAYMENT_NEGATIVE", "Payment must be strictly positive");
  }
}
