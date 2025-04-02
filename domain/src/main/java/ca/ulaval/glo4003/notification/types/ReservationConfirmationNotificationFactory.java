package ca.ulaval.glo4003.notification.types;

import ca.ulaval.glo4003.notification.Notification;
import ca.ulaval.glo4003.reservation.Reservation;

public class ReservationConfirmationNotificationFactory {

  public Notification buildConfirmation(Reservation reservation) {
    String subject = "Confirmation pour le voyage " + reservation.getTripID();
    String body = "Votre voyage a été confirmé!";
    return new Notification(subject, body);
  }
}
