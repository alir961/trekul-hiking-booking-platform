package ca.ulaval.glo4003.transport.exception;

import ca.ulaval.glo4003.exception.TrekULException;

public class TransportInvalidTypeException extends TrekULException {
  public TransportInvalidTypeException() {
    super("TRANSPORT_TYPE_INVALID", "The transport type provided does not exist, please try again");
  }
}
