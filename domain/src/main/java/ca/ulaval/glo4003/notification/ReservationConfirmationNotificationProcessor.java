package ca.ulaval.glo4003.notification;

import ca.ulaval.glo4003.notification.types.ReservationConfirmationNotificationFactory;
import ca.ulaval.glo4003.reservation.Reservation;
import ca.ulaval.glo4003.traveler.user.User;

public class ReservationConfirmationNotificationProcessor {
  private final ReservationConfirmationNotificationFactory notificationFactory;
  private final EmailNotificationSender emailNotificationSender;

  public ReservationConfirmationNotificationProcessor(
      ReservationConfirmationNotificationFactory notificationFactory,
      EmailNotificationSender emailNotificationSender) {
    this.notificationFactory = notificationFactory;
    this.emailNotificationSender = emailNotificationSender;
  }

  public void notify(Reservation reservation, User user) {
    Notification notification = this.notificationFactory.buildConfirmation(reservation);
    this.emailNotificationSender.sendEmail(user, notification);
    this.emailNotificationSender.sendEmail(reservation.listGuests(), notification);
  }
}
