package ca.ulaval.glo4003.payment.assembler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.payment.PaymentInformation;
import ca.ulaval.glo4003.payment.request.PaymentRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PaymentRequestAssemblerTest {
  private static final String ANY_CARD_NUMBER = "123";
  private static final Integer ANY_CARD_CCV = 421;
  private static final String ANY_CARD_HOLDER_NAME = "Jo";
  private static final String ANY_EXPIRATION_DATE = "1024";
  private static final Double ANY_PAYMENT_STRING = 67.8;
  private static final PaymentRequest ANY_PAYMENT_REQUEST =
      new PaymentRequest(
          ANY_CARD_NUMBER,
          ANY_EXPIRATION_DATE,
          ANY_CARD_CCV,
          ANY_CARD_HOLDER_NAME,
          ANY_PAYMENT_STRING);
  private PaymentInformation expectedPaymentInformation;
  private PaymentRequestAssembler paymentRequestAssembler;

  @BeforeEach
  void setUp() {
    DateTimeFormatter cardDateFormat =
        new DateTimeFormatterBuilder()
            .appendPattern("MMyy")
            .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
            .toFormatter();
    LocalDate expirationDate = LocalDate.parse(ANY_EXPIRATION_DATE, cardDateFormat);

    this.paymentRequestAssembler = new PaymentRequestAssembler();
    this.expectedPaymentInformation =
        new PaymentInformation(
            ANY_CARD_NUMBER,
            ANY_CARD_HOLDER_NAME,
            ANY_CARD_CCV,
            expirationDate,
            new Money(ANY_PAYMENT_STRING));
  }

  @Test
  void whenAssembling_thenPaymentResponseAssembleCorrectly() {
    PaymentInformation paymentInformation =
        this.paymentRequestAssembler.assemble(ANY_PAYMENT_REQUEST);

    assertEquals(this.expectedPaymentInformation.ccv(), paymentInformation.ccv());
    assertEquals(this.expectedPaymentInformation.cardNumber(), paymentInformation.cardNumber());
    assertEquals(
        this.expectedPaymentInformation.cardHolderName(), paymentInformation.cardHolderName());
    assertEquals(
        this.expectedPaymentInformation.expirationDate(), paymentInformation.expirationDate());
    assertEquals(this.expectedPaymentInformation.payment(), paymentInformation.payment());
  }
}
