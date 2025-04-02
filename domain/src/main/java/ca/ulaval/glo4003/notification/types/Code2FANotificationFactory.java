package ca.ulaval.glo4003.notification.types;

import ca.ulaval.glo4003.notification.Notification;
import ca.ulaval.glo4003.token.AuthToken;
import ca.ulaval.glo4003.traveler.Contactable;

public class Code2FANotificationFactory {
  private static final String DEFAULT_NOTIFICATION_TITLE = "Code 2FA pour TrekUL!";

  public Notification build(Contactable contactable, AuthToken token) {
    String body = this.buildMessage(contactable, token);
    return new Notification(DEFAULT_NOTIFICATION_TITLE, body);
  }

  private String buildMessage(Contactable contactable, AuthToken token) {
    return String.format(
        """
            Bonjour %s! Voici votre code 2FA:
            \t%s

            Ce code expire dans %s minutes.
        """,
        contactable.getName(), token.getValue(), token.getExpirationInMinutes());
  }
}
