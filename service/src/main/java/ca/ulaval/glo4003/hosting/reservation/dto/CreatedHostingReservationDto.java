package ca.ulaval.glo4003.hosting.reservation.dto;

import ca.ulaval.glo4003.hosting.HostingID;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.trip.TripID;
import java.util.Map;

public record CreatedHostingReservationDto(
    HostingID hostingID, String hostingName, TripID tripID, Map<Money, Money> priceRange) {}
