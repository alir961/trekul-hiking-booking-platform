package ca.ulaval.glo4003.invoice.exception;

import ca.ulaval.glo4003.exception.TrekULNotFoundException;

public class InvoiceNotFoundException extends TrekULNotFoundException {
  public InvoiceNotFoundException() {
    super("INVOICE_NOT_FOUND", "Invoice not found");
  }
}
