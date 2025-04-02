package ca.ulaval.glo4003.payment;

public interface PaymentApi {

  PaymentTrace checkout(PaymentInformation paymentInformation);
}
