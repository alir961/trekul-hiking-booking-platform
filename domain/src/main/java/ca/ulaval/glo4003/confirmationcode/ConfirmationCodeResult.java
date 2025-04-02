package ca.ulaval.glo4003.confirmationcode;

public enum ConfirmationCodeResult {
  VALID("The confirmation code is valid"),
  INVALID("The confirmation code is invalid"),
  NOT_PAID("The trip has not been paid by the user");

  private final String displayName;

  ConfirmationCodeResult(String displayName) {
    this.displayName = displayName;
  }

  @Override
  public String toString() {
    return this.displayName;
  }
}
