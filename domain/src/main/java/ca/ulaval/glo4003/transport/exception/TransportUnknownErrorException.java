package ca.ulaval.glo4003.transport.exception;

import ca.ulaval.glo4003.exception.TrekULException;

public class TransportUnknownErrorException extends TrekULException {
  public TransportUnknownErrorException() {
    super("UNKNOWN ERROR", "Please try again later");
  }
}
