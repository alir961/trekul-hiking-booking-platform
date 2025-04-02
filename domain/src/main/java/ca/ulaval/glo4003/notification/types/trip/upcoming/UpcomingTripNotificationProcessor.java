package ca.ulaval.glo4003.notification.types.trip.upcoming;

import ca.ulaval.glo4003.invoice.TripInvoice;
import ca.ulaval.glo4003.notification.EmailNotificationSender;
import ca.ulaval.glo4003.notification.Notification;
import ca.ulaval.glo4003.reservation.Reservation;
import ca.ulaval.glo4003.reservation.exception.ReservationNotFoundException;
import ca.ulaval.glo4003.traveler.guest.Guest;
import ca.ulaval.glo4003.traveler.user.User;
import ca.ulaval.glo4003.trip.Trip;
import java.util.Collection;

public class UpcomingTripNotificationProcessor {
  private final EmailNotificationSender emailNotificationSender;
  private final UpcomingTripNotificationDirector upcomingTripNotificationDirector;

  public UpcomingTripNotificationProcessor(
      EmailNotificationSender emailNotificationSender,
      UpcomingTripNotificationDirector upcomingTripNotificationDirector) {
    this.emailNotificationSender = emailNotificationSender;
    this.upcomingTripNotificationDirector = upcomingTripNotificationDirector;
  }

  public void notify(Collection<User> users, Trip trip) {
    users.forEach(user -> this.notify(user, trip));
  }

  private void notify(User user, Trip trip) {
    TripInvoice invoice = user.getInvoiceForTrip(trip.getId());
    this.notifyUser(user, trip, invoice);
    trip.retrieveAllGuestsOfTraveler(user.getId())
        .forEach(guest -> this.notifyGuest(guest, trip, invoice));
  }

  private void notifyUser(User user, Trip trip, TripInvoice invoice) {
    Reservation reservation =
        user.findFirstReservationOfTrip(trip.getId())
            .orElseThrow(ReservationNotFoundException::new);

    Notification notification =
        this.upcomingTripNotificationDirector.buildUserNotification(
            user, trip, invoice, reservation);
    this.emailNotificationSender.sendEmail(user, notification);
    user.notifiedForTrip(trip.getId());
  }

  private void notifyGuest(Guest guest, Trip trip, TripInvoice invoice) {
    Notification notification =
        this.upcomingTripNotificationDirector.buildGuestNotification(guest, trip, invoice);
    this.emailNotificationSender.sendEmail(guest, notification);
  }
}
