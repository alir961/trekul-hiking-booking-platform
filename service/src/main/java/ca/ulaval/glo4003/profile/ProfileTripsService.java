package ca.ulaval.glo4003.profile;

import ca.ulaval.glo4003.invoice.TripInvoice;
import ca.ulaval.glo4003.profile.assembler.ProfileTripDtoAssembler;
import ca.ulaval.glo4003.profile.dto.ProfileTripDto;
import ca.ulaval.glo4003.reservation.Reservation;
import ca.ulaval.glo4003.reservation.ReservationStatus;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.user.User;
import ca.ulaval.glo4003.traveler.user.UserRepository;
import ca.ulaval.glo4003.traveler.user.exception.UserNotFoundException;
import ca.ulaval.glo4003.trip.Trip;
import ca.ulaval.glo4003.trip.TripRepository;
import java.util.List;
import java.util.Optional;

public class ProfileTripsService {
  private final TripRepository tripRepository;
  private final UserRepository userRepository;
  private final ProfileTripDtoAssembler profileTripDtoAssembler;

  public ProfileTripsService(
      TripRepository tripRepository,
      UserRepository userRepository,
      ProfileTripDtoAssembler profileTripDtoAssembler) {
    this.tripRepository = tripRepository;
    this.userRepository = userRepository;
    this.profileTripDtoAssembler = profileTripDtoAssembler;
  }

  public List<ProfileTripDto> searchTrips(TravelerID userID, ReservationStatus status) {
    User user = this.userRepository.getById(userID).orElseThrow(UserNotFoundException::new);

    return user.fetchReservationsOfStatus(status).stream()
        .map(reservation -> this.buildProfileTripDto(reservation, user))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .toList();
  }

  private Optional<ProfileTripDto> buildProfileTripDto(Reservation reservation, User user) {
    Optional<TripInvoice> tripInvoice = user.getTripInvoice(reservation.getInvoiceID());

    Optional<Trip> trip = this.tripRepository.get(reservation.getTripID());

    if (trip.isPresent() && tripInvoice.isPresent()) {
      return Optional.of(
          this.profileTripDtoAssembler.assemble(trip.get(), reservation, user.getId()));
    } else {
      return Optional.empty();
    }
  }
}
