package ca.ulaval.glo4003.etl.trip;

import ca.ulaval.glo4003.hiking.Hiking;
import ca.ulaval.glo4003.transport.TransportCollection;
import ca.ulaval.glo4003.transport.TransportCollectionFactory;
import ca.ulaval.glo4003.transport.TransportType;
import ca.ulaval.glo4003.transport.organizedbus.OrganizedBus;
import ca.ulaval.glo4003.transport.organizedbus.OrganizedBusFactory;
import ca.ulaval.glo4003.transport.organizedbus.TransportStop;
import ca.ulaval.glo4003.trip.Trip;
import ca.ulaval.glo4003.trip.TripFactory;
import ca.ulaval.glo4003.trip.TripRepository;
import java.util.Collection;

public class TripLoader {
  private final TripFactory tripFactory;
  private final TripRepository tripRepository;
  private final OrganizedBusFactory organizedBusFactory;
  private final TransportCollectionFactory transportCollectionFactory;

  public TripLoader(
      TripFactory tripFactory,
      TripRepository tripRepository,
      OrganizedBusFactory organizedBusFactory,
      TransportCollectionFactory transportCollectionFactory) {
    this.tripFactory = tripFactory;
    this.tripRepository = tripRepository;
    this.organizedBusFactory = organizedBusFactory;
    this.transportCollectionFactory = transportCollectionFactory;
  }

  public void load(Collection<Hiking> hikings, Collection<TransportStop> busStops) {
    hikings.forEach(hiking -> this.saveNewTrip(hiking, busStops));
  }

  private void saveNewTrip(Hiking hiking, Collection<TransportStop> stops) {
    TransportCollection transportCollection = this.transportCollectionFactory.create();
    OrganizedBus bus = this.organizedBusFactory.createOrganizedBus(stops);
    transportCollection.addTransport(TransportType.ORGANIZED_BUS, bus);
    Trip trip = this.tripFactory.createTrip(hiking, transportCollection);
    this.tripRepository.save(trip);
  }
}
