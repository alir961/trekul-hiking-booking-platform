package ca.ulaval.glo4003.transport;

import ca.ulaval.glo4003.money.Money;

public record TransportOption(
    TransportType transportType, boolean isAvailable, int optionsQuantity, Money minimumPrice) {}
