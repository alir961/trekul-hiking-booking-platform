package ca.ulaval.glo4003.invoice;

import static ca.ulaval.glo4003.invoice.BillableItems.*;

import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.payment.PaymentTrace;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class SubInvoice {
  private final List<PaymentTrace> paymentList = new ArrayList<>();
  private final Map<BillableItems, Money> itemsCostWithoutTaxes =
      new EnumMap<>(BillableItems.class);
  private Money paidIncludingTaxes = new Money();
  private Money minimumToPay = new Money();

  public void saveItem(BillableItems item, Money cost) {
    this.itemsCostWithoutTaxes.put(item, cost);
  }

  public void scaleItemCost(BillableItems item, int scale) {
    Money itemNewCost = this.getItem(item).multiply(scale);
    this.saveItem(item, itemNewCost);
  }

  public Map<BillableItems, Money> getItemsCostWithoutTaxes() {
    return Map.copyOf(this.itemsCostWithoutTaxes);
  }

  public Money calculateTotalWithoutTaxes() {
    return new Money().add(this.itemsCostWithoutTaxes.values());
  }

  public Money getBalancePaid() {
    return this.paidIncludingTaxes;
  }

  public Money calculateTotalPaid() {
    return this.paymentList.stream().map(PaymentTrace::paid).reduce(new Money(), Money::add);
  }

  public void addPayment(PaymentTrace paymentTrace) {
    this.paidIncludingTaxes = this.paidIncludingTaxes.add(paymentTrace.paid());
    this.paymentList.add(paymentTrace);
  }

  public List<PaymentTrace> getPaymentList() {
    return List.copyOf(this.paymentList);
  }

  public Money getMinimumToPay() {
    return this.minimumToPay;
  }

  public void setItemAsMinimumToPay(BillableItems item) {
    this.minimumToPay = this.getItem(item);
  }

  public Money getItem(BillableItems item) {
    if (this.itemsCostWithoutTaxes.containsKey(item)) {
      return this.itemsCostWithoutTaxes.get(item);
    } else {
      return new Money();
    }
  }

  public void addCredit(Money credit) {
    Money currentCredit = this.getItem(CREDIT);
    this.saveItem(CREDIT, currentCredit.add(credit.asNegative()));
  }

  public void refund(double percentageOfPenalty) {
    BigDecimal penaltyPercentage = new BigDecimal(percentageOfPenalty);
    Money amountToRefund = this.calculateTotalWithoutTaxes();
    amountToRefund = amountToRefund.subtract(this.getItem(RESERVATION));
    amountToRefund = amountToRefund.subtract(this.getItem(CREDIT));
    Money penaltyAmount = amountToRefund.multiply(penaltyPercentage);
    this.addCredit(amountToRefund);
    this.addPenalty(penaltyAmount);
  }

  private void addPenalty(Money penalty) {
    Money penaltyTotal = this.getItem(PENALTY);
    penaltyTotal = penaltyTotal.add(penalty);
    this.saveItem(PENALTY, penaltyTotal);
  }

  public boolean isFirstPayment() {
    return this.paymentList.isEmpty();
  }

  public SubInvoice copy() {
    SubInvoice copied = new SubInvoice();
    copied.itemsCostWithoutTaxes.putAll(this.itemsCostWithoutTaxes);
    copied.paidIncludingTaxes = this.paidIncludingTaxes;
    copied.minimumToPay = this.minimumToPay;
    copied.paymentList.addAll(this.paymentList);

    return copied;
  }
}
