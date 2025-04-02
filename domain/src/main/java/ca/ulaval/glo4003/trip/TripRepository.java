package ca.ulaval.glo4003.trip;

import ca.ulaval.glo4003.traveler.TravelerID;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TripRepository {
  void save(Trip trip);

  Optional<Trip> get(TripID tripID);

  Optional<Trip> getByIdAndTravelerId(TripID tripID, TravelerID travelerID);

  List<Trip> getAll();

  List<Trip> getBunch(Collection<TripID> trips);

  List<Trip> findBetweenDates(LocalDate startDate, LocalDate endDate);
}
