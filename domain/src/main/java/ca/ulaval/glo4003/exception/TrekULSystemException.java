package ca.ulaval.glo4003.exception;

public class TrekULSystemException extends TrekULException {
  protected TrekULSystemException(String error, String message) {
    super(error, message);
  }

  protected TrekULSystemException(String error, Exception source) {
    super(error, source);
  }
}
