package ca.ulaval.glo4003.guest;

import ca.ulaval.glo4003.group.Group;
import ca.ulaval.glo4003.group.exception.GroupNotFoundException;
import ca.ulaval.glo4003.profile.assembler.ProfileTripDtoAssembler;
import ca.ulaval.glo4003.profile.dto.ProfileTripDto;
import ca.ulaval.glo4003.reservation.Reservation;
import ca.ulaval.glo4003.reservation.exception.ReservationNotFoundException;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.user.User;
import ca.ulaval.glo4003.traveler.user.UserRepository;
import ca.ulaval.glo4003.traveler.user.exception.UserNotFoundException;
import ca.ulaval.glo4003.trip.*;
import ca.ulaval.glo4003.trip.exception.TripNotFoundException;

public class GuestTripService {
  private final TripRepository tripRepository;
  private final UserRepository userRepository;
  private final ProfileTripDtoAssembler profileTripDtoAssembler;

  public GuestTripService(
      TripRepository tripRepository,
      UserRepository userRepository,
      ProfileTripDtoAssembler profileTripDtoAssembler) {
    this.tripRepository = tripRepository;
    this.userRepository = userRepository;
    this.profileTripDtoAssembler = profileTripDtoAssembler;
  }

  public ProfileTripDto searchTripByIdAndTravelerId(TripID tripId, TravelerID travelerId) {
    Trip trip =
        this.tripRepository
            .getByIdAndTravelerId(tripId, travelerId)
            .orElseThrow(TripNotFoundException::new);
    Group groupWithTraveler =
        trip.findFirstGroupOfTraveler(travelerId).orElseThrow(GroupNotFoundException::new);
    User userInCharge =
        this.userRepository
            .getById(groupWithTraveler.getUserInCharge())
            .orElseThrow(UserNotFoundException::new);
    Reservation tripReservation =
        userInCharge
            .findFirstReservationOfTrip(tripId)
            .orElseThrow(ReservationNotFoundException::new);

    return this.profileTripDtoAssembler.assemble(trip, tripReservation, travelerId);
  }
}
