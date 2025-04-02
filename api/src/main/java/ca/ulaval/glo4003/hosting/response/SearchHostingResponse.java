package ca.ulaval.glo4003.hosting.response;

public record SearchHostingResponse(
    String hostingID,
    String hostingName,
    String region,
    int availableCapacity,
    String priceRange,
    String availabilityPeriod) {}
