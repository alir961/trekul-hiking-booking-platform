package ca.ulaval.glo4003.reservation;

import ca.ulaval.glo4003.money.Money;
import java.time.Duration;

public record ReservationConfig(Money reservationCost, Duration reservationExpiration) {}
