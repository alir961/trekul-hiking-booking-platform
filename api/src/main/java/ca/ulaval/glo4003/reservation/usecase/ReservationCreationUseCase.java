package ca.ulaval.glo4003.reservation.usecase;

import ca.ulaval.glo4003.reservation.ReservationService;
import ca.ulaval.glo4003.reservation.assembler.ReservationRequestAssembler;
import ca.ulaval.glo4003.reservation.assembler.ReservationResponseAssembler;
import ca.ulaval.glo4003.reservation.dto.CreatedReservationDto;
import ca.ulaval.glo4003.reservation.dto.ReservationDto;
import ca.ulaval.glo4003.reservation.request.ReservationRequest;
import ca.ulaval.glo4003.reservation.response.ReservationResponse;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.trip.TripID;
import jakarta.inject.Inject;

public class ReservationCreationUseCase {

  private final ReservationService reservationService;
  private final ReservationRequestAssembler reservationRequestAssembler;
  private final ReservationResponseAssembler reservationResponseAssembler;

  @Inject
  public ReservationCreationUseCase(
      ReservationService reservationService,
      ReservationRequestAssembler reservationRequestAssembler,
      ReservationResponseAssembler reservationResponseAssembler) {
    this.reservationService = reservationService;
    this.reservationRequestAssembler = reservationRequestAssembler;
    this.reservationResponseAssembler = reservationResponseAssembler;
  }

  public ReservationResponse createReservation(
      ReservationRequest reservationRequest, TravelerID userID, TripID tripID) {

    ReservationDto reservationDto =
        this.reservationRequestAssembler.assemble(reservationRequest, userID, tripID);
    CreatedReservationDto createdReservationDto =
        this.reservationService.createReservation(reservationDto);

    return this.reservationResponseAssembler.assembleConfirmation(createdReservationDto);
  }
}
