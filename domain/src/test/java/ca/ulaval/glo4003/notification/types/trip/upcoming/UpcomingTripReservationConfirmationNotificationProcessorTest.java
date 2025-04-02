package ca.ulaval.glo4003.notification.types.trip.upcoming;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.invoice.TripInvoice;
import ca.ulaval.glo4003.invoice.exception.InvoiceNotFoundException;
import ca.ulaval.glo4003.notification.EmailNotificationSender;
import ca.ulaval.glo4003.notification.Notification;
import ca.ulaval.glo4003.reservation.Reservation;
import ca.ulaval.glo4003.reservation.exception.ReservationNotFoundException;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.guest.Guest;
import ca.ulaval.glo4003.traveler.user.User;
import ca.ulaval.glo4003.trip.Trip;
import ca.ulaval.glo4003.trip.TripID;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UpcomingTripReservationConfirmationNotificationProcessorTest {
  private static final TravelerID ANY_USER_ID = new TravelerID("40y22g2f");
  private static final String ANY_TITLE = "Trip Notification";
  private static final String ANY_BODY = "Your trip is coming up!";
  private static final TripID ANY_TRIP_ID = TripID.generate();
  private static final Notification ANY_NOTIFICATION = new Notification(ANY_TITLE, ANY_BODY);

  private EmailNotificationSender emailNotificationSenderMock;
  private UpcomingTripNotificationDirector upcomingTripNotificationDirectorMock;
  private UpcomingTripNotificationProcessor upcomingTripNotificationProcessor;

  private User userMock;
  private Trip tripMock;
  private TripInvoice tripInvoiceMock;
  private Reservation reservationMock;
  private Guest guestMock;

  @BeforeEach
  void setUp() {
    this.emailNotificationSenderMock = mock(EmailNotificationSender.class);
    this.upcomingTripNotificationDirectorMock = mock(UpcomingTripNotificationDirector.class);
    this.upcomingTripNotificationProcessor =
        new UpcomingTripNotificationProcessor(
            this.emailNotificationSenderMock, this.upcomingTripNotificationDirectorMock);

    this.userMock = mock(User.class);
    this.tripMock = mock(Trip.class);
    this.tripInvoiceMock = mock(TripInvoice.class);
    this.reservationMock = mock(Reservation.class);
    this.guestMock = mock(Guest.class);
  }

  @Test
  void givenUserAndTrip_whenNotify_thenSendEmail() {
    this.givenUserAndTrip();

    this.upcomingTripNotificationProcessor.notify(List.of(this.userMock), this.tripMock);

    verify(this.emailNotificationSenderMock).sendEmail(this.userMock, ANY_NOTIFICATION);
    verify(this.userMock).notifiedForTrip(ANY_TRIP_ID);
  }

  @Test
  void givenMultipleUsers_whenNotify_thenSendEmailsToAll() {
    User anotherUserMock = mock(User.class);
    this.givenMultipleUsers(anotherUserMock);

    this.upcomingTripNotificationProcessor.notify(
        List.of(this.userMock, anotherUserMock), this.tripMock);

    verify(this.emailNotificationSenderMock).sendEmail(this.userMock, ANY_NOTIFICATION);
    verify(this.userMock).notifiedForTrip(ANY_TRIP_ID);
    verify(anotherUserMock).notifiedForTrip(ANY_TRIP_ID);
    verify(this.emailNotificationSenderMock).sendEmail(anotherUserMock, ANY_NOTIFICATION);
  }

  @Test
  void givenNonExistentInvoice_whenNotify_thenThrowInvoiceNotFoundException() {
    this.givenNonExistentInvoice();

    assertThrows(
        InvoiceNotFoundException.class,
        () -> this.upcomingTripNotificationProcessor.notify(List.of(this.userMock), this.tripMock));
  }

  @Test
  void givenNonExistentReservation_whenNotify_thenThrowReservationNotFoundException() {
    this.givenNonExistantReservation();

    assertThrows(
        ReservationNotFoundException.class,
        () -> this.upcomingTripNotificationProcessor.notify(List.of(this.userMock), this.tripMock));
  }

  @Test
  void givenExistentInvoiceAndAGuest_whenNotify_thenBuildNotificationForGuest() {
    this.givenExistentInvoice();
    this.givenGuestsWithUser();
    when(this.upcomingTripNotificationDirectorMock.buildGuestNotification(
            this.guestMock, this.tripMock, this.tripInvoiceMock))
        .thenReturn(ANY_NOTIFICATION);

    this.upcomingTripNotificationProcessor.notify(List.of(this.userMock), this.tripMock);

    verify(this.upcomingTripNotificationDirectorMock)
        .buildGuestNotification(this.guestMock, this.tripMock, this.tripInvoiceMock);
  }

  @Test
  void givenExistentInvoiceAndAGuest_whenNotify_thenSendEmailToGuestToo() {
    this.givenExistentInvoice();
    this.givenGuestsWithUser();

    this.upcomingTripNotificationProcessor.notify(List.of(this.userMock), this.tripMock);

    verify(this.emailNotificationSenderMock).sendEmail(this.guestMock, ANY_NOTIFICATION);
  }

  private void givenUserAndTrip() {
    when(this.tripMock.getId()).thenReturn(ANY_TRIP_ID);
    when(this.userMock.getInvoiceForTrip(ANY_TRIP_ID)).thenReturn(this.tripInvoiceMock);

    when(this.userMock.findFirstReservationOfTrip(ANY_TRIP_ID))
        .thenReturn(Optional.of(this.reservationMock));
    when(this.upcomingTripNotificationDirectorMock.buildUserNotification(
            this.userMock, this.tripMock, this.tripInvoiceMock, this.reservationMock))
        .thenReturn(ANY_NOTIFICATION);
  }

  private void givenMultipleUsers(User anotherUserMock) {
    when(this.tripMock.getId()).thenReturn(ANY_TRIP_ID);
    when(this.userMock.getInvoiceForTrip(ANY_TRIP_ID)).thenReturn(this.tripInvoiceMock);

    when(anotherUserMock.getInvoiceForTrip(ANY_TRIP_ID)).thenReturn(this.tripInvoiceMock);
    when(this.userMock.findFirstReservationOfTrip(ANY_TRIP_ID))
        .thenReturn(Optional.of(this.reservationMock));
    when(anotherUserMock.findFirstReservationOfTrip(ANY_TRIP_ID))
        .thenReturn(Optional.of(this.reservationMock));
    when(this.upcomingTripNotificationDirectorMock.buildUserNotification(
            this.userMock, this.tripMock, this.tripInvoiceMock, this.reservationMock))
        .thenReturn(ANY_NOTIFICATION);
    when(this.upcomingTripNotificationDirectorMock.buildUserNotification(
            anotherUserMock, this.tripMock, this.tripInvoiceMock, this.reservationMock))
        .thenReturn(ANY_NOTIFICATION);
  }

  private void givenNonExistentInvoice() {
    when(this.tripMock.getId()).thenReturn(ANY_TRIP_ID);
    when(this.userMock.getInvoiceForTrip(ANY_TRIP_ID)).thenThrow(new InvoiceNotFoundException());
    when(this.userMock.findFirstReservationOfTrip(ANY_TRIP_ID))
        .thenReturn(Optional.ofNullable(this.reservationMock));
  }

  private void givenNonExistantReservation() {
    when(this.tripMock.getId()).thenReturn(ANY_TRIP_ID);
    when(this.userMock.getInvoiceForTrip(ANY_TRIP_ID)).thenReturn(this.tripInvoiceMock);
    when(this.userMock.findFirstReservationOfTrip(ANY_TRIP_ID)).thenReturn(Optional.empty());
  }

  private void givenExistentInvoice() {
    when(this.tripMock.getId()).thenReturn(ANY_TRIP_ID);
    when(this.userMock.getInvoiceForTrip(ANY_TRIP_ID)).thenReturn(this.tripInvoiceMock);
    when(this.userMock.findFirstReservationOfTrip(ANY_TRIP_ID))
        .thenReturn(Optional.ofNullable(this.reservationMock));
    when(this.upcomingTripNotificationDirectorMock.buildGuestNotification(
            this.guestMock, this.tripMock, this.tripInvoiceMock))
        .thenReturn(ANY_NOTIFICATION);
  }

  private void givenGuestsWithUser() {
    when(this.tripMock.retrieveAllGuestsOfTraveler(ANY_USER_ID))
        .thenReturn(List.of(this.guestMock));
    when(this.userMock.getId()).thenReturn(ANY_USER_ID);
  }
}
