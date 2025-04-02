package ca.ulaval.glo4003.hosting.exception;

import ca.ulaval.glo4003.exception.TrekULNotFoundException;

public class HostingNotFoundException extends TrekULNotFoundException {

  public HostingNotFoundException() {
    super("HOSTING_NOT_FOUND", "Hosting not found");
  }
}
