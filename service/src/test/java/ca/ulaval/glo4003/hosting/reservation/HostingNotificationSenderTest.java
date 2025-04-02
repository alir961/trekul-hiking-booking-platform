package ca.ulaval.glo4003.hosting.reservation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.hosting.Hosting;
import ca.ulaval.glo4003.notification.EmailNotificationSender;
import ca.ulaval.glo4003.notification.Notification;
import ca.ulaval.glo4003.notification.types.HostingConfirmedNotificationFactory;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.guest.Guest;
import ca.ulaval.glo4003.traveler.user.User;
import ca.ulaval.glo4003.traveler.user.UserRepository;
import ca.ulaval.glo4003.trip.Trip;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HostingNotificationSenderTest {
  private static final TravelerID ANY_USER_ID = new TravelerID("1245");
  private static final List<TravelerID> ANY_USER_ID_LIST = List.of(ANY_USER_ID);
  private User userMock;
  private Guest guestMock;
  private Notification notificationMock;
  private Trip tripMock;
  private Hosting hostingMock;
  private UserRepository userRepositoryMock;
  private HostingConfirmedNotificationFactory hostingConfirmedNotificationFactoryMock;
  private EmailNotificationSender emailNotificationSenderMock;
  private HostingNotificationSender hostingNotificationSender;

  @BeforeEach
  void setUp() {
    this.userMock = mock(User.class);
    this.guestMock = mock(Guest.class);
    this.notificationMock = mock(Notification.class);
    this.tripMock = mock(Trip.class);
    this.hostingMock = mock(Hosting.class);

    this.userRepositoryMock = mock(UserRepository.class);
    this.hostingConfirmedNotificationFactoryMock = mock(HostingConfirmedNotificationFactory.class);
    this.emailNotificationSenderMock = mock(EmailNotificationSender.class);

    this.hostingNotificationSender =
        new HostingNotificationSender(
            this.userRepositoryMock,
            this.hostingConfirmedNotificationFactoryMock,
            this.emailNotificationSenderMock);
  }

  @Test
  void whenNotifyUsers_thenNotificationIsCreated() {
    this.hostingNotificationSender.notifyUsers(this.tripMock, this.hostingMock);

    verify(this.hostingConfirmedNotificationFactoryMock).build(this.tripMock, this.hostingMock);
  }

  @Test
  void whenNotifyUsers_thenSendsEmailToEachUser() {
    when(this.tripMock.retrieveReservedUsers()).thenReturn(List.of(ANY_USER_ID));
    when(this.userRepositoryMock.getBunch(ANY_USER_ID_LIST)).thenReturn(List.of(this.userMock));
    when(this.hostingConfirmedNotificationFactoryMock.build(this.tripMock, this.hostingMock))
        .thenReturn(this.notificationMock);

    this.hostingNotificationSender.notifyUsers(this.tripMock, this.hostingMock);

    verify(this.emailNotificationSenderMock)
        .sendEmail(List.of((this.userMock)), this.notificationMock);
  }

  @Test
  void whenNotifyUsers_thenSendsEmailToEachGuest() {
    when(this.tripMock.retrieveAllGuests()).thenReturn(List.of(this.guestMock));
    when(this.hostingConfirmedNotificationFactoryMock.build(this.tripMock, this.hostingMock))
        .thenReturn(this.notificationMock);

    this.hostingNotificationSender.notifyUsers(this.tripMock, this.hostingMock);

    verify(this.emailNotificationSenderMock)
        .sendEmail(List.of((this.guestMock)), this.notificationMock);
  }
}
