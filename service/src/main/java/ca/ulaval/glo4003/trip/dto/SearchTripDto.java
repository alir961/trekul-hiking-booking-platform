package ca.ulaval.glo4003.trip.dto;

import ca.ulaval.glo4003.hiking.HikingRegion;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.trip.TripStatus;
import java.time.LocalDate;
import java.util.Optional;

public record SearchTripDto(
    Money priceLimit,
    LocalDate startDate,
    LocalDate endDate,
    TripStatus status,
    Optional<HikingRegion> region) {}
