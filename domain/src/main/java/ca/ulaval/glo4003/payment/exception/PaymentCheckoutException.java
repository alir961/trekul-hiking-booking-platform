package ca.ulaval.glo4003.payment.exception;

import ca.ulaval.glo4003.exception.TrekULException;

public class PaymentCheckoutException extends TrekULException {
  public PaymentCheckoutException() {
    super("PAYMENT_CHECKOUT_FAILED", "Checkout could not be processed. Verify with your bank");
  }
}
