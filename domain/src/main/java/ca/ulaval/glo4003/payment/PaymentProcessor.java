package ca.ulaval.glo4003.payment;

import ca.ulaval.glo4003.invoice.TripInvoice;
import ca.ulaval.glo4003.traveler.TravelerID;

public class PaymentProcessor {
  private final PaymentApi paymentApi;
  private final PaymentQuantityValidator paymentQuantityValidator;

  public PaymentProcessor(
      PaymentQuantityValidator paymentQuantityValidator, PaymentApi paymentApi) {
    this.paymentApi = paymentApi;
    this.paymentQuantityValidator = paymentQuantityValidator;
  }

  public void processPayment(
      PaymentInformation paymentInformation, TripInvoice invoice, TravelerID payer) {
    this.paymentQuantityValidator.validatePaymentInRange(
        invoice.calculateMinimumToPay(payer),
        invoice.calculateBalanceToPay(payer),
        paymentInformation.payment());
    PaymentTrace paymentTrace = this.paymentApi.checkout(paymentInformation);
    invoice.addPayment(paymentTrace, payer);
  }
}
