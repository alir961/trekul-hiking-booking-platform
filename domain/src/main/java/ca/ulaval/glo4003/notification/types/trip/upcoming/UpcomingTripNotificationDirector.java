package ca.ulaval.glo4003.notification.types.trip.upcoming;

import ca.ulaval.glo4003.invoice.BillableItems;
import ca.ulaval.glo4003.invoice.TripInvoice;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.notification.Notification;
import ca.ulaval.glo4003.reservation.Reservation;
import ca.ulaval.glo4003.traveler.PersonName;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.guest.Guest;
import ca.ulaval.glo4003.traveler.user.User;
import ca.ulaval.glo4003.trip.Trip;
import java.util.Map;

public class UpcomingTripNotificationDirector {

  private final UpcomingTripNotificationBuilder builder;

  public UpcomingTripNotificationDirector(UpcomingTripNotificationBuilder builder) {
    this.builder = builder;
  }

  public Notification buildUserNotification(
      User user, Trip trip, TripInvoice tripInvoice, Reservation reservation) {
    this.buildCommonNotificationParameters(user.getName(), trip, tripInvoice, user.getId());

    this.builder.withTransportType(reservation).withConfirmationCodeLink(tripInvoice);

    return this.builder.build();
  }

  public Notification buildGuestNotification(Guest guest, Trip trip, TripInvoice tripInvoice) {
    this.buildCommonNotificationParameters(guest.getName(), trip, tripInvoice, guest.getId());

    this.builder.withTwoFactorAuthLink(guest.getId(), tripInvoice);

    return this.builder.build();
  }

  private void buildCommonNotificationParameters(
      PersonName travelerName, Trip trip, TripInvoice tripInvoice, TravelerID travelerID) {
    Map<BillableItems, Money> billables = tripInvoice.getItems(travelerID);
    Map<String, Money> taxes = tripInvoice.calculateTaxes(travelerID);
    this.builder.withHeader(travelerName, trip).withInvoiceInformation(billables).withTaxes(taxes);

    if (tripInvoice.isFullPaid(travelerID)) {
      this.builder.withPaymentComplete();
    } else {
      this.builder.withCheckoutLink(tripInvoice);
    }

    if (trip.getHostingReservation() != null) {
      this.builder.withHostingInformation(trip.getHostingReservation());
    }
  }
}
