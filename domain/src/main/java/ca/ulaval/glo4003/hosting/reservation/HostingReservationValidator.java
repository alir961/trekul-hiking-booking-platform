package ca.ulaval.glo4003.hosting.reservation;

import ca.ulaval.glo4003.hosting.Hosting;
import ca.ulaval.glo4003.hosting.reservation.exception.HostingReservationExistingForTripException;
import ca.ulaval.glo4003.hosting.reservation.exception.HostingReservationImpossibleException;
import ca.ulaval.glo4003.hosting.reservation.exception.HostingReservationUnavailableException;
import ca.ulaval.glo4003.trip.Trip;

public class HostingReservationValidator {

  public void validate(Hosting hosting, Trip trip) {
    if (trip.hasHostingReservation()) {
      throw new HostingReservationExistingForTripException();
    }
    if (!hosting.isCompatibleWithTrip(trip)) {
      throw new HostingReservationImpossibleException();
    }
    if (!trip.isBlocked()) {
      throw new HostingReservationUnavailableException();
    }
  }
}
