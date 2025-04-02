package ca.ulaval.glo4003.invoice.response;

import java.time.LocalDate;

public record PaymentTraceResponse(LocalDate date, String paid, String method) {}
