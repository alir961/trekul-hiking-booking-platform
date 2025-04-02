package ca.ulaval.glo4003.reservation.dto;

import ca.ulaval.glo4003.transport.TransportType;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.guest.dto.GuestDto;
import ca.ulaval.glo4003.trip.TripID;
import java.util.List;

public record ReservationDto(
    TravelerID userID,
    TripID tripID,
    TransportType transportType,
    String stop,
    List<GuestDto> guests) {}
