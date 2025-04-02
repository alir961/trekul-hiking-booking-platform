package ca.ulaval.glo4003.transport.exception;

import ca.ulaval.glo4003.exception.TrekULException;

public class TransportNotAvailableException extends TrekULException {
  public TransportNotAvailableException() {
    super("NOT AVAILABLE", "The transport you chose is not available at the moment");
  }
}
