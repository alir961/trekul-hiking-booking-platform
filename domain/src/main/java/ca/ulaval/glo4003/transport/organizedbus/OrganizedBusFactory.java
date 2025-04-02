package ca.ulaval.glo4003.transport.organizedbus;

import ca.ulaval.glo4003.transport.TransportConfig;
import ca.ulaval.glo4003.transport.TransportID;
import java.util.Collection;

public class OrganizedBusFactory {
  private final TransportConfig organizedBusConfig;

  public OrganizedBusFactory(TransportConfig organizedBusConfig) {
    this.organizedBusConfig = organizedBusConfig;
  }

  public OrganizedBus createOrganizedBus(Collection<TransportStop> stops) {
    return new OrganizedBus(
        TransportID.generate(),
        this.organizedBusConfig.getMaximumSeats(),
        this.organizedBusConfig.getCost(),
        stops);
  }
}
