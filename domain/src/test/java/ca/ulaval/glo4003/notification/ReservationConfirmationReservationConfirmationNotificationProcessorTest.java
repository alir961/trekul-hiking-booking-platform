package ca.ulaval.glo4003.notification;

import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.notification.types.ReservationConfirmationNotificationFactory;
import ca.ulaval.glo4003.reservation.Reservation;
import ca.ulaval.glo4003.traveler.guest.Guest;
import ca.ulaval.glo4003.traveler.user.User;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReservationConfirmationReservationConfirmationNotificationProcessorTest {

  private ReservationConfirmationNotificationProcessor reservationConfirmationNotificationProcessor;
  private ReservationConfirmationNotificationFactory notificationFactoryMock;
  private EmailNotificationSender emailNotificationSenderMock;
  private Reservation reservationMock;
  private User userMock;
  private Notification notificationMock;
  private Guest guest1Mock;
  private Guest guest2Mock;

  @BeforeEach
  void setUp() {
    this.notificationFactoryMock = mock(ReservationConfirmationNotificationFactory.class);
    this.emailNotificationSenderMock = mock(EmailNotificationSender.class);
    this.reservationMock = mock(Reservation.class);
    this.userMock = mock(User.class);
    this.notificationMock = mock(Notification.class);
    this.guest1Mock = mock(Guest.class);
    this.guest2Mock = mock(Guest.class);

    this.reservationConfirmationNotificationProcessor =
        new ReservationConfirmationNotificationProcessor(
            this.notificationFactoryMock, this.emailNotificationSenderMock);
  }

  @Test
  void givenReservationAndUser_whenNotifyUser_thenFactoryBuildsNotification() {
    when(this.notificationFactoryMock.buildConfirmation(this.reservationMock))
        .thenReturn(this.notificationMock);
    when(this.reservationMock.listGuests()).thenReturn(List.of(this.guest1Mock, this.guest2Mock));

    this.reservationConfirmationNotificationProcessor.notify(this.reservationMock, this.userMock);

    verify(this.notificationFactoryMock).buildConfirmation(this.reservationMock);
  }

  @Test
  void givenReservationAndUser_whenNotifyUser_thenEmailIsSentToUser() {
    when(this.notificationFactoryMock.buildConfirmation(this.reservationMock))
        .thenReturn(this.notificationMock);
    when(this.reservationMock.listGuests()).thenReturn(List.of(this.guest1Mock, this.guest2Mock));

    this.reservationConfirmationNotificationProcessor.notify(this.reservationMock, this.userMock);

    verify(this.emailNotificationSenderMock).sendEmail(this.userMock, this.notificationMock);
  }

  @Test
  void givenReservationAndUser_whenNotifyUser_thenEmailIsSentToAllGuests() {
    when(this.notificationFactoryMock.buildConfirmation(this.reservationMock))
        .thenReturn(this.notificationMock);
    List<Guest> guests = List.of(this.guest1Mock, this.guest2Mock);
    when(this.reservationMock.listGuests()).thenReturn(guests);

    this.reservationConfirmationNotificationProcessor.notify(this.reservationMock, this.userMock);

    verify(this.emailNotificationSenderMock).sendEmail(guests, this.notificationMock);
  }

  @Test
  void givenReservationWithNoGuests_whenNotifyUser_thenEmailIsStillSentToUserButNoGuests() {
    when(this.notificationFactoryMock.buildConfirmation(this.reservationMock))
        .thenReturn(this.notificationMock);
    when(this.reservationMock.listGuests()).thenReturn(List.of());

    this.reservationConfirmationNotificationProcessor.notify(this.reservationMock, this.userMock);

    verify(this.emailNotificationSenderMock).sendEmail(this.userMock, this.notificationMock);
    verify(this.emailNotificationSenderMock).sendEmail(List.of(), this.notificationMock);
  }
}
