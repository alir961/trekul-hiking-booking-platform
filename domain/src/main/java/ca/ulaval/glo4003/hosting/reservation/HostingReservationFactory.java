package ca.ulaval.glo4003.hosting.reservation;

import ca.ulaval.glo4003.hosting.Hosting;

public class HostingReservationFactory {
  public HostingReservation create(Hosting hosting) {
    return new HostingReservation(hosting.getID(), hosting.getName(), hosting.getPriceRange());
  }
}
