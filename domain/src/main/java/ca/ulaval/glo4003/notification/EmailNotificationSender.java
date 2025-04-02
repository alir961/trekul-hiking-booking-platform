package ca.ulaval.glo4003.notification;

import ca.ulaval.glo4003.traveler.Contactable;
import java.util.Collection;

public interface EmailNotificationSender {

  void sendEmail(Contactable contacts, Notification notification);

  void sendEmail(Collection<? extends Contactable> contacts, Notification notification);
}
