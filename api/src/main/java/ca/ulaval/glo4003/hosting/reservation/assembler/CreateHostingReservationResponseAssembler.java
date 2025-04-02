package ca.ulaval.glo4003.hosting.reservation.assembler;

import ca.ulaval.glo4003.hosting.reservation.dto.CreatedHostingReservationDto;
import ca.ulaval.glo4003.hosting.reservation.response.HostingReservationResponse;

public class CreateHostingReservationResponseAssembler {
  public HostingReservationResponse assemble(
      CreatedHostingReservationDto createdReservationHostingDto) {
    return new HostingReservationResponse(
        createdReservationHostingDto.hostingID().toString(),
        createdReservationHostingDto.tripID().toString(),
        createdReservationHostingDto.priceRange().toString());
  }
}
