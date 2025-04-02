package ca.ulaval.glo4003.confirmationcode;

import java.util.UUID;

public class ConfirmationCodeFactory {
  public ConfirmationCode createConfirmationCode() {
    UUID uuid = UUID.randomUUID();
    return new ConfirmationCode(uuid.toString());
  }
}
