package ca.ulaval.glo4003.exception;

public class TrekULException extends RuntimeException {

  private final String error;

  protected TrekULException(String error, String message) {
    super(message);
    this.error = error;
  }

  protected TrekULException(String error, Exception source) {
    super(source);
    this.error = error;
  }

  public String getError() {
    return this.error;
  }
}
