package ca.ulaval.glo4003.confirmationcode.exception;

import ca.ulaval.glo4003.exception.TrekULNotFoundException;

public class ConfirmationCodeNotFoundException extends TrekULNotFoundException {

  public ConfirmationCodeNotFoundException() {
    super("CONFIRMATION_CODE_NOT_FOUND", "Confirmation code does not exist or is invalid.");
  }
}
