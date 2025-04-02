package ca.ulaval.glo4003.notification;

import ca.ulaval.glo4003.distance.Distance;
import ca.ulaval.glo4003.hiking.Hiking;
import ca.ulaval.glo4003.hosting.reservation.HostingReservation;
import ca.ulaval.glo4003.invoice.BillableItems;
import ca.ulaval.glo4003.invoice.TripInvoice;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.notification.language.FrenchDescriptions;
import ca.ulaval.glo4003.notification.types.trip.upcoming.UpcomingTripNotificationBuilder;
import ca.ulaval.glo4003.reservation.Reservation;
import ca.ulaval.glo4003.traveler.PersonName;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.trip.Trip;
import java.time.LocalDate;
import java.util.Map;

public class UpcomingTripNotificationFrenchBuilder implements UpcomingTripNotificationBuilder {
  private static final String DEFAULT_HEADER_FORMAT =
      "Bonjour %s! Voici quelques détails à propos de votre voyage à venir le %s.\n"
          + "\tDifficulté de la randonnée: %s\n"
          + "\tLieu de la randonnée: %s\n"
          + "\tDistance de la randonnée: %s\n";
  private static final String DEFAULT_TITLE = "Rappel de votre voyage à venir!";
  private static final String CONFIRMATION_CODE_LINK_FORMAT =
      "Une fois votre facture payée en entier, "
          + "cliquez sur le lien suivant pour obtenir votre code de confirmation "
          + "si ce n'est pas déjà fait: http://localhost:8080/api/trekul/invoices/%s/code";
  private static final String CHECKOUT_LINK_FORMAT =
      "Pour tout paiement, visitez: http://localhost:8080/api/trekul/invoices/%s:pay";
  private static final String HOSTING_INFORMATION_FORMAT =
      "Voici les détails concernant votre séjour:\n\t Vous avez choisi de séjourner à : %s\n";
  private static final String MONEY_FORMAT = "\t%s: %s$\n";
  private static final String TRANSPORT_INFORMATION_FORMAT =
      "Vous avez choisi de voyager par: %s \n";
  private static final String TWO_FACTOR_AUTH_LINK_FORMAT =
      "Vous pouvez vous authentifier en tant que visiteur ici: http://localhost:8080/api/trekul/%s/guests/%s/2FA\n";

  private StringBuilder bodyStringBuilder;
  private FrenchDescriptions frenchDescriptions;

  public UpcomingTripNotificationFrenchBuilder(FrenchDescriptions frenchDescriptions) {
    this.bodyStringBuilder = new StringBuilder();
    this.frenchDescriptions = frenchDescriptions;
  }

  public UpcomingTripNotificationFrenchBuilder withHeader(PersonName travelerName, Trip trip) {
    LocalDate hikingDate = trip.getDate();
    Hiking hiking = trip.getHiking();
    String hikingDifficulty =
        this.frenchDescriptions.getHikingDifficultyDescription(hiking.getDifficulty());
    String hikingRegion = this.frenchDescriptions.getHikingRegionDescription(hiking.getRegion());
    Distance hikingDistance = hiking.getDistance();
    this.bodyStringBuilder.append(
        String.format(
            DEFAULT_HEADER_FORMAT,
            travelerName,
            hikingDate,
            hikingDifficulty,
            hikingRegion,
            hikingDistance));
    return this;
  }

  public UpcomingTripNotificationFrenchBuilder withHostingInformation(
      HostingReservation hostingReservation) {
    this.bodyStringBuilder.append("\n");
    this.bodyStringBuilder.append(
        String.format(HOSTING_INFORMATION_FORMAT, hostingReservation.getHostingName()));
    return this;
  }

  public UpcomingTripNotificationFrenchBuilder withInvoiceInformation(
      Map<BillableItems, Money> items) {
    this.bodyStringBuilder.append("\n");
    this.bodyStringBuilder.append("Détails de facturation:\n");
    items.forEach(
        (item, cost) ->
            this.bodyStringBuilder.append(
                String.format(
                    MONEY_FORMAT, this.frenchDescriptions.getBillableItemDescription(item), cost)));
    return this;
  }

  public UpcomingTripNotificationFrenchBuilder withTaxes(Map<String, Money> taxes) {
    this.bodyStringBuilder.append("\n");
    this.bodyStringBuilder.append("Détails des taxes:\n");
    taxes.forEach(
        (taxName, taxAmount) ->
            this.bodyStringBuilder.append(String.format(MONEY_FORMAT, taxName, taxAmount)));
    return this;
  }

  public UpcomingTripNotificationFrenchBuilder withCheckoutLink(TripInvoice invoice) {
    this.bodyStringBuilder.append("\n");
    this.bodyStringBuilder.append(String.format(CHECKOUT_LINK_FORMAT, invoice.getID()));
    return this;
  }

  public UpcomingTripNotificationFrenchBuilder withConfirmationCodeLink(TripInvoice invoice) {
    this.bodyStringBuilder.append("\n");
    this.bodyStringBuilder.append(String.format(CONFIRMATION_CODE_LINK_FORMAT, invoice.getID()));
    return this;
  }

  public UpcomingTripNotificationFrenchBuilder withTwoFactorAuthLink(
      TravelerID travelerID, TripInvoice invoice) {
    this.bodyStringBuilder.append("\n");
    this.bodyStringBuilder.append(
        String.format(TWO_FACTOR_AUTH_LINK_FORMAT, invoice.getID(), travelerID));
    return this;
  }

  public UpcomingTripNotificationFrenchBuilder withTransportType(Reservation reservation) {
    this.bodyStringBuilder.append("\n");
    this.bodyStringBuilder.append(
        String.format(
            TRANSPORT_INFORMATION_FORMAT,
            this.frenchDescriptions.getTransportTypeDescription(reservation.getTransportType())));
    return this;
  }

  public UpcomingTripNotificationFrenchBuilder withPaymentComplete() {
    this.bodyStringBuilder.append("\n");
    this.bodyStringBuilder.append("Votre facture a été payée en entier");
    return this;
  }

  public Notification build() {
    String body = this.bodyStringBuilder.toString();
    this.bodyStringBuilder = new StringBuilder();
    return new Notification(DEFAULT_TITLE, body);
  }
}
