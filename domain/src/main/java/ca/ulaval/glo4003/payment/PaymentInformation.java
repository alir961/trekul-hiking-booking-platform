package ca.ulaval.glo4003.payment;

import ca.ulaval.glo4003.money.Money;
import java.time.LocalDate;

public record PaymentInformation(
    String cardNumber,
    String cardHolderName,
    Integer ccv,
    LocalDate expirationDate,
    Money payment) {}
