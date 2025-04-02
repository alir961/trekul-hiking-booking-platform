package ca.ulaval.glo4003.trip;

import java.util.UUID;

public class TripID {
  private final UUID value;

  public TripID(String value) {
    this.value = UUID.fromString(value);
  }

  private TripID(UUID value) {
    this.value = value;
  }

  public static TripID generate() {
    return new TripID(UUID.randomUUID());
  }

  @Override
  public boolean equals(Object o) {
    boolean equal = false;

    if (o == this) {
      equal = true;
    } else if (o instanceof TripID otherID) {
      equal = this.value.equals(otherID.value);
    }

    return equal;
  }

  @Override
  public String toString() {
    return this.value.toString();
  }

  @Override
  public int hashCode() {
    return this.value.hashCode();
  }
}
