package ca.ulaval.glo4003.hosting.exception;

import ca.ulaval.glo4003.exception.TrekULException;

public class HostingNoCapacityAvailableException extends TrekULException {

  public HostingNoCapacityAvailableException() {
    super("HOSTING_FULL", "Not enough capacity available in this hosting for this trip.");
  }
}
