package ca.ulaval.glo4003.transport.exception;

import ca.ulaval.glo4003.exception.TrekULException;

public class TransportInvalidRequestException extends TrekULException {
  public TransportInvalidRequestException() {
    super("INVALID_PARAMETERS", "A drop off bus stop must be specified to use the organized bus");
  }
}
