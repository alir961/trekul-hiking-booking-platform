package ca.ulaval.glo4003.reservation.assembler;

import ca.ulaval.glo4003.invoice.TripInvoice;
import ca.ulaval.glo4003.invoice.assembler.InvoiceDtoAssembler;
import ca.ulaval.glo4003.reservation.Reservation;
import ca.ulaval.glo4003.reservation.dto.CreatedReservationDto;

public class CreatedReservationDtoAssembler {
  private final InvoiceDtoAssembler invoiceDtoAssembler;

  public CreatedReservationDtoAssembler(InvoiceDtoAssembler invoiceDtoAssembler) {
    this.invoiceDtoAssembler = invoiceDtoAssembler;
  }

  public CreatedReservationDto assemble(Reservation reservation) {
    TripInvoice invoice = reservation.getInvoice();
    return new CreatedReservationDto(
        reservation.getReservationID(),
        this.invoiceDtoAssembler.assemble(invoice, reservation.getUserID()),
        reservation.getGroup(),
        invoice.calculateMinimumToPay(reservation.getUserID()),
        reservation.getExpiration(),
        reservation.getStatus(),
        reservation.getTransportType());
  }
}
