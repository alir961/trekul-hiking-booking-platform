package ca.ulaval.glo4003.reservation.assembler;

import ca.ulaval.glo4003.guest.assembler.GuestRequestAssembler;
import ca.ulaval.glo4003.reservation.dto.ReservationDto;
import ca.ulaval.glo4003.reservation.request.ReservationRequest;
import ca.ulaval.glo4003.reservation.request.ReservationRequestValidator;
import ca.ulaval.glo4003.transport.TransportType;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.guest.dto.GuestDto;
import ca.ulaval.glo4003.trip.TripID;
import jakarta.inject.Inject;
import java.util.List;

public class ReservationRequestAssembler {
  private final ReservationRequestValidator reservationRequestValidator;
  private final GuestRequestAssembler guestRequestAssembler;

  @Inject
  public ReservationRequestAssembler(
      ReservationRequestValidator reservationRequestValidator,
      GuestRequestAssembler guestRequestAssembler) {
    this.reservationRequestValidator = reservationRequestValidator;
    this.guestRequestAssembler = guestRequestAssembler;
  }

  public ReservationDto assemble(
      ReservationRequest reservationRequest, TravelerID userID, TripID tripID) {
    this.reservationRequestValidator.validate(reservationRequest);
    String stop = "";
    if (reservationRequest.stop() != null) {
      stop = reservationRequest.stop().toUpperCase();
    }

    List<GuestDto> guestDtos = this.guestRequestAssembler.assemble(reservationRequest.guests());
    TransportType transportType =
        TransportType.valueOf(reservationRequest.transportType().toUpperCase());

    return new ReservationDto(userID, tripID, transportType, stop, guestDtos);
  }
}
