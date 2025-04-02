package ca.ulaval.glo4003.invoice;

import ca.ulaval.glo4003.invoice.exception.InvoiceMinimumAlreadySetException;
import ca.ulaval.glo4003.invoice.exception.SubInvoiceNotFoundException;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.payment.PaymentTrace;
import ca.ulaval.glo4003.reservation.DurationCancellationPenalty;
import ca.ulaval.glo4003.tax.TaxStrategy;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.trip.TripID;
import java.time.LocalDate;
import java.util.*;

public class TripInvoice {
  private final InvoiceID invoiceID;
  private final TravelerID userID;
  private final TripID tripID;
  private final TaxStrategy taxStrategy;
  private final Map<TravelerID, SubInvoice> subInvoices = new HashMap<>();
  private final DurationCancellationPenalty durationCancellationPenalty;
  private boolean isMinimumSet = false;

  public TripInvoice(
      InvoiceID invoiceID,
      TravelerID userID,
      TripID tripID,
      TaxStrategy taxStrategy,
      DurationCancellationPenalty durationCancellationPenalty,
      Collection<TravelerID> guests) {
    this.invoiceID = invoiceID;
    this.userID = userID;
    this.tripID = tripID;
    this.taxStrategy = taxStrategy;
    this.durationCancellationPenalty = durationCancellationPenalty;
    this.subInvoices.put(userID, new SubInvoice());
    guests.forEach(guest -> this.subInvoices.put(guest, new SubInvoice()));
  }

  public void updateInvoicesWithItemMinimumToPay(BillableItems item) {
    if (this.isMinimumSet) {
      throw new InvoiceMinimumAlreadySetException();
    }
    this.isMinimumSet = true;
    SubInvoice userInvoice = this.fetchInvoiceOf(this.userID);
    userInvoice.scaleItemCost(item, this.subInvoices.size());
    this.subInvoices.values().forEach(invoice -> invoice.setItemAsMinimumToPay(item));
  }

  public void saveItem(BillableItems item, Money cost) {
    this.subInvoices.values().forEach(invoice -> invoice.saveItem(item, cost));
  }

  public Map<BillableItems, Money> getItems(TravelerID travelerID) {
    return this.fetchInvoiceOf(travelerID).getItemsCostWithoutTaxes();
  }

  public InvoiceID getInvoiceID() {
    return this.invoiceID;
  }

  public Money calculateTotalAfterTaxes(TravelerID travelerID) {
    Money total = this.fetchInvoiceOf(travelerID).calculateTotalWithoutTaxes();
    return this.taxStrategy.calculateTax(total).getTotal();
  }

  public Map<String, Money> calculateTaxes(TravelerID travelerID) {
    Money total = this.fetchInvoiceOf(travelerID).calculateTotalWithoutTaxes();
    return this.taxStrategy.calculateTax(total).getTaxes();
  }

  public Money getBalancePaid(TravelerID travelerID) {
    return this.fetchInvoiceOf(travelerID).getBalancePaid();
  }

  public boolean isFullPaid(TravelerID travelerID) {
    return !this.calculateBalanceToPay(travelerID).isStrictlyPositive();
  }

  public boolean isFullyPaidByGroup() {
    return this.subInvoices.keySet().stream().allMatch(this::isFullPaid);
  }

  public Money calculateBalanceToPay(TravelerID travelerID) {
    SubInvoice subInvoice = this.fetchInvoiceOf(travelerID);
    Money total = subInvoice.calculateTotalWithoutTaxes();
    total = this.taxStrategy.calculateTax(total).getTotal();
    return total.subtract(subInvoice.getBalancePaid());
  }

  public void addPayment(PaymentTrace paymentTrace, TravelerID travelerID) {
    SubInvoice invoice = this.fetchInvoiceOf(travelerID);
    if (!this.userID.equals(travelerID) && invoice.isFirstPayment()) {
      SubInvoice invoiceOwner = this.fetchInvoiceOf(this.userID);
      invoiceOwner.addCredit(invoice.getMinimumToPay());
    }
    invoice.addPayment(paymentTrace);
  }

  public List<PaymentTrace> getPaymentList(TravelerID travelerID) {
    return this.fetchInvoiceOf(travelerID).getPaymentList();
  }

  public int getNumberOfPayers() {
    return this.subInvoices.size();
  }

  public TripID getTripID() {
    return this.tripID;
  }

  public TravelerID getUserID() {
    return this.userID;
  }

  public InvoiceID getID() {
    return this.invoiceID;
  }

  public TripInvoice copy() {
    TripInvoice copied =
        new TripInvoice(
            this.invoiceID,
            this.userID,
            this.tripID,
            this.taxStrategy,
            this.durationCancellationPenalty,
            this.subInvoices.keySet());
    this.subInvoices.forEach((key, value) -> copied.subInvoices.put(key, value.copy()));
    copied.isMinimumSet = this.isMinimumSet;
    return copied;
  }

  @Override
  public boolean equals(Object object) {
    if (object == null) {
      return false;
    }
    if (object.getClass() != this.getClass()) {
      return false;
    }
    final TripInvoice other = (TripInvoice) object;
    return other.invoiceID.equals(this.invoiceID);
  }

  @Override
  public int hashCode() {
    return this.invoiceID.hashCode();
  }

  public Money calculateMinimumToPay(TravelerID travelerID) {
    SubInvoice subInvoice = this.fetchInvoiceOf(travelerID);
    Money minimumToPay = this.taxStrategy.calculateTax(subInvoice.getMinimumToPay()).getTotal();
    minimumToPay = minimumToPay.subtract(subInvoice.getBalancePaid());
    if (minimumToPay.isStrictlyPositive()) {
      return minimumToPay;
    }
    return new Money();
  }

  private SubInvoice fetchInvoiceOf(TravelerID travelerID) {
    if (this.subInvoices.containsKey(travelerID)) {
      return this.subInvoices.get(travelerID);
    } else {
      throw new SubInvoiceNotFoundException();
    }
  }

  public void refund(LocalDate tripDate) {
    Double percentageOfCancellationPenalty =
        this.durationCancellationPenalty.calculateCancellationPenalty(tripDate.atStartOfDay());
    this.subInvoices
        .values()
        .forEach(subInvoice -> subInvoice.refund(percentageOfCancellationPenalty));
  }

  public Money calculateUserCredits() {
    Money credits = this.calculateBalanceToPay(this.userID);
    if (credits.isStrictlyPositive()) {
      credits = new Money();
    }
    return credits.asPositive();
  }
}
