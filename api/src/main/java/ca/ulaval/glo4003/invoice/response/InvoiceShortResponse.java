package ca.ulaval.glo4003.invoice.response;

public record InvoiceShortResponse(String invoiceID, String total, String balanceToPay) {}
