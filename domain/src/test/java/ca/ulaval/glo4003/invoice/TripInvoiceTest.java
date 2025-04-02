package ca.ulaval.glo4003.invoice;

import static ca.ulaval.glo4003.payment.PaymentMethod.CREDIT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.payment.PaymentMethod;
import ca.ulaval.glo4003.payment.PaymentTrace;
import ca.ulaval.glo4003.reservation.DurationCancellationPenalty;
import ca.ulaval.glo4003.tax.TaxStrategy;
import ca.ulaval.glo4003.tax.Taxes;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.trip.TripID;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TripInvoiceTest {
  private static final PaymentMethod ANY_PAYMENT_METHOD = CREDIT;
  private static final Money ZERO_MONEY = new Money();
  private static final Taxes ZERO_TAXES = new Taxes(ZERO_MONEY);
  private static final Money ANY_MONEY = new Money("504.32");
  private static final Money BEFORE_TAXES = new Money("28512");
  private static final Money AFTER_TAXES_TOTAL = new Money("124012");
  private static final Taxes TAXES_STUB = mock(Taxes.class);
  private static final InvoiceID ANY_INVOICE_ID = InvoiceID.generate();
  private static final TravelerID ANY_USER_ID = new TravelerID("120dsa");
  private static final TripID ANY_TRIP_ID = TripID.generate();
  private static final List<TravelerID> EMPTY_GUESTS_LIST = new ArrayList<>();
  private static final LocalDate ANY_TRIP_DATE = LocalDate.now();
  private static final LocalDateTime ANY_TRIP_DATE_TIME = ANY_TRIP_DATE.atStartOfDay();
  private static final double ANY_PENALTY_PERCENTAGE = 0.5;
  private TaxStrategy taxStrategyMock;
  private DurationCancellationPenalty durationCancellationPenaltyMock;
  private TripInvoice tripInvoice;

  @BeforeEach
  void setUp() {
    when(TAXES_STUB.getTotal()).thenReturn(AFTER_TAXES_TOTAL);
    this.taxStrategyMock = mock(TaxStrategy.class);
    this.durationCancellationPenaltyMock = mock(DurationCancellationPenalty.class);
    when(this.taxStrategyMock.calculateTax(BEFORE_TAXES)).thenReturn(TAXES_STUB);
    when(this.taxStrategyMock.calculateTax(BEFORE_TAXES)).thenReturn(TAXES_STUB);
    when(this.taxStrategyMock.calculateTax(ZERO_MONEY)).thenReturn(ZERO_TAXES);
    this.tripInvoice =
        new TripInvoice(
            ANY_INVOICE_ID,
            ANY_USER_ID,
            ANY_TRIP_ID,
            this.taxStrategyMock,
            this.durationCancellationPenaltyMock,
            EMPTY_GUESTS_LIST);
  }

  @Test
  void whenCreatingInvoice_thenInvoiceIsEmpty() {
    assertTrue(this.tripInvoice.getItems(ANY_USER_ID).isEmpty());
    assertEquals(ZERO_MONEY, this.tripInvoice.calculateTotalAfterTaxes(ANY_USER_ID));
    assertEquals(ZERO_MONEY, this.tripInvoice.getBalancePaid(ANY_USER_ID));
  }

  @Test
  void whenSavingItem_thenItemIsAdded() {
    BillableItems addedItem = BillableItems.TRIP;
    Money addedItemCost = new Money("44003");
    this.tripInvoice.saveItem(addedItem, addedItemCost);

    Map<BillableItems, Money> result = this.tripInvoice.getItems(ANY_USER_ID);
    assertTrue(result.containsKey(addedItem));
    assertEquals(addedItemCost, result.get(addedItem));
  }

  @Test
  void whenCalculatingTotalAfterTaxes_thenTotalAfterTaxesIsRecalculated() {
    this.tripInvoice.saveItem(BillableItems.RESERVATION, BEFORE_TAXES);
    Money result = this.tripInvoice.calculateTotalAfterTaxes(ANY_USER_ID);
    assertEquals(AFTER_TAXES_TOTAL, result);
  }

  @Test
  void whenAddingPayment_thenBalancePaidIsAddedSameAmount() {
    Money newPayment = new Money("860.45");
    PaymentTrace paymentTrace = new PaymentTrace(LocalDate.now(), newPayment, ANY_PAYMENT_METHOD);
    this.tripInvoice.addPayment(paymentTrace, ANY_USER_ID);
    Money result = this.tripInvoice.getBalancePaid(ANY_USER_ID);

    assertEquals(newPayment, result);
  }

  @Test
  void whenAddingPayment_thenBalanceToPayIsSubtractedSameAmount() {
    Money newPayment = new Money("100");
    Money after = AFTER_TAXES_TOTAL.subtract(newPayment);
    this.tripInvoice.saveItem(BillableItems.TRIP, BEFORE_TAXES);
    PaymentTrace paymentTrace = new PaymentTrace(LocalDate.now(), newPayment, ANY_PAYMENT_METHOD);
    this.tripInvoice.addPayment(paymentTrace, ANY_USER_ID);
    Money result = this.tripInvoice.calculateBalanceToPay(ANY_USER_ID);

    assertEquals(after, result);
  }

  @Test
  void whenCopying_thenNewInvoiceIsEqualToOldInvoice() {
    TripInvoice copied = this.tripInvoice.copy();

    assertEquals(this.tripInvoice, copied);
  }

  @Test
  void whenCopying_thenNewInvoiceIsNotTheSameObjectAsOld() {
    TripInvoice copied = this.tripInvoice.copy();

    assertNotSame(this.tripInvoice, copied);
  }

  @Test
  void whenCopying_thenNewInvoiceHasBalancePaidEqualToOld() {
    PaymentTrace paymentTrace = new PaymentTrace(LocalDate.now(), ANY_MONEY, ANY_PAYMENT_METHOD);
    this.tripInvoice.addPayment(paymentTrace, ANY_USER_ID);
    TripInvoice copied = this.tripInvoice.copy();

    assertEquals(this.tripInvoice.getBalancePaid(ANY_USER_ID), copied.getBalancePaid(ANY_USER_ID));
  }

  @Test
  void whenCopying_thenNewInvoiceHasItemsEqualToOld() {
    this.tripInvoice.saveItem(BillableItems.RESERVATION, ANY_MONEY);
    TripInvoice copied = this.tripInvoice.copy();

    assertEquals(this.tripInvoice.getItems(ANY_USER_ID), copied.getItems(ANY_USER_ID));
    assertNotSame(this.tripInvoice.getItems(ANY_USER_ID), copied.getItems(ANY_USER_ID));
  }

  @Test
  void whenCopying_thenNewInvoiceHasTotalAfterTaxesEqualToOld() {
    this.tripInvoice.saveItem(BillableItems.RESERVATION, BEFORE_TAXES);
    TripInvoice copied = this.tripInvoice.copy();

    assertEquals(
        this.tripInvoice.calculateTotalAfterTaxes(ANY_USER_ID),
        copied.calculateTotalAfterTaxes(ANY_USER_ID));
  }

  @Test
  void givenItemAddedAfterCopy_whenCopying_thenNewInvoiceDoNotHaveSameItemsAsOld() {
    this.tripInvoice.saveItem(BillableItems.RESERVATION, BEFORE_TAXES);
    TripInvoice copied = this.tripInvoice.copy();
    copied.saveItem(BillableItems.TRIP, BEFORE_TAXES);

    assertNotEquals(this.tripInvoice.getItems(ANY_USER_ID), copied.getItems(ANY_USER_ID));
  }

  @Test
  void whenGettingNumberOfPayers_thenGetsCorrectNumberOfPayers() {
    int expectedValue = 1;

    int result = this.tripInvoice.getNumberOfPayers();

    assertEquals(expectedValue, result);
  }

  @Test
  void givenSameObject_whenCallingEquals_thenObjectIsEqual() {
    assertEquals(this.tripInvoice, this.tripInvoice);
  }

  @Test
  void givenAnotherObjectWithSamePropertiesButDifferentId_whenCallingEquals_thenObjectIsNotEqual() {
    InvoiceID differentID = InvoiceID.generate();
    TripInvoice sameInvoiceDifferentId =
        new TripInvoice(
            differentID,
            ANY_USER_ID,
            ANY_TRIP_ID,
            this.taxStrategyMock,
            this.durationCancellationPenaltyMock,
            EMPTY_GUESTS_LIST);

    assertNotEquals(this.tripInvoice, sameInvoiceDifferentId);
  }

  @Test
  void givenAnotherObjectWithDifferentPropertiesButSameId_whenCallingEquals_thenObjectIsEqual() {
    TripInvoice differentInvoiceSameID = this.tripInvoice.copy();
    differentInvoiceSameID.saveItem(BillableItems.TRIP, BEFORE_TAXES);

    assertEquals(this.tripInvoice, differentInvoiceSameID);
  }

  @Test
  void whenRefund_thenDurationRefundCalculatesRefundPercentage() {
    this.tripInvoice.refund(ANY_TRIP_DATE);

    verify(this.durationCancellationPenaltyMock).calculateCancellationPenalty(ANY_TRIP_DATE_TIME);
  }

  @Test
  void whenRefund_thenRefundIsDone() {
    Money anyTripCost = new Money("100.0");
    when(this.durationCancellationPenaltyMock.calculateCancellationPenalty(ANY_TRIP_DATE_TIME))
        .thenReturn(ANY_PENALTY_PERCENTAGE);
    this.tripInvoice.saveItem(BillableItems.TRIP, anyTripCost);
    this.tripInvoice.addPayment(
        new PaymentTrace(LocalDate.now(), anyTripCost, CREDIT), ANY_USER_ID);

    this.tripInvoice.refund(ANY_TRIP_DATE);

    Map<BillableItems, Money> billablesAfterRefund = this.tripInvoice.getItems(ANY_USER_ID);
    assertTrue(billablesAfterRefund.containsKey(BillableItems.CREDIT));
  }

  @Test
  void givenGuests_whenRefund_thenTheirSubInvoicesAlsoGetRefund() {
    TravelerID guest = new TravelerID("firstGuest");
    when(this.durationCancellationPenaltyMock.calculateCancellationPenalty(ANY_TRIP_DATE_TIME))
        .thenReturn(ANY_PENALTY_PERCENTAGE);
    this.populateInvoiceWithItemsAndGuest(guest);

    this.tripInvoice.refund(ANY_TRIP_DATE);

    Map<BillableItems, Money> billablesAfterRefundForGuest = this.tripInvoice.getItems(guest);
    assertFalse(
        billablesAfterRefundForGuest.get(BillableItems.CREDIT).isGreaterOrEqualThan(new Money()));
    assertTrue(billablesAfterRefundForGuest.get(BillableItems.PENALTY).isStrictlyPositive());
  }

  @Test
  void givenPositiveBalanceToPay_whenCalculatingUserCredits_thenNoCreditIsGiven() {
    this.tripInvoice.saveItem(BillableItems.TRIP, BEFORE_TAXES);

    Money result = this.tripInvoice.calculateUserCredits();

    assertEquals(ZERO_MONEY, result);
  }

  @Test
  void givenNegativeBalanceToPay_whenCalculatingUserCredits_thenBalanceToPayIsGiven() {
    Money newPayment = new Money("860.45");
    PaymentTrace paymentTrace = new PaymentTrace(LocalDate.now(), newPayment, ANY_PAYMENT_METHOD);
    this.tripInvoice.addPayment(paymentTrace, ANY_USER_ID);

    Money result = this.tripInvoice.calculateUserCredits();

    assertEquals(newPayment, result);
  }

  private void populateInvoiceWithItemsAndGuest(TravelerID guest) {
    this.tripInvoice =
        new TripInvoice(
            ANY_INVOICE_ID,
            ANY_USER_ID,
            ANY_TRIP_ID,
            this.taxStrategyMock,
            this.durationCancellationPenaltyMock,
            List.of(guest));
    this.tripInvoice.saveItem(BillableItems.RESERVATION, new Money("50"));
    this.tripInvoice.saveItem(BillableItems.TRIP, new Money("50"));
    this.tripInvoice.saveItem(BillableItems.HOSTING, new Money("50"));
    this.tripInvoice.updateInvoicesWithItemMinimumToPay(BillableItems.RESERVATION);
  }
}
