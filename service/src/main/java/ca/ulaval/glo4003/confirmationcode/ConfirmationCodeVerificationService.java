package ca.ulaval.glo4003.confirmationcode;

import ca.ulaval.glo4003.confirmationcode.exception.ConfirmationCodeNotFoundException;
import ca.ulaval.glo4003.traveler.user.User;
import ca.ulaval.glo4003.traveler.user.UserRepository;
import ca.ulaval.glo4003.trip.Trip;
import ca.ulaval.glo4003.trip.TripID;
import ca.ulaval.glo4003.trip.TripRepository;
import ca.ulaval.glo4003.trip.exception.TripNotFoundException;

public class ConfirmationCodeVerificationService {
  private final TripRepository tripRepository;
  private final UserRepository userRepository;

  public ConfirmationCodeVerificationService(
      TripRepository tripRepository, UserRepository userRepository) {
    this.tripRepository = tripRepository;
    this.userRepository = userRepository;
  }

  public ValidationResult verifyConfirmationCode(TripID tripID, String code) {
    Trip trip = this.tripRepository.get(tripID).orElseThrow(TripNotFoundException::new);
    User user = this.findUserByConfirmationCode(trip, code);
    return user.validateTripInvoiceForTrip(trip.getId());
  }

  private User findUserByConfirmationCode(Trip trip, String code) {
    return this.userRepository.getBunch(trip.retrieveReservedUsers()).stream()
        .filter(user -> user.hasConfirmationCode(code))
        .findFirst()
        .orElseThrow(ConfirmationCodeNotFoundException::new);
  }
}
