package ca.ulaval.glo4003.invoice.exception;

import ca.ulaval.glo4003.exception.TrekULNotFoundException;

public class SubInvoiceNotFoundException extends TrekULNotFoundException {
  public SubInvoiceNotFoundException() {
    super("SUB_INVOICE_NOT_FOUND", "Sub invoice not found");
  }
}
