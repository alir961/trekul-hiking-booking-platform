package ca.ulaval.glo4003.hosting.reservation;

import ca.ulaval.glo4003.hosting.Hosting;
import ca.ulaval.glo4003.notification.EmailNotificationSender;
import ca.ulaval.glo4003.notification.Notification;
import ca.ulaval.glo4003.notification.types.HostingConfirmedNotificationFactory;
import ca.ulaval.glo4003.traveler.user.UserRepository;
import ca.ulaval.glo4003.trip.Trip;

public class HostingNotificationSender {
  private UserRepository userRepository;
  private HostingConfirmedNotificationFactory hostingConfirmedNotificationFactory;
  private EmailNotificationSender emailNotificationSender;

  public HostingNotificationSender(
      UserRepository userRepository,
      HostingConfirmedNotificationFactory hostingConfirmedNotificationFactory,
      EmailNotificationSender emailNotificationSender) {
    this.userRepository = userRepository;
    this.hostingConfirmedNotificationFactory = hostingConfirmedNotificationFactory;
    this.emailNotificationSender = emailNotificationSender;
  }

  void notifyUsers(Trip trip, Hosting hosting) {
    Notification notification = this.hostingConfirmedNotificationFactory.build(trip, hosting);
    this.emailNotificationSender.sendEmail(
        this.userRepository.getBunch(trip.retrieveReservedUsers()), notification);
    this.emailNotificationSender.sendEmail(trip.retrieveAllGuests(), notification);
  }
}
