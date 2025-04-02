package ca.ulaval.glo4003.transport.exception;

import ca.ulaval.glo4003.exception.TrekULNotFoundException;

public class TransportStopDoesNotExistException extends TrekULNotFoundException {
  public TransportStopDoesNotExistException() {
    super("STOP_DOES_NOT_EXIST", "The chosen stop does not exist, please try again");
  }
}
