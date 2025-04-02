package ca.ulaval.glo4003.exception;

public class TrekULNotFoundException extends TrekULException {
  protected TrekULNotFoundException(String error, String message) {
    super(error, message);
  }
}
