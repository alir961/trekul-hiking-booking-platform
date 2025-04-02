package ca.ulaval.glo4003.transport.exception;

import ca.ulaval.glo4003.exception.TrekULDeniedException;

public class TransportWithoutEnoughPlacesException extends TrekULDeniedException {
  public TransportWithoutEnoughPlacesException() {
    super(
        "NOT ENOUGH PLACES",
        "The transport you chose does not have enough available places for all the group, please look for other options");
  }
}
