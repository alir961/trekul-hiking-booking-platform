package ca.ulaval.glo4003.payment;

import static org.junit.jupiter.api.Assertions.assertThrows;

import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.payment.exception.PaymentHigherThanMaximumException;
import ca.ulaval.glo4003.payment.exception.PaymentLowerThanMinimumException;
import ca.ulaval.glo4003.payment.exception.PaymentNegativeException;
import org.junit.jupiter.api.Test;

public class PaymentQuantityValidatorTest {
  private static final Money ANY_MAXIMUM_MONEY = new Money("1000");
  private static final Money ANY_MINIMUM_MONEY = new Money("100");
  private PaymentQuantityValidator paymentQuantityValidator = new PaymentQuantityValidator();

  @Test
  void givenPaymentInRange_whenValidatingPaymentInRange_thenThrowNothing() {
    Money moneyInRange = ANY_MAXIMUM_MONEY.subtract(new Money("10"));
    this.paymentQuantityValidator.validatePaymentInRange(
        ANY_MINIMUM_MONEY, ANY_MAXIMUM_MONEY, moneyInRange);
  }

  @Test
  void givenPaymentIsMaximumAllowed_whenValidatingPaymentInRange_thenThrowNothing() {
    this.paymentQuantityValidator.validatePaymentInRange(
        ANY_MINIMUM_MONEY, ANY_MAXIMUM_MONEY, ANY_MAXIMUM_MONEY);
  }

  @Test
  void givenPaymentIsMinimumAllowed_whenValidatingPaymentInRange_thenThrowNothing() {
    this.paymentQuantityValidator.validatePaymentInRange(
        ANY_MINIMUM_MONEY, ANY_MAXIMUM_MONEY, ANY_MINIMUM_MONEY);
  }

  @Test
  void
      givenPaymentIsHigherThanMaximumAllowed_whenValidatingPaymentInRange_thenThrowPaymentHighThanMaximumException() {
    Money moneyTooHigh = ANY_MAXIMUM_MONEY.add(new Money("10"));

    assertThrows(
        PaymentHigherThanMaximumException.class,
        () ->
            this.paymentQuantityValidator.validatePaymentInRange(
                ANY_MINIMUM_MONEY, ANY_MAXIMUM_MONEY, moneyTooHigh));
  }

  @Test
  void
      givenPaymentIsLowerThanMinimumAllowed_whenValidatingPaymentInRange_thenThrowPaymentLowerThanMinimumException() {
    Money moneyTooLow = ANY_MINIMUM_MONEY.subtract(new Money("10"));

    assertThrows(
        PaymentLowerThanMinimumException.class,
        () ->
            this.paymentQuantityValidator.validatePaymentInRange(
                ANY_MINIMUM_MONEY, ANY_MAXIMUM_MONEY, moneyTooLow));
  }

  @Test
  void givenPaymentIsZero_whenValidatingPaymentInRange_thenThrowPaymentNegativeException() {
    Money zeroMoney = new Money();

    assertThrows(
        PaymentNegativeException.class,
        () ->
            this.paymentQuantityValidator.validatePaymentInRange(
                ANY_MINIMUM_MONEY, ANY_MAXIMUM_MONEY, zeroMoney));
  }

  @Test
  void givenPaymentIsNegative_whenValidatingPaymentInRange_thenThrowPaymentNegativeException() {
    Money negativeMoney = new Money("-10");

    assertThrows(
        PaymentNegativeException.class,
        () ->
            this.paymentQuantityValidator.validatePaymentInRange(
                ANY_MINIMUM_MONEY, ANY_MAXIMUM_MONEY, negativeMoney));
  }
}
