package ca.ulaval.glo4003.exception;

public class TrekULAuthenticationException extends TrekULException {
  protected TrekULAuthenticationException(String error, String message) {
    super(error, message);
  }
}
