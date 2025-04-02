package ca.ulaval.glo4003.invoice.response;

import java.util.List;
import java.util.Map;

public record InvoiceResponse(
    String invoiceID,
    Map<String, String> items,
    Map<String, String> taxes,
    String total,
    String balanceToPay,
    List<PaymentTraceResponse> payments) {}
