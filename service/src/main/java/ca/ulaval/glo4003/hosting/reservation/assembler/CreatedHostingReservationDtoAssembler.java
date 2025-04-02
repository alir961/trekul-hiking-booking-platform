package ca.ulaval.glo4003.hosting.reservation.assembler;

import ca.ulaval.glo4003.hosting.reservation.HostingReservation;
import ca.ulaval.glo4003.hosting.reservation.dto.CreatedHostingReservationDto;
import ca.ulaval.glo4003.trip.TripID;

public class CreatedHostingReservationDtoAssembler {
  public CreatedHostingReservationDto assemble(
      TripID tripID, HostingReservation hostingReservation) {
    return new CreatedHostingReservationDto(
        hostingReservation.getHostingID(),
        hostingReservation.getHostingName(),
        tripID,
        hostingReservation.getPriceRange());
  }
}
