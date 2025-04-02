package ca.ulaval.glo4003.trip;

import ca.ulaval.glo4003.traveler.TravelerID;
import java.time.LocalDate;
import java.util.*;

public class InMemoryTripRepositoryImpl implements TripRepository {
  private final Map<TripID, Trip> allTrips = new HashMap<>();

  @Override
  public void save(Trip trip) {
    this.allTrips.put(trip.getId(), trip.copy());
  }

  @Override
  public Optional<Trip> get(TripID tripID) {
    Trip found = this.allTrips.get(tripID);

    if (found != null) {
      return Optional.of(found.copy());
    }

    return Optional.empty();
  }

  @Override
  public Optional<Trip> getByIdAndTravelerId(TripID tripID, TravelerID travelerID) {
    Trip found = this.allTrips.get(tripID);

    if (found != null && found.hasTraveler(travelerID)) {
      return Optional.of(found.copy());
    }

    return Optional.empty();
  }

  @Override
  public List<Trip> getAll() {
    return this.allTrips.values().stream().map(Trip::copy).toList();
  }

  @Override
  public List<Trip> getBunch(Collection<TripID> trips) {
    return trips.stream()
        .filter(this.allTrips::containsKey)
        .map(this.allTrips::get)
        .map(Trip::copy)
        .toList();
  }

  @Override
  public List<Trip> findBetweenDates(LocalDate startDate, LocalDate endDate) {
    return this.allTrips.values().stream()
        .filter(trip -> trip.isBetweenDates(startDate, endDate))
        .map(Trip::copy)
        .toList();
  }
}
