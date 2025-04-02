package ca.ulaval.glo4003.payment.response;

import ca.ulaval.glo4003.invoice.response.InvoiceResponse;

public record PaymentResponse(
    String paymentReceived, String totalPaid, String totalLeft, InvoiceResponse invoice) {}
