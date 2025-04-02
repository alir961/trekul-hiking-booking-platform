package ca.ulaval.glo4003.hosting.assembler;

import ca.ulaval.glo4003.hosting.reservation.HostingReservation;
import ca.ulaval.glo4003.hosting.response.HostingResponse;

public class HostingResponseAssembler {
  public HostingResponse assemble(HostingReservation hostingReservation) {
    if (hostingReservation == null) {
      return null;
    }
    return new HostingResponse(
        hostingReservation.getHostingID().toString(),
        hostingReservation.getHostingName(),
        hostingReservation.getPriceRange().toString());
  }
}
