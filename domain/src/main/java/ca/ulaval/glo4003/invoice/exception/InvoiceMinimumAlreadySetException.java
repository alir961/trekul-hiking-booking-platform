package ca.ulaval.glo4003.invoice.exception;

import ca.ulaval.glo4003.exception.TrekULDeniedException;

public class InvoiceMinimumAlreadySetException extends TrekULDeniedException {
  public InvoiceMinimumAlreadySetException() {
    super("INVOICE_MINIMUM_ALREADY_SET", "Invoice minimum to pay can only be set once.");
  }
}
