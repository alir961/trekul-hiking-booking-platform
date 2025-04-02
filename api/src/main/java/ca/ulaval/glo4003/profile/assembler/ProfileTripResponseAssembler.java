package ca.ulaval.glo4003.profile.assembler;

import ca.ulaval.glo4003.hiking.assembler.HikingResponseAssembler;
import ca.ulaval.glo4003.hosting.assembler.HostingResponseAssembler;
import ca.ulaval.glo4003.invoice.assembler.InvoiceResponseAssembler;
import ca.ulaval.glo4003.profile.dto.ProfileTripDto;
import ca.ulaval.glo4003.profile.response.ProfileTripResponse;
import ca.ulaval.glo4003.reservation.assembler.ReservationResponseAssembler;
import jakarta.inject.Inject;
import java.util.Collection;
import java.util.List;

public class ProfileTripResponseAssembler {
  private final InvoiceResponseAssembler invoiceResponseAssembler;
  private final HikingResponseAssembler hikingResponseAssembler;
  private final ReservationResponseAssembler reservationResponseAssembler;
  private final HostingResponseAssembler hostingResponseAssembler;

  @Inject
  public ProfileTripResponseAssembler(
      InvoiceResponseAssembler invoiceResponseAssembler,
      HikingResponseAssembler hikingResponseAssembler,
      ReservationResponseAssembler essentialReservationElementsResponseAssembler,
      HostingResponseAssembler hostingResponseAssembler) {
    this.invoiceResponseAssembler = invoiceResponseAssembler;
    this.hikingResponseAssembler = hikingResponseAssembler;
    this.reservationResponseAssembler = essentialReservationElementsResponseAssembler;
    this.hostingResponseAssembler = hostingResponseAssembler;
  }

  public List<ProfileTripResponse> assemble(Collection<ProfileTripDto> trips) {
    return trips.stream().map(this::assemble).toList();
  }

  public ProfileTripResponse assemble(ProfileTripDto trip) {
    return new ProfileTripResponse(
        trip.tripID().toString(),
        this.hikingResponseAssembler.assemble(trip.hiking()),
        trip.date().toString(),
        this.invoiceResponseAssembler.assembleShort(trip.invoice()),
        this.reservationResponseAssembler.assembleStatus(trip.reservation()),
        this.hostingResponseAssembler.assemble(trip.hostingReservation()));
  }
}
