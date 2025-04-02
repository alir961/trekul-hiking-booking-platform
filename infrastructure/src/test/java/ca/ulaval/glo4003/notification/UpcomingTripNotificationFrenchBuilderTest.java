package ca.ulaval.glo4003.notification;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4003.distance.Distance;
import ca.ulaval.glo4003.hiking.Hiking;
import ca.ulaval.glo4003.hiking.HikingDifficulty;
import ca.ulaval.glo4003.hiking.HikingRegion;
import ca.ulaval.glo4003.hosting.reservation.HostingReservation;
import ca.ulaval.glo4003.invoice.BillableItems;
import ca.ulaval.glo4003.invoice.InvoiceID;
import ca.ulaval.glo4003.invoice.TripInvoice;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.notification.language.FrenchDescriptions;
import ca.ulaval.glo4003.reservation.Reservation;
import ca.ulaval.glo4003.transport.TransportType;
import ca.ulaval.glo4003.traveler.PersonName;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.trip.Trip;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UpcomingTripNotificationFrenchBuilderTest {
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
  private static final String MESSAGE_PAYMENT_COMPLETE = "Votre facture a été payée en entier";

  private static final String ANY_TRAVELER_FIRSTNAME = "Joey";
  private static final String ANY_TRAVELER_LASTNAME = "random last name";
  private static final PersonName ANY_TRAVELER_FULLNAME =
      new PersonName(ANY_TRAVELER_FIRSTNAME, ANY_TRAVELER_LASTNAME);
  private static final LocalDate ANY_TRIP_DATE = LocalDate.now();
  private static final TravelerID ANY_TRAVELER_ID = new TravelerID("anyUserId");
  private static final FrenchDescriptions FRENCH_DESCRIPTIONS = new FrenchDescriptions();
  private static final InvoiceID ANY_INVOICE_ID = InvoiceID.generate();

  private UpcomingTripNotificationFrenchBuilder builder;
  private Trip tripMock;
  private TripInvoice tripInvoiceMock;
  private Reservation reservationMock;

  @BeforeEach
  void setUp() {
    this.builder = new UpcomingTripNotificationFrenchBuilder(FRENCH_DESCRIPTIONS);
    this.tripMock = mock(Trip.class);
    this.tripInvoiceMock = mock(TripInvoice.class);
    this.reservationMock = mock(Reservation.class);
    when(this.tripInvoiceMock.getID()).thenReturn(ANY_INVOICE_ID);
  }

  @Test
  void whenWithHeader_thenIncludeHeader() {
    Hiking hikingMock = mock(Hiking.class);
    HikingDifficulty easyDifficulty = HikingDifficulty.EASY;
    HikingRegion saglacRegion = HikingRegion.SAGLAC;
    Distance twoKm = new Distance("2");
    when(this.tripMock.getDate()).thenReturn(ANY_TRIP_DATE);
    when(this.tripMock.getHiking()).thenReturn(hikingMock);
    when(hikingMock.getDifficulty()).thenReturn(easyDifficulty);
    when(hikingMock.getRegion()).thenReturn(saglacRegion);
    when(hikingMock.getDistance()).thenReturn(twoKm);

    this.builder = this.builder.withHeader(ANY_TRAVELER_FULLNAME, this.tripMock);

    Notification notification = this.builder.build();
    this.assertNotificationBodyIncludesHeader(
        notification.body(),
        ANY_TRIP_DATE.toString(),
        FRENCH_DESCRIPTIONS.getHikingDifficultyDescription(HikingDifficulty.EASY),
        FRENCH_DESCRIPTIONS.getHikingRegionDescription(HikingRegion.SAGLAC),
        twoKm.toString());
  }

  private void assertNotificationBodyIncludesHeader(
      String notificationBody,
      String hikingDate,
      String hikingDifficulty,
      String hikingRegion,
      String hikingDistance) {
    String expectedHeader =
        String.format(
            DEFAULT_HEADER_FORMAT,
            ANY_TRAVELER_FULLNAME,
            hikingDate,
            hikingDifficulty,
            hikingRegion,
            hikingDistance);
    assertTrue(notificationBody.contains(expectedHeader));
  }

  @Test
  void whenWithHostingReservation_thenIncludeHostingReservation() {
    String anyHostingName = "Manoir St-Luc";
    HostingReservation hostingReservationMock = mock(HostingReservation.class);
    when(hostingReservationMock.getHostingName()).thenReturn(anyHostingName);

    this.builder = this.builder.withHostingInformation(hostingReservationMock);

    Notification notification = this.builder.build();
    this.assertNotificationBodyIncludesHostingInformation(notification.body(), anyHostingName);
  }

  private void assertNotificationBodyIncludesHostingInformation(
      String notificationBody, String hostingName) {
    String expectedBody = String.format(HOSTING_INFORMATION_FORMAT, hostingName);

    assertTrue(notificationBody.contains(expectedBody));
  }

  @Test
  void whenWithInvoiceInformation_thenIncludeInvoiceInformation() {
    String anyReservationCost = "50";
    String anyHostingCost = "41.98";
    Map<BillableItems, Money> billablesWithReservationAndHostingCosts = new HashMap<>();
    billablesWithReservationAndHostingCosts.put(
        BillableItems.RESERVATION, new Money(anyReservationCost));
    billablesWithReservationAndHostingCosts.put(BillableItems.HOSTING, new Money(anyHostingCost));

    this.builder = this.builder.withInvoiceInformation(billablesWithReservationAndHostingCosts);

    Notification notification = this.builder.build();
    this.assertNotificationBodyIncludesInvoiceInformation(
        notification.body(), billablesWithReservationAndHostingCosts);
  }

  private void assertNotificationBodyIncludesInvoiceInformation(
      String notificationBody, Map<BillableItems, Money> billables) {
    StringBuilder content = new StringBuilder();

    billables.forEach(
        (item, cost) ->
            content.append(
                String.format(
                    MONEY_FORMAT, FRENCH_DESCRIPTIONS.getBillableItemDescription(item), cost)));

    assertTrue(notificationBody.contains(content.toString()));
  }

  @Test
  void whenWithTaxes_thenIncludeTaxes() {
    String anyFirstTax = "50";
    String anySecondTax = "41.98";
    Map<String, Money> taxesWithTwoItems = new HashMap<>();
    taxesWithTwoItems.put("one", new Money(anyFirstTax));
    taxesWithTwoItems.put("two", new Money(anySecondTax));

    this.builder = this.builder.withTaxes(taxesWithTwoItems);

    Notification notification = this.builder.build();
    this.assertNotificationBodyIncludesTaxes(notification.body(), taxesWithTwoItems);
  }

  private void assertNotificationBodyIncludesTaxes(
      String notificationBody, Map<String, Money> taxes) {
    StringBuilder content = new StringBuilder();
    taxes.forEach(
        (taxName, taxAmount) -> content.append(String.format(MONEY_FORMAT, taxName, taxAmount)));

    assertTrue(notificationBody.contains(content));
  }

  @Test
  void whenWithCheckoutLink_thenIncludeCheckoutLink() {
    this.builder = this.builder.withCheckoutLink(this.tripInvoiceMock);

    Notification notification = this.builder.build();
    this.assertNotificationBodyIncludesCheckoutLink(notification.body(), ANY_INVOICE_ID.toString());
  }

  private void assertNotificationBodyIncludesCheckoutLink(
      String notificationBody, String invoiceId) {
    String expectedBody = String.format(CHECKOUT_LINK_FORMAT, invoiceId);

    assertTrue(notificationBody.contains(expectedBody));
  }

  @Test
  void whenWithConfirmationCodeLink_thenIncludeConfirmationCodeLink() {

    this.builder = this.builder.withConfirmationCodeLink(this.tripInvoiceMock);

    Notification notification = this.builder.build();
    this.assertNotificationBodyIncludesConfirmationCodeLink(
        notification.body(), ANY_INVOICE_ID.toString());
  }

  private void assertNotificationBodyIncludesConfirmationCodeLink(
      String notificationBody, String invoiceId) {
    String expectedBody = String.format(CONFIRMATION_CODE_LINK_FORMAT, invoiceId);

    assertTrue(notificationBody.contains(expectedBody));
  }

  @Test
  void whenWithTwoFactorAuthLink_thenIncludeTwoFactorAuthLink() {
    when(this.tripInvoiceMock.getID()).thenReturn(ANY_INVOICE_ID);
    this.builder = this.builder.withTwoFactorAuthLink(ANY_TRAVELER_ID, this.tripInvoiceMock);

    Notification notification = this.builder.build();
    this.assertNotificationBodyIncludesTwoFactorAuthLink(notification.body(), ANY_INVOICE_ID);
  }

  private void assertNotificationBodyIncludesTwoFactorAuthLink(
      String notificationBody, InvoiceID anyInvoiceId) {
    String expectedBody = String.format(TWO_FACTOR_AUTH_LINK_FORMAT, anyInvoiceId, ANY_TRAVELER_ID);

    assertTrue(notificationBody.contains(expectedBody));
  }

  @Test
  void whenWithTransportType_thenIncludeTransportType() {
    TransportType anyTransportType = TransportType.CARPOOL;
    when(this.reservationMock.getTransportType()).thenReturn(anyTransportType);
    this.builder = this.builder.withTransportType(this.reservationMock);

    Notification notification = this.builder.build();

    this.assertNotificationBodyIncludesTransportType(
        notification.body(), FRENCH_DESCRIPTIONS.getTransportTypeDescription(anyTransportType));
  }

  private void assertNotificationBodyIncludesTransportType(
      String notificationBody, String transportType) {
    String expectedBody = String.format(TRANSPORT_INFORMATION_FORMAT, transportType);

    assertTrue(notificationBody.contains(expectedBody));
  }

  @Test
  void whenWithPaymentComplete_thenIncludePaymentComplete() {
    this.builder = this.builder.withPaymentComplete();

    Notification notification = this.builder.build();
    this.assertNotificationBodyIncludesPaymentComplete(notification.body());
  }

  private void assertNotificationBodyIncludesPaymentComplete(String notificationBody) {
    assertTrue(notificationBody.contains(MESSAGE_PAYMENT_COMPLETE));
  }

  @Test
  void whenBuild_thenReturnStringInCorrectOrder() {
    this.builder =
        this.builder.withPaymentComplete().withConfirmationCodeLink(this.tripInvoiceMock);

    Notification notification = this.builder.build();
    String body = notification.body();
    String confirmationCodeLink = String.format(CONFIRMATION_CODE_LINK_FORMAT, ANY_INVOICE_ID);
    this.assertNotificationBodyIncludesPaymentComplete(body);
    this.assertNotificationBodyIncludesConfirmationCodeLink(body, ANY_INVOICE_ID.toString());
    this.assertSubstringsInCorrectOrder(body, confirmationCodeLink);
  }

  private void assertSubstringsInCorrectOrder(String body, String confirmationCodeLink) {
    assertTrue(body.indexOf(MESSAGE_PAYMENT_COMPLETE) < body.indexOf(confirmationCodeLink));
  }

  @Test
  void whenBuild_thenResetBuffer() {
    this.builder = this.builder.withPaymentComplete();
    this.builder.build();
    TransportType anyTransportType = TransportType.CARPOOL;
    when(this.reservationMock.getTransportType()).thenReturn(anyTransportType);
    this.builder = this.builder.withTwoFactorAuthLink(ANY_TRAVELER_ID, this.tripInvoiceMock);

    Notification notification = this.builder.build();

    assertFalse(notification.body().contains(MESSAGE_PAYMENT_COMPLETE));
  }
}
