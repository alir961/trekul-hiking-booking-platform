package ca.ulaval.glo4003.hosting.dto;

import ca.ulaval.glo4003.hiking.HikingRegion;
import ca.ulaval.glo4003.hosting.HostingID;
import ca.ulaval.glo4003.money.Money;
import java.time.LocalDate;
import java.util.Map;

public record HostingDto(
    HostingID id,
    String name,
    HikingRegion region,
    Map<Money, Money> priceRange,
    int maxCapacity,
    int availableCapacity,
    Map<LocalDate, LocalDate> availabilityPeriod) {}
