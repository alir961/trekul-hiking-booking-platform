package ca.ulaval.glo4003.payment;

import ca.ulaval.glo4003.money.Money;
import java.time.LocalDate;

public record PaymentTrace(LocalDate date, Money paid, PaymentMethod method) {}
