package ca.ulaval.glo4003.transport.organizedbus;

import ca.ulaval.glo4003.group.Group;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.reservation.Reservation;
import ca.ulaval.glo4003.transport.ReservableTransport;
import ca.ulaval.glo4003.transport.TransportID;
import ca.ulaval.glo4003.transport.TransportOption;
import ca.ulaval.glo4003.transport.TransportType;
import ca.ulaval.glo4003.transport.exception.TransportFullyBookedException;
import ca.ulaval.glo4003.transport.exception.TransportStopDoesNotExistException;
import ca.ulaval.glo4003.transport.exception.TransportWithoutEnoughPlacesException;
import ca.ulaval.glo4003.traveler.TravelerID;
import java.util.*;

public class OrganizedBus implements ReservableTransport {
  private static final int NUMBER_OF_OPTIONS = 1;
  private final TransportID transportID;
  private final int maximumSeatsAvailable;
  private final Money cost;
  private int currentSeatsAvailable;
  private Map<TravelerID, String> stops = new HashMap<>();
  private Map<TravelerID, Group> groups = new HashMap<>();
  private final Collection<TransportStop> busStops;

  public OrganizedBus(
      TransportID transportID,
      int maximumSeatsAvailable,
      Money cost,
      Collection<TransportStop> busStops) {
    this.transportID = transportID;
    this.maximumSeatsAvailable = maximumSeatsAvailable;
    this.cost = cost;
    this.busStops = busStops;
    this.currentSeatsAvailable = maximumSeatsAvailable;
  }

  public void reserveSeat(String stop, Reservation ongoingReservation) {
    this.checkOngoingValidationPossible(stop, ongoingReservation);
    this.stops.put(ongoingReservation.getUserID(), stop);
    this.groups.put(ongoingReservation.getUserID(), ongoingReservation.getGroup());
    ongoingReservation.setTransportCost(this.cost);
    this.currentSeatsAvailable -= ongoingReservation.calculateTotalTravelers();
  }

  public void cancelSeat(Reservation ongoingReservation) {
    this.groups.remove(ongoingReservation.getUserID());
    this.currentSeatsAvailable += ongoingReservation.calculateTotalTravelers();
  }

  public OrganizedBus copy() {
    OrganizedBus copy =
        new OrganizedBus(this.transportID, this.maximumSeatsAvailable, this.cost, this.busStops);
    copy.stops.putAll(this.stops);
    copy.groups.putAll(this.groups);
    copy.currentSeatsAvailable = this.currentSeatsAvailable;
    return copy;
  }

  @Override
  public boolean equals(Object object) {
    if (object == null) {
      return false;
    }
    if (object.getClass() != this.getClass()) {
      return false;
    }

    final OrganizedBus other = (OrganizedBus) object;
    return this.transportID.equals(other.transportID);
  }

  public boolean isAvailable() {
    return this.currentSeatsAvailable != 0;
  }

  public Money getCost() {
    return this.cost;
  }

  public Collection<TransportStop> getStops() {
    return this.getBusStops();
  }

  public TransportID getTransportID() {
    return this.transportID;
  }

  public Collection<TransportStop> getBusStops() {
    return this.busStops;
  }

  public int getCurrentSeatsAvailable() {
    return this.currentSeatsAvailable;
  }

  public TransportOption getTransportOption() {
    return new TransportOption(
        TransportType.ORGANIZED_BUS, this.isAvailable(), NUMBER_OF_OPTIONS, this.getCost());
  }

  private void checkOngoingValidationPossible(String stop, Reservation ongoingReservation) {
    if (this.currentSeatsAvailable == 0) {
      throw new TransportFullyBookedException();
    } else if (this.currentSeatsAvailable < ongoingReservation.calculateTotalTravelers()) {
      throw new TransportWithoutEnoughPlacesException();
    } else if (!this.hasOrganizedBusStopWithName(stop)) {
      throw new TransportStopDoesNotExistException();
    }
  }

  private boolean hasOrganizedBusStopWithName(String stopName) {
    return this.busStops.stream().anyMatch(organizedBusStop -> organizedBusStop.isNamed(stopName));
  }
}
