package ca.ulaval.glo4003.transport.exception;

import ca.ulaval.glo4003.exception.TrekULDeniedException;

public class TransportFullyBookedException extends TrekULDeniedException {
  public TransportFullyBookedException() {
    super(
        "FULLY BOOKED", "The transport you chose seems to be full, please look for other options");
  }
}
