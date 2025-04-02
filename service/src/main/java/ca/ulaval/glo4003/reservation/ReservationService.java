package ca.ulaval.glo4003.reservation;

import ca.ulaval.glo4003.group.Group;
import ca.ulaval.glo4003.reservation.assembler.CreatedReservationDtoAssembler;
import ca.ulaval.glo4003.reservation.dto.CreatedReservationDto;
import ca.ulaval.glo4003.reservation.dto.ReservationDto;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.user.User;
import ca.ulaval.glo4003.traveler.user.UserRepository;
import ca.ulaval.glo4003.traveler.user.exception.UserNotFoundException;
import ca.ulaval.glo4003.trip.Trip;
import ca.ulaval.glo4003.trip.TripRepository;
import ca.ulaval.glo4003.trip.exception.TripNotFoundException;

public class ReservationService {
  private final UserRepository userRepository;
  private final TripRepository tripRepository;
  private final ReservationFactory reservationFactory;
  private final GroupFactory groupFactory;
  private final CreatedReservationDtoAssembler createdReservationDtoAssembler;

  public ReservationService(
      UserRepository userRepository,
      TripRepository tripRepository,
      ReservationFactory reservationFactory,
      GroupFactory groupFactory,
      CreatedReservationDtoAssembler createdReservationDtoAssembler) {
    this.userRepository = userRepository;
    this.tripRepository = tripRepository;
    this.reservationFactory = reservationFactory;
    this.groupFactory = groupFactory;
    this.createdReservationDtoAssembler = createdReservationDtoAssembler;
  }

  public CreatedReservationDto createReservation(ReservationDto reservationDto) {
    TravelerID userId = reservationDto.userID();
    User user = this.userRepository.getById(userId).orElseThrow(UserNotFoundException::new);
    Group group = this.groupFactory.create(userId, reservationDto.guests());

    Trip tripToReserve =
        this.tripRepository.get(reservationDto.tripID()).orElseThrow(TripNotFoundException::new);
    Reservation reservation =
        this.reservationFactory.create(
            userId, tripToReserve, group, reservationDto.stop(), reservationDto.transportType());
    tripToReserve.reserveSeat(reservation);
    user.addReservation(reservation);

    this.userRepository.save(user);
    this.tripRepository.save(tripToReserve);

    return this.createdReservationDtoAssembler.assemble(reservation);
  }
}
