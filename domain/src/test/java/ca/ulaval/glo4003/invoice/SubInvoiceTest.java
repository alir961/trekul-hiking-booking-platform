package ca.ulaval.glo4003.invoice;

import static ca.ulaval.glo4003.invoice.BillableItems.CREDIT;
import static ca.ulaval.glo4003.invoice.BillableItems.PENALTY;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.payment.PaymentMethod;
import ca.ulaval.glo4003.payment.PaymentTrace;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SubInvoiceTest {
  private static final PaymentMethod ANY_PAYMENT_METHOD = PaymentMethod.CREDIT;
  private static final Money ZERO_MONEY = new Money();
  private static final Money ANY_MONEY = new Money("504.32");
  private static final double ANY_PERCENTAGE = 0.5;
  private static final BigDecimal ANY_BIG_DECIMAL_PERCENTAGE = new BigDecimal(ANY_PERCENTAGE);
  private SubInvoice subInvoice;

  @BeforeEach
  void setUp() {
    this.subInvoice = new SubInvoice();
  }

  @Test
  void whenCreatingInvoice_thenInvoiceIsEmpty() {
    assertTrue(this.subInvoice.getItemsCostWithoutTaxes().isEmpty());
    assertTrue(this.subInvoice.getPaymentList().isEmpty());
    assertTrue(this.subInvoice.isFirstPayment());
    assertEquals(ZERO_MONEY, this.subInvoice.calculateTotalWithoutTaxes());
    assertEquals(ZERO_MONEY, this.subInvoice.getBalancePaid());
    assertEquals(ZERO_MONEY, this.subInvoice.getMinimumToPay());
  }

  @Test
  void whenSavingItem_thenItemIsAdded() {
    BillableItems addedItem = BillableItems.TRIP;
    Money addedItemCost = new Money("44003");
    this.subInvoice.saveItem(addedItem, addedItemCost);

    Map<BillableItems, Money> result = this.subInvoice.getItemsCostWithoutTaxes();
    assertTrue(result.containsKey(addedItem));
    assertEquals(addedItemCost, result.get(addedItem));
  }

  @Test
  void whenAddingPayment_thenBalancePaidIsAddedSameAmount() {
    Money newPayment = new Money("860.45");
    PaymentTrace paymentTrace = new PaymentTrace(LocalDate.now(), newPayment, ANY_PAYMENT_METHOD);
    this.subInvoice.addPayment(paymentTrace);
    Money result = this.subInvoice.getBalancePaid();

    assertEquals(newPayment, result);
  }

  @Test
  void givenFirstPayment_whenAddingPayment_thenInvoiceFirstPaymentIsNowDone() {
    Money newPayment = new Money("860.45");
    PaymentTrace paymentTrace = new PaymentTrace(LocalDate.now(), newPayment, ANY_PAYMENT_METHOD);
    this.subInvoice.addPayment(paymentTrace);

    assertFalse(this.subInvoice.isFirstPayment());
  }

  @Test
  void whenCopying_thenNewInvoiceIsNotTheSameObjectAsOld() {
    SubInvoice copied = this.subInvoice.copy();

    assertNotSame(this.subInvoice, copied);
  }

  @Test
  void whenCopying_thenNewInvoiceHasBalancePaidEqualToOld() {
    PaymentTrace paymentTrace = new PaymentTrace(LocalDate.now(), ANY_MONEY, ANY_PAYMENT_METHOD);
    this.subInvoice.addPayment(paymentTrace);
    SubInvoice copied = this.subInvoice.copy();

    assertEquals(this.subInvoice.getBalancePaid(), copied.getBalancePaid());
  }

  @Test
  void whenCopying_thenNewInvoiceHasItemsEqualToOld() {
    this.subInvoice.saveItem(BillableItems.RESERVATION, ANY_MONEY);
    SubInvoice copied = this.subInvoice.copy();

    assertEquals(this.subInvoice.getItemsCostWithoutTaxes(), copied.getItemsCostWithoutTaxes());
    assertNotSame(this.subInvoice.getItemsCostWithoutTaxes(), copied.getItemsCostWithoutTaxes());
  }

  @Test
  void givenPositiveCredit_whenAddingCredit_thenCreditItemIsAddedAsNegativeCost() {
    Money positiveCredit = new Money("403");
    this.subInvoice.addCredit(positiveCredit);
    Money result = this.subInvoice.getItem(CREDIT);
    assertEquals(positiveCredit.asNegative(), result);
  }

  @Test
  void givenNegativeCredit_whenAddingCredit_thenCreditItemIsKeptAsNegative() {
    Money negativeCredit = new Money("-6045");
    this.subInvoice.addCredit(negativeCredit);
    Money result = this.subInvoice.getItem(CREDIT);
    assertEquals(negativeCredit, result);
  }

  @Test
  void givenCreditAlreadyExists_whenAddingCredit_thenCreditsAreAdded() {
    Money firstCredit = new Money("20");
    Money secondCredit = new Money("40");
    Money expectedCredit = new Money("-60");
    this.subInvoice.addCredit(firstCredit);
    this.subInvoice.addCredit(secondCredit);
    Money result = this.subInvoice.getItem(CREDIT);
    assertEquals(expectedCredit, result);
  }

  @Test
  void givenPaymentsAlreadyDone_whenRefund_thenRefundsWithGivenPercentageExceptForReservation() {
    Money tripCost = new Money("440");
    this.givenPaymentsAlreadyDone(tripCost);

    this.subInvoice.refund(ANY_PERCENTAGE);

    Money expectedPenalty = tripCost.multiply(ANY_BIG_DECIMAL_PERCENTAGE);
    Money actualRefund = this.subInvoice.getItemsCostWithoutTaxes().get(CREDIT);
    Money actualPenalty = this.subInvoice.getItemsCostWithoutTaxes().get(PENALTY);
    assertEquals(tripCost.asNegative(), actualRefund);
    assertEquals(expectedPenalty, actualPenalty);
  }

  private void givenPaymentsAlreadyDone(Money tripCost) {
    BillableItems tripBillable = BillableItems.TRIP;
    BillableItems reservationBillable = BillableItems.RESERVATION;
    Money reservationCost = new Money("80");
    this.subInvoice.saveItem(tripBillable, tripCost);
    this.subInvoice.saveItem(reservationBillable, reservationCost);
    PaymentTrace paymentTrace =
        new PaymentTrace(LocalDate.now(), tripCost.add(reservationCost), ANY_PAYMENT_METHOD);
    this.subInvoice.addPayment(paymentTrace);
  }

  @Test
  void whenCalculateTotalPaid_thenReturnCorrectAmount() {
    Money firstPayment = new Money("50");
    Money secondPayment = new Money("60");
    PaymentTrace firstTrace = new PaymentTrace(LocalDate.now(), firstPayment, PaymentMethod.CREDIT);
    PaymentTrace secondTrace =
        new PaymentTrace(LocalDate.now(), secondPayment, PaymentMethod.CREDIT);
    this.subInvoice.addPayment(firstTrace);
    this.subInvoice.addPayment(secondTrace);

    Money total = this.subInvoice.calculateTotalPaid();

    Money expected = firstPayment.add(secondPayment);
    assertEquals(expected, total);
  }
}
