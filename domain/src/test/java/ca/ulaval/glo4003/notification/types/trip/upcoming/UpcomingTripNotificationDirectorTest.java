package ca.ulaval.glo4003.notification.types.trip.upcoming;

import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.hosting.reservation.HostingReservation;
import ca.ulaval.glo4003.invoice.BillableItems;
import ca.ulaval.glo4003.invoice.TripInvoice;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.reservation.Reservation;
import ca.ulaval.glo4003.traveler.PersonName;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.guest.Guest;
import ca.ulaval.glo4003.traveler.user.User;
import ca.ulaval.glo4003.trip.Trip;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UpcomingTripNotificationDirectorTest {
  private static final String ANY_USER_FIRSTNAME = "Joey";
  private static final String ANY_USER_LASTNAME = "Last name";
  private static final PersonName ANY_USER_FULLNAME =
      new PersonName(ANY_USER_FIRSTNAME, ANY_USER_LASTNAME);
  private static final TravelerID ANY_USER_ID = new TravelerID("anyUserId");
  private static final String ANY_GUEST_FIRSTNAME = "Bobbi";
  private static final String ANY_GUEST_LASTNAME = "Fatt";
  private static final PersonName ANY_GUEST_FULLNAME =
      new PersonName(ANY_GUEST_FIRSTNAME, ANY_GUEST_LASTNAME);
  private static final TravelerID ANY_GUEST_ID = new TravelerID("anyGuestId");
  private static final Map<BillableItems, Money> ANY_BILLABLES = new HashMap<>();
  private static final Map<String, Money> ANY_TAXES = new HashMap<>();
  private static final boolean ANY_FULLY_PAID = false;

  private UpcomingTripNotificationDirector director;

  private UpcomingTripNotificationBuilder builderMock;
  private User userMock;
  private Guest guestMock;
  private Trip tripMock;
  private TripInvoice tripInvoiceMock;
  private Reservation reservationMock;

  @BeforeEach
  void setUp() {
    this.userMock = mock(User.class);
    this.guestMock = mock(Guest.class);
    this.tripMock = mock(Trip.class);
    this.tripInvoiceMock = mock(TripInvoice.class);
    this.reservationMock = mock(Reservation.class);
    this.builderMock = mock(UpcomingTripNotificationBuilder.class);
    this.director = new UpcomingTripNotificationDirector(this.builderMock);

    this.setUpUserMocks();
    this.setUpGuestMocks();
    when(this.builderMock.withInvoiceInformation(ANY_BILLABLES)).thenReturn(this.builderMock);
    when(this.builderMock.withTaxes(ANY_TAXES)).thenReturn(this.builderMock);
    when(this.builderMock.withTransportType(this.reservationMock)).thenReturn(this.builderMock);
    when(this.builderMock.withConfirmationCodeLink(this.tripInvoiceMock))
        .thenReturn(this.builderMock);
  }

  private void setUpUserMocks() {
    when(this.builderMock.withHeader(ANY_USER_FULLNAME, this.tripMock))
        .thenReturn(this.builderMock);
    when(this.userMock.getName()).thenReturn(ANY_USER_FULLNAME);
    when(this.userMock.getId()).thenReturn(ANY_USER_ID);
    when(this.tripInvoiceMock.calculateTaxes(ANY_USER_ID)).thenReturn(ANY_TAXES);
    when(this.tripInvoiceMock.getItems(ANY_USER_ID)).thenReturn(ANY_BILLABLES);
    when(this.tripInvoiceMock.isFullPaid(ANY_USER_ID)).thenReturn(ANY_FULLY_PAID);
  }

  private void setUpGuestMocks() {
    when(this.builderMock.withHeader(ANY_GUEST_FULLNAME, this.tripMock))
        .thenReturn(this.builderMock);
    when(this.guestMock.getName()).thenReturn(ANY_GUEST_FULLNAME);
    when(this.guestMock.getId()).thenReturn(ANY_GUEST_ID);
    when(this.tripInvoiceMock.isFullPaid(ANY_GUEST_ID)).thenReturn(ANY_FULLY_PAID);
  }

  @Test
  void whenBuildUserNotification_thenBuildWithHeader() {
    this.director.buildUserNotification(
        this.userMock, this.tripMock, this.tripInvoiceMock, this.reservationMock);

    verify(this.builderMock).withHeader(ANY_USER_FULLNAME, this.tripMock);
  }

  @Test
  void whenBuildUserNotification_thenBuildWithInvoiceInformation() {
    this.director.buildUserNotification(
        this.userMock, this.tripMock, this.tripInvoiceMock, this.reservationMock);

    verify(this.builderMock).withInvoiceInformation(ANY_BILLABLES);
  }

  @Test
  void whenBuildUserNotification_thenBuildWithTaxesInformation() {
    this.director.buildUserNotification(
        this.userMock, this.tripMock, this.tripInvoiceMock, this.reservationMock);

    verify(this.builderMock).withTaxes(ANY_TAXES);
  }

  @Test
  void givenPaymentComplete_whenBuildUserNotification_thenBuildWithPaymentComplete() {
    boolean paymentComplete = true;
    when(this.tripInvoiceMock.isFullPaid(ANY_USER_ID)).thenReturn(paymentComplete);

    this.director.buildUserNotification(
        this.userMock, this.tripMock, this.tripInvoiceMock, this.reservationMock);

    verify(this.builderMock).withPaymentComplete();
  }

  @Test
  void givenPaymentIsNotComplete_whenBuildUserNotification_thenBuildWithCheckoutLink() {
    boolean paymentComplete = false;
    when(this.tripInvoiceMock.isFullPaid(ANY_USER_ID)).thenReturn(paymentComplete);

    this.director.buildUserNotification(
        this.userMock, this.tripMock, this.tripInvoiceMock, this.reservationMock);

    verify(this.builderMock, never()).withPaymentComplete();
  }

  @Test
  void givenUserHasAHostingReservation_whenBuildUserNotification_thenBuildWithHostingReservation() {
    HostingReservation hostingReservationMock = mock(HostingReservation.class);
    when(this.tripMock.getHostingReservation()).thenReturn(hostingReservationMock);

    this.director.buildUserNotification(
        this.userMock, this.tripMock, this.tripInvoiceMock, this.reservationMock);

    verify(this.builderMock).withHostingInformation(hostingReservationMock);
  }

  @Test
  void
      givenUserHasNoHostingReservation_whenBuildUserNotification_thenDoNotBuildWithHostingReservation() {
    HostingReservation nullableHostingReservation = null;
    when(this.tripMock.getHostingReservation()).thenReturn(nullableHostingReservation);

    this.director.buildUserNotification(
        this.userMock, this.tripMock, this.tripInvoiceMock, this.reservationMock);

    verify(this.builderMock, never()).withHostingInformation(nullableHostingReservation);
  }

  @Test
  void whenBuildUserNotification_thenBuildWithTransportType() {
    this.director.buildUserNotification(
        this.userMock, this.tripMock, this.tripInvoiceMock, this.reservationMock);

    verify(this.builderMock).withTransportType(this.reservationMock);
  }

  @Test
  void whenBuildUserNotification_thenBuildWithConfirmationCodeLink() {
    this.director.buildUserNotification(
        this.userMock, this.tripMock, this.tripInvoiceMock, this.reservationMock);

    verify(this.builderMock).withConfirmationCodeLink(this.tripInvoiceMock);
  }

  @Test
  void whenBuildGuestNotification_thenBuildWithHeader() {
    this.director.buildGuestNotification(this.guestMock, this.tripMock, this.tripInvoiceMock);

    verify(this.builderMock).withHeader(ANY_GUEST_FULLNAME, this.tripMock);
  }

  @Test
  void whenBuildGuestNotification_thenBuildWithInvoiceInformation() {
    this.director.buildGuestNotification(this.guestMock, this.tripMock, this.tripInvoiceMock);

    verify(this.builderMock).withInvoiceInformation(ANY_BILLABLES);
  }

  @Test
  void whenBuildGuestNotification_thenBuildWithTaxesInformation() {
    this.director.buildGuestNotification(this.guestMock, this.tripMock, this.tripInvoiceMock);

    verify(this.builderMock).withTaxes(ANY_TAXES);
  }

  @Test
  void givenPaymentComplete_whenBuildGuestNotification_thenBuildWithPaymentComplete() {
    boolean paymentComplete = true;
    when(this.tripInvoiceMock.isFullPaid(ANY_GUEST_ID)).thenReturn(paymentComplete);

    this.director.buildGuestNotification(this.guestMock, this.tripMock, this.tripInvoiceMock);

    verify(this.builderMock).withPaymentComplete();
  }

  @Test
  void givenPaymentIsNotComplete_whenBuildGuestNotification_thenBuildWithCheckoutLink() {
    boolean paymentComplete = false;
    when(this.tripInvoiceMock.isFullPaid(ANY_GUEST_ID)).thenReturn(paymentComplete);

    this.director.buildGuestNotification(this.guestMock, this.tripMock, this.tripInvoiceMock);

    verify(this.builderMock, never()).withPaymentComplete();
  }

  @Test
  void
      givenUserHasAHostingReservation_whenBuildGuestNotification_thenBuildWithHostingReservation() {
    HostingReservation hostingReservationMock = mock(HostingReservation.class);
    when(this.tripMock.getHostingReservation()).thenReturn(hostingReservationMock);

    this.director.buildGuestNotification(this.guestMock, this.tripMock, this.tripInvoiceMock);

    verify(this.builderMock).withHostingInformation(hostingReservationMock);
  }

  @Test
  void
      givenUserHasNoHostingReservation_whenBuildGuestNotification_thenDoNotBuildWithHostingReservation() {
    HostingReservation nullableHostingReservation = null;
    when(this.tripMock.getHostingReservation()).thenReturn(nullableHostingReservation);

    this.director.buildGuestNotification(this.guestMock, this.tripMock, this.tripInvoiceMock);

    verify(this.builderMock, never()).withHostingInformation(nullableHostingReservation);
  }

  @Test
  void whenBuildGuestNotification_thenBuildWithTwoFactorAuthLink() {
    this.director.buildGuestNotification(this.guestMock, this.tripMock, this.tripInvoiceMock);

    verify(this.builderMock).withTwoFactorAuthLink(ANY_GUEST_ID, this.tripInvoiceMock);
  }
}
