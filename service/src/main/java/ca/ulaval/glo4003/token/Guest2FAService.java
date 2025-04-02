package ca.ulaval.glo4003.token;

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

public class Guest2FAService {
  private final TripRepository tripRepository;
  private final AuthTokenRepository authTokenRepository;
  private final AuthTokenFactory authTokenFactory;
  private final EmailNotificationSender emailNotificationSender;
  private final Code2FANotificationFactory code2FANotificationFactory;

  public Guest2FAService(
      TripRepository tripRepository,
      AuthTokenRepository authTokenRepository,
      AuthTokenFactory authTokenFactory,
      EmailNotificationSender emailNotificationSender,
      Code2FANotificationFactory code2FANotificationFactory) {
    this.tripRepository = tripRepository;
    this.authTokenRepository = authTokenRepository;
    this.authTokenFactory = authTokenFactory;
    this.emailNotificationSender = emailNotificationSender;
    this.code2FANotificationFactory = code2FANotificationFactory;
  }

  public void send2FAToken(TripID tripID, TravelerID travelerID) {
    Trip trip = this.tripRepository.get(tripID).orElseThrow(TripNotFoundException::new);
    Optional<Guest> guest = trip.retrieveGuest(travelerID);
    guest.ifPresent(this::sendCode);
  }

  private void sendCode(Guest guest) {
    AuthToken authToken = this.authTokenFactory.createToken(guest.getId());
    this.authTokenRepository.save(authToken);

    Notification notification = this.code2FANotificationFactory.build(guest, authToken);
    this.emailNotificationSender.sendEmail(guest, notification);
  }
}
