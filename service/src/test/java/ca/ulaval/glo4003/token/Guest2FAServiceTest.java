package ca.ulaval.glo4003.token;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.notification.EmailNotificationSender;
import ca.ulaval.glo4003.notification.Notification;
import ca.ulaval.glo4003.notification.types.Code2FANotificationFactory;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.guest.Guest;
import ca.ulaval.glo4003.trip.Trip;
import ca.ulaval.glo4003.trip.TripID;
import ca.ulaval.glo4003.trip.TripRepository;
import ca.ulaval.glo4003.trip.exception.TripNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Guest2FAServiceTest {
  private static final TripID ANY_TRIP_ID = TripID.generate();
  private static final TravelerID ANY_TRAVELER_ID = new TravelerID("6h04h");
  private static final Notification CODE_2FA_NOTIFICATION = new Notification("title", "body");
  private Trip tripMock;
  private Guest guestMock;
  private TripRepository tripRepositoryMock;
  private AuthTokenRepository authTokenRepositoryMock;
  private AuthTokenFactory authTokenFactoryMock;
  private EmailNotificationSender emailNotificationSenderMock;
  private Code2FANotificationFactory code2FANotificationFactoryMock;
  private Guest2FAService guest2FAService;

  @BeforeEach
  void setUp() {
    this.tripRepositoryMock = mock(TripRepository.class);
    this.authTokenRepositoryMock = mock(AuthTokenRepository.class);
    this.authTokenFactoryMock = mock(AuthTokenFactory.class);
    this.emailNotificationSenderMock = mock(EmailNotificationSender.class);
    this.code2FANotificationFactoryMock = mock(Code2FANotificationFactory.class);

    this.guest2FAService =
        new Guest2FAService(
            this.tripRepositoryMock,
            this.authTokenRepositoryMock,
            this.authTokenFactoryMock,
            this.emailNotificationSenderMock,
            this.code2FANotificationFactoryMock);
  }

  @Test
  void givenInvalidTripID_whenSending2FAToken_thenThrowTripNotFoundException() {
    when(this.tripRepositoryMock.get(ANY_TRIP_ID)).thenReturn(Optional.empty());

    assertThrows(
        TripNotFoundException.class,
        () -> this.guest2FAService.send2FAToken(ANY_TRIP_ID, ANY_TRAVELER_ID));
  }

  @Test
  void givenInvalidTravelerID_whenSending2FAToken_thenDoNothing() {
    this.givenTripExists();

    this.guest2FAService.send2FAToken(ANY_TRIP_ID, ANY_TRAVELER_ID);

    verify(this.authTokenFactoryMock, never()).createToken(ANY_TRAVELER_ID);
  }

  @Test
  void givenValidGuest_whenSending2FAToken_thenSendNotification() {
    this.givenValidGuest();
    this.guest2FAService.send2FAToken(ANY_TRIP_ID, ANY_TRAVELER_ID);

    verify(this.emailNotificationSenderMock).sendEmail(this.guestMock, CODE_2FA_NOTIFICATION);
  }

  private void givenTripExists() {
    this.tripMock = mock(Trip.class);
    when(this.tripRepositoryMock.get(ANY_TRIP_ID)).thenReturn(Optional.of(this.tripMock));
  }

  private void givenValidGuest() {
    this.givenTripExists();
    this.guestMock = mock(Guest.class);
    when(this.tripMock.retrieveGuest(ANY_TRAVELER_ID)).thenReturn(Optional.of(this.guestMock));
    when(this.guestMock.getId()).thenReturn(ANY_TRAVELER_ID);

    AuthToken anyAuthToken = mock(AuthToken.class);
    when(this.authTokenFactoryMock.createToken(ANY_TRAVELER_ID)).thenReturn(anyAuthToken);
    when(this.code2FANotificationFactoryMock.build(this.guestMock, anyAuthToken))
        .thenReturn(CODE_2FA_NOTIFICATION);
  }
}
