package ca.ulaval.glo4003.reservation;

import ca.ulaval.glo4003.invoice.TripInvoice;
import ca.ulaval.glo4003.invoice.assembler.InvoiceDtoAssembler;
import ca.ulaval.glo4003.invoice.dto.InvoiceDto;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.user.User;
import ca.ulaval.glo4003.traveler.user.UserRepository;
import ca.ulaval.glo4003.traveler.user.exception.UserNotFoundException;
import ca.ulaval.glo4003.trip.Trip;
import ca.ulaval.glo4003.trip.TripID;
import ca.ulaval.glo4003.trip.TripRepository;
import ca.ulaval.glo4003.trip.exception.TripNotFoundException;

public class CancelReservationService {
  private final UserRepository userRepository;
  private final TripRepository tripRepository;
  private final InvoiceDtoAssembler invoiceDtoAssembler;

  public CancelReservationService(
      UserRepository userRepository,
      TripRepository tripRepository,
      InvoiceDtoAssembler invoiceDtoAssembler) {
    this.userRepository = userRepository;
    this.tripRepository = tripRepository;
    this.invoiceDtoAssembler = invoiceDtoAssembler;
  }

  public InvoiceDto cancelReservation(TravelerID travelerID, ReservationID reservationID) {
    User user = this.userRepository.getById(travelerID).orElseThrow(UserNotFoundException::new);
    TripID tripID = user.getTripIDForReservation(reservationID);
    Trip trip = this.tripRepository.get(tripID).orElseThrow(TripNotFoundException::new);

    Reservation cancelledReservation = user.cancelReservation(reservationID, trip.getDate());
    trip.releaseSeat(cancelledReservation);

    TripInvoice invoice = user.getInvoiceForTrip(tripID);
    this.userRepository.save(user);
    this.tripRepository.save(trip);
    return this.invoiceDtoAssembler.assemble(invoice, travelerID);
  }
}
