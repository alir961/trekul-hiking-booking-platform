package ca.ulaval.glo4003.notification.types.trip.upcoming;

import ca.ulaval.glo4003.hosting.reservation.HostingReservation;
import ca.ulaval.glo4003.invoice.BillableItems;
import ca.ulaval.glo4003.invoice.TripInvoice;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.notification.Notification;
import ca.ulaval.glo4003.reservation.Reservation;
import ca.ulaval.glo4003.traveler.PersonName;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.trip.Trip;
import java.util.Map;

public interface UpcomingTripNotificationBuilder {
  UpcomingTripNotificationBuilder withHeader(PersonName travelerName, Trip trip);

  UpcomingTripNotificationBuilder withHostingInformation(HostingReservation hostingReservation);

  UpcomingTripNotificationBuilder withInvoiceInformation(Map<BillableItems, Money> items);

  UpcomingTripNotificationBuilder withTaxes(Map<String, Money> taxes);

  UpcomingTripNotificationBuilder withCheckoutLink(TripInvoice invoice);

  UpcomingTripNotificationBuilder withConfirmationCodeLink(TripInvoice invoice);

  UpcomingTripNotificationBuilder withTwoFactorAuthLink(TravelerID travelerID, TripInvoice invoice);

  UpcomingTripNotificationBuilder withTransportType(Reservation reservation);

  UpcomingTripNotificationBuilder withPaymentComplete();

  Notification build();
}
