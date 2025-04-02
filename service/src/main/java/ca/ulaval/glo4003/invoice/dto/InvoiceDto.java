package ca.ulaval.glo4003.invoice.dto;

import ca.ulaval.glo4003.invoice.InvoiceID;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.payment.PaymentTrace;
import java.util.List;
import java.util.Map;

public record InvoiceDto(
    InvoiceID invoiceID,
    Map<String, Money> items,
    Map<String, Money> taxes,
    Money total,
    Money balanceToPay,
    List<PaymentTrace> payments) {}
