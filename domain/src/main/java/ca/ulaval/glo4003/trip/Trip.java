package ca.ulaval.glo4003.trip;

import static ca.ulaval.glo4003.trip.TripStatus.*;

import ca.ulaval.glo4003.group.Group;
import ca.ulaval.glo4003.hiking.Hiking;
import ca.ulaval.glo4003.hiking.HikingRegion;
import ca.ulaval.glo4003.hosting.Hosting;
import ca.ulaval.glo4003.hosting.reservation.HostingReservation;
import ca.ulaval.glo4003.hosting.reservation.exception.HostingReservationExistingForTripException;
import ca.ulaval.glo4003.invoice.BillableItems;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.reservation.Reservation;
import ca.ulaval.glo4003.transport.ReservableTransport;
import ca.ulaval.glo4003.transport.TransportCollection;
import ca.ulaval.glo4003.transport.TransportOption;
import ca.ulaval.glo4003.transport.TransportType;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.guest.Guest;
import ca.ulaval.glo4003.traveler.user.User;
import ca.ulaval.glo4003.trip.exception.TripAlreadyContainsTravelerException;
import ca.ulaval.glo4003.trip.exception.TripNotAvailableException;
import ca.ulaval.glo4003.trip.exception.TripNotEnoughSeatsException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class Trip {
  private final TripID id;
  private final int maximumSeats;
  private final LocalDate date;
  private final Hiking hiking;
  private final Money cost;
  private List<Group> groups = new ArrayList<>();
  private TransportCollection transportCollection;
  private int availableSeats;
  private HostingReservation hostingReservation;
  private boolean hasReservation;
  private TripStatus status;

  public Trip(
      TripID id,
      int maximumSeats,
      LocalDate date,
      Hiking hiking,
      Money cost,
      TripStatus status,
      TransportCollection transportCollection) {
    this.id = id;
    this.maximumSeats = maximumSeats;
    this.date = date;
    this.hiking = hiking;
    this.availableSeats = maximumSeats;
    this.cost = cost;
    this.hostingReservation = null;
    this.hasReservation = false;
    this.status = status;
    this.transportCollection = transportCollection;
  }

  public Money getCost() {
    return this.cost;
  }

  public TripID getId() {
    return this.id;
  }

  public Hiking getHiking() {
    return this.hiking;
  }

  public LocalDate getDate() {
    return this.date;
  }

  public boolean isBeforeDate(LocalDate dateToCompare) {
    return this.date.isBefore(dateToCompare);
  }

  public HikingRegion getRegion() {
    return this.hiking.getRegion();
  }

  public HostingReservation getHostingReservation() {
    return this.hostingReservation;
  }

  public boolean isBetweenDates(LocalDate start, LocalDate end) {
    return this.date.isAfter(start) && this.date.isBefore(end);
  }

  public TripStatus getStatus() {
    return this.status;
  }

  public boolean hasStatus(TripStatus status) {
    return this.status.equals(status);
  }

  public Trip copy() {
    Trip copied =
        new Trip(
            this.id,
            this.maximumSeats,
            this.date,
            this.hiking,
            this.cost,
            this.status,
            this.transportCollection);
    copied.availableSeats = this.availableSeats;
    if (this.hasReservation) {
      copied.hostingReservation = this.hostingReservation.copy();
    }
    copied.hasReservation = this.hasReservation;
    copied.groups.addAll(this.groups);
    return copied;
  }

  public List<ReservableTransport> getReservableTransports(TransportType transportType) {
    return this.transportCollection.getTransportsOfType(transportType);
  }

  public void blockTrip() {
    this.status = BLOCKED;
  }

  @Override
  public boolean equals(Object o) {
    boolean equal = false;

    if (o == this) {
      equal = true;
    } else if (o instanceof Trip otherTrip) {
      equal = this.id.equals(otherTrip.id);
    }

    return equal;
  }

  @Override
  public int hashCode() {
    return this.id.hashCode();
  }

  public void reserveSeat(Reservation reservation) {
    Group group = reservation.getGroup();
    this.validateSeatsCanBeReserved(group);
    this.groups.add(group);
    this.availableSeats -= group.calculateTotalTravelers();
    this.transportCollection.reserve(reservation);
    if (this.availableSeats == 0) {
      this.status = FULL;
    }
  }

  public int getAvailableSeats() {
    return this.availableSeats;
  }

  public int getReservedSeats() {
    return this.maximumSeats - this.availableSeats;
  }

  public List<TravelerID> retrieveReservedUsers() {
    return this.groups.stream().map(Group::getUserInCharge).toList();
  }

  public List<Group> retrieveGroups() {
    return this.groups;
  }

  public Collection<Guest> retrieveAllGuests() {
    List<Guest> guests = new ArrayList<>();

    for (Group group : this.groups) {
      guests.addAll(group.getGuests());
    }

    return guests;
  }

  public List<Guest> retrieveAllGuestsOfTraveler(TravelerID travelerInCharge) {
    List<Guest> guests = new ArrayList<>();
    for (Group group : this.groups) {
      if (group.getUserInCharge().equals(travelerInCharge)) {
        List<Guest> guestsForGroup = group.getGuests();
        guests.addAll(guestsForGroup);
      }
    }
    return guests;
  }

  public void releaseSeat(Reservation reservation) {
    Group group = reservation.getGroup();
    if (this.groups.remove(group)) {
      this.availableSeats += group.calculateTotalTravelers();
      this.transportCollection.cancelSeat(reservation);
      if (FULL.equals(this.status)) {
        this.status = AVAILABLE;
      }
    }
  }

  public boolean isBlocked() {
    return BLOCKED.equals(this.status);
  }

  public HostingReservation reserveHosting(
      Hosting hosting, TripTravelerFetcher tripTravelerFetcher) {
    if (this.hasReservation) {
      throw new HostingReservationExistingForTripException();
    }
    List<User> users = tripTravelerFetcher.fetchUsers(this);
    users.forEach(
        user -> user.saveItemForTrip(this.id, BillableItems.HOSTING, hosting.getMinimalCost()));
    this.hostingReservation = hosting.reserveHost(this.getReservedSeats());
    this.hasReservation = true;

    return this.hostingReservation;
  }

  public boolean hasHostingReservation() {
    return this.hasReservation;
  }

  public boolean hasTraveler(TravelerID travelerID) {
    return this.groups.stream().anyMatch(group -> group.hasTraveler(travelerID));
  }

  public Optional<Group> findFirstGroupOfTraveler(TravelerID travelerId) {
    return this.groups.stream().filter(group -> group.hasTraveler(travelerId)).findFirst();
  }

  public Optional<Guest> retrieveGuest(TravelerID travelerId) {
    Optional<Group> group = this.findFirstGroupOfTraveler(travelerId);
    if (group.isPresent()) {
      return group.get().getGuest(travelerId);
    }
    return Optional.empty();
  }

  public List<TransportOption> readCurrentTransportOptions() {
    return this.transportCollection.readCurrentTransportOptions();
  }

  private boolean isGroupSharingTravelersWithOthers(Group group) {
    return this.groups.stream().anyMatch(savedGroup -> savedGroup.isSharingTravelers(group));
  }

  private void validateSeatsCanBeReserved(Group group) {
    if (this.status != AVAILABLE) {
      throw new TripNotAvailableException(this.status);
    } else if (this.isGroupSharingTravelersWithOthers(group)) {
      throw new TripAlreadyContainsTravelerException();
    } else if (this.availableSeats < group.calculateTotalTravelers()) {
      throw new TripNotEnoughSeatsException(this.availableSeats);
    }
  }
}
