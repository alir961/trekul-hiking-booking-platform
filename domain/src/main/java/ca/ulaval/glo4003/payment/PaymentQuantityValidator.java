package ca.ulaval.glo4003.payment;

import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.payment.exception.PaymentHigherThanMaximumException;
import ca.ulaval.glo4003.payment.exception.PaymentLowerThanMinimumException;
import ca.ulaval.glo4003.payment.exception.PaymentNegativeException;

public class PaymentQuantityValidator {
  public void validatePaymentInRange(Money minimum, Money maximum, Money payment) {
    this.validatePaymentIsPositive(payment);
    this.validateHigherThanMinimum(minimum, payment);
    this.validateLowerThanMaximum(maximum, payment);
  }

  private void validatePaymentIsPositive(Money payment) {
    if (!payment.isStrictlyPositive()) {
      throw new PaymentNegativeException();
    }
  }

  private void validateHigherThanMinimum(Money minimum, Money payment) {
    if (!minimum.isLowerOrEqualThan(payment)) {
      throw new PaymentLowerThanMinimumException(minimum);
    }
  }

  private void validateLowerThanMaximum(Money maximum, Money payment) {
    if (!payment.isLowerOrEqualThan(maximum)) {
      throw new PaymentHigherThanMaximumException(maximum);
    }
  }
}
