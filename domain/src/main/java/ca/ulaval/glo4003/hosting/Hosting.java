package ca.ulaval.glo4003.hosting;

import ca.ulaval.glo4003.hiking.HikingRegion;
import ca.ulaval.glo4003.hosting.exception.HostingNoCapacityAvailableException;
import ca.ulaval.glo4003.hosting.reservation.HostingReservation;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.trip.Trip;
import java.time.LocalDate;
import java.util.Map;

public class Hosting {
  private final HostingID id;
  private final String name;
  private final HikingRegion region;
  private final Map<Money, Money> priceRange;
  private final int maxCapacity;
  private int availableCapacity;
  private final Map<LocalDate, LocalDate> availablePeriod;

  public Hosting(
      HostingID id,
      String name,
      HikingRegion region,
      Map<Money, Money> priceRange,
      int maxCapacity,
      Map<LocalDate, LocalDate> availablePeriod) {
    this.id = id;
    this.name = name;
    this.region = region;
    this.priceRange = priceRange;
    this.maxCapacity = maxCapacity;
    this.availableCapacity = maxCapacity;
    this.availablePeriod = availablePeriod;
  }

  public boolean canBeReservedForDates(LocalDate start, LocalDate end) {
    return this.availablePeriod.entrySet().stream()
        .anyMatch(period -> start.isAfter(period.getKey()) && end.isBefore(period.getValue()));
  }

  public boolean isBetweenPriceRange(Money min, Money max) {
    return this.priceRange.entrySet().stream()
            .anyMatch(entry -> this.isEntryInPriceRange(entry, min, max))
        || this.priceRange.entrySet().stream()
            .anyMatch(entry -> this.isEntryInPriceRange(entry, max, min));
  }

  public boolean isCompatibleWithTrip(Trip trip) {
    return this.canBeReservedForDates(trip.getDate(), trip.getDate())
        && this.region.equals(trip.getRegion())
        && this.availableCapacity >= trip.getReservedSeats();
  }

  public Hosting copy() {
    Hosting copied =
        new Hosting(
            this.id,
            this.name,
            this.region,
            this.priceRange,
            this.maxCapacity,
            this.availablePeriod);
    copied.availableCapacity = this.availableCapacity;

    return copied;
  }

  @Override
  public boolean equals(Object o) {
    boolean equal = false;

    if (o == this) {
      equal = true;
    } else if (o instanceof Hosting otherHosting) {
      equal = this.id.equals(otherHosting.id);
    }

    return equal;
  }

  public HostingReservation reserveHost(int numberOfPlaces) {
    if (this.availableCapacity < numberOfPlaces) {
      throw new HostingNoCapacityAvailableException();
    }
    this.availableCapacity -= numberOfPlaces;
    HostingReservation hostingReservation =
        new HostingReservation(this.id, this.name, this.priceRange);

    return hostingReservation;
  }

  public HostingID getID() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public int getMaxCapacity() {
    return this.maxCapacity;
  }

  public int getAvailableCapacity() {
    return this.availableCapacity;
  }

  public HikingRegion getRegion() {
    return this.region;
  }

  public Map<LocalDate, LocalDate> getAvailablePeriod() {
    return this.availablePeriod;
  }

  public Map<Money, Money> getPriceRange() {
    return this.priceRange;
  }

  public Money getMinimalCost() {
    return this.priceRange.keySet().iterator().next();
  }

  private boolean isEntryInPriceRange(Map.Entry<Money, Money> entry, Money min, Money max) {
    return min.isLowerOrEqualThan(entry.getValue()) && max.isGreaterOrEqualThan(entry.getKey());
  }
}
