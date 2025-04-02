package ca.ulaval.glo4003.payment.assembler;

import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.payment.PaymentInformation;
import ca.ulaval.glo4003.payment.request.PaymentRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public class PaymentRequestAssembler {
  private static final DateTimeFormatter CARD_DATE_FORMAT =
      new DateTimeFormatterBuilder()
          .appendPattern("MMyy")
          .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
          .toFormatter();

  public PaymentInformation assemble(PaymentRequest paymentRequest) {
    LocalDate expirationDate = LocalDate.parse(paymentRequest.expirationDate(), CARD_DATE_FORMAT);
    Money payment = new Money(paymentRequest.payment());
    return new PaymentInformation(
        paymentRequest.cardNumber(),
        paymentRequest.cardHolderName(),
        paymentRequest.ccv(),
        expirationDate,
        payment);
  }
}
