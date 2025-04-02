package ca.ulaval.glo4003.invoice.exception;

import ca.ulaval.glo4003.exception.TrekULSystemException;

public class CreditsException extends TrekULSystemException {
  public CreditsException() {
    super("REFUND_ERROR", "Invalid state: could not process refund");
  }
}
