package ca.ulaval.glo4003.payment.dto;

import ca.ulaval.glo4003.invoice.dto.InvoiceDto;
import ca.ulaval.glo4003.money.Money;

public record PaymentConfirmationDto(
    Money paymentReceived, Money totalPaid, Money totalLeft, InvoiceDto invoice) {}
