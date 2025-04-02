package ca.ulaval.glo4003.reservation.assembler;

import ca.ulaval.glo4003.group.assembler.GroupResponseAssembler;
import ca.ulaval.glo4003.invoice.assembler.InvoiceResponseAssembler;
import ca.ulaval.glo4003.reservation.dto.CreatedReservationDto;
import ca.ulaval.glo4003.reservation.response.ReservationResponse;
import ca.ulaval.glo4003.reservation.response.ReservationStatusResponse;
import jakarta.inject.Inject;

public class ReservationResponseAssembler {
  private final InvoiceResponseAssembler invoiceResponseAssembler;
  private final GroupResponseAssembler guestResponseAssembler;

  @Inject
  public ReservationResponseAssembler(
      InvoiceResponseAssembler invoiceResponseAssembler,
      GroupResponseAssembler guestResponseAssembler) {
    this.invoiceResponseAssembler = invoiceResponseAssembler;
    this.guestResponseAssembler = guestResponseAssembler;
  }

  public ReservationResponse assembleConfirmation(CreatedReservationDto createdReservationDto) {
    return new ReservationResponse(
        createdReservationDto.reservationID().toString(),
        this.invoiceResponseAssembler.assemble(createdReservationDto.invoiceDto()),
        this.guestResponseAssembler.assemble(createdReservationDto.group()),
        createdReservationDto.minimumDue().toString(),
        createdReservationDto.expiration());
  }

  public ReservationStatusResponse assembleStatus(CreatedReservationDto reservationDto) {
    return new ReservationStatusResponse(
        reservationDto.status().toString(), reservationDto.transportType().toString());
  }
}
