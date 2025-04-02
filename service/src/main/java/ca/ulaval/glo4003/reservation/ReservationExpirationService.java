package ca.ulaval.glo4003.reservation;

import ca.ulaval.glo4003.traveler.user.User;
import ca.ulaval.glo4003.traveler.user.UserRepository;
import ca.ulaval.glo4003.trip.Trip;
import ca.ulaval.glo4003.trip.TripRepository;
import ca.ulaval.glo4003.trip.exception.TripNotFoundException;
import java.util.Collection;
import java.util.List;

public class ReservationExpirationService {

  private final UserRepository userRepository;
  private final TripRepository tripRepository;

  public ReservationExpirationService(
      UserRepository userRepository, TripRepository tripRepository) {
    this.userRepository = userRepository;
    this.tripRepository = tripRepository;
  }

  public void cancelExpiredReservations() {
    List<User> users = this.userRepository.getAll();
    users.forEach(
        user -> {
          Collection<Reservation> expiredReservations = user.cancelExpiredReservations();
          expiredReservations.forEach(
              reservation -> {
                Trip canceledTrip = this.cancelTripReservation(reservation);
                this.tripRepository.save(canceledTrip);
              });
          this.userRepository.save(user);
        });
  }

  private Trip cancelTripReservation(Reservation reservation) {
    Trip trip =
        this.tripRepository.get(reservation.getTripID()).orElseThrow(TripNotFoundException::new);
    trip.releaseSeat(reservation);
    return trip;
  }
}
