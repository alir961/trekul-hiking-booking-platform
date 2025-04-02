package ca.ulaval.glo4003.exception;

public class TrekULDeniedException extends TrekULException {
  protected TrekULDeniedException(String error, String message) {
    super(error, message);
  }
}
