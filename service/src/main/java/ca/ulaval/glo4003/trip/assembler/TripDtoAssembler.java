package ca.ulaval.glo4003.trip.assembler;

import ca.ulaval.glo4003.trip.Trip;
import ca.ulaval.glo4003.trip.dto.TripDto;
import java.util.List;

public class TripDtoAssembler {
  public List<TripDto> assemble(List<Trip> trips) {
    return trips.stream().map(this::assembleSingle).toList();
  }

  public TripDto assembleSingle(Trip trip) {
    return new TripDto(
        trip.getHiking(),
        trip.getDate(),
        trip.getAvailableSeats(),
        trip.getId(),
        trip.getCost(),
        trip.getStatus(),
        trip.getHostingReservation());
  }
}
