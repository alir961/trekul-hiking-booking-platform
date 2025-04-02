package ca.ulaval.glo4003.transport.response;

public record TransportOptionResponse(
    String transportType,
    String description,
    boolean isAvailable,
    int optionsQuantity,
    String minimumCost) {}
