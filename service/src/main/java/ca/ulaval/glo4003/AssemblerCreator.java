package ca.ulaval.glo4003;

import ca.ulaval.glo4003.hosting.assembler.HostingDtoAssembler;
import ca.ulaval.glo4003.hosting.reservation.assembler.CreatedHostingReservationDtoAssembler;
import ca.ulaval.glo4003.invoice.assembler.InvoiceDtoAssembler;
import ca.ulaval.glo4003.payment.assembler.PaymentConfirmationDtoAssembler;
import ca.ulaval.glo4003.profile.assembler.ProfileTripDtoAssembler;
import ca.ulaval.glo4003.reservation.assembler.CreatedReservationDtoAssembler;
import ca.ulaval.glo4003.transport.assembler.ReservableTransportDtoAssembler;
import ca.ulaval.glo4003.transport.assembler.ReservableTransportStopDtoAssembler;
import ca.ulaval.glo4003.trip.assembler.TripDtoAssembler;

public class AssemblerCreator {

  public InvoiceDtoAssembler createInvoiceDtoAssembler() {
    return new InvoiceDtoAssembler();
  }

  public CreatedReservationDtoAssembler createCreatedReservationDtoAssembler() {
    return new CreatedReservationDtoAssembler(this.createInvoiceDtoAssembler());
  }

  public PaymentConfirmationDtoAssembler createPaymentConfirmationDtoAssembler() {
    return new PaymentConfirmationDtoAssembler(this.createInvoiceDtoAssembler());
  }

  public ProfileTripDtoAssembler createProfileTripDtoAssembler() {
    return new ProfileTripDtoAssembler(
        this.createInvoiceDtoAssembler(), this.createCreatedReservationDtoAssembler());
  }

  public TripDtoAssembler createTripDtoAssembler() {
    return new TripDtoAssembler();
  }

  public HostingDtoAssembler createHostingDtoAssembler() {
    return new HostingDtoAssembler();
  }

  public CreatedHostingReservationDtoAssembler createCreatedHostingReservationDtoAssembler() {
    return new CreatedHostingReservationDtoAssembler();
  }

  public ReservableTransportDtoAssembler createOrganizedBusDtoAssembler() {
    return new ReservableTransportDtoAssembler(this.createOrganizedBusStopDtoAssembler());
  }

  public ReservableTransportStopDtoAssembler createOrganizedBusStopDtoAssembler() {
    return new ReservableTransportStopDtoAssembler();
  }
}
