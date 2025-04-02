package ca.ulaval.glo4003.profile.assembler;

import ca.ulaval.glo4003.invoice.assembler.InvoiceDtoAssembler;
import ca.ulaval.glo4003.invoice.dto.InvoiceDto;
import ca.ulaval.glo4003.profile.dto.ProfileTripDto;
import ca.ulaval.glo4003.reservation.Reservation;
import ca.ulaval.glo4003.reservation.assembler.CreatedReservationDtoAssembler;
import ca.ulaval.glo4003.reservation.dto.CreatedReservationDto;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.trip.Trip;

public class ProfileTripDtoAssembler {
  private final InvoiceDtoAssembler invoiceDtoAssembler;
  private final CreatedReservationDtoAssembler reservationDtoAssembler;

  public ProfileTripDtoAssembler(
      InvoiceDtoAssembler invoiceDtoAssembler,
      CreatedReservationDtoAssembler createdReservationDtoAssembler) {
    this.invoiceDtoAssembler = invoiceDtoAssembler;
    this.reservationDtoAssembler = createdReservationDtoAssembler;
  }

  public ProfileTripDto assemble(Trip trip, Reservation reservation, TravelerID travelerID) {
    InvoiceDto invoiceDto = this.invoiceDtoAssembler.assemble(reservation.getInvoice(), travelerID);
    CreatedReservationDto reservationDto = this.reservationDtoAssembler.assemble(reservation);
    return new ProfileTripDto(
        trip.getHiking(),
        trip.getId(),
        trip.getDate(),
        invoiceDto,
        reservationDto,
        trip.getHostingReservation());
  }
}
