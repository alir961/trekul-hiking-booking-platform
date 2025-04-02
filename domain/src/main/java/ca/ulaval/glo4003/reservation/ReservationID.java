package ca.ulaval.glo4003.reservation;

import java.util.UUID;

public class ReservationID {
  private final UUID value;

  private ReservationID(UUID value) {
    this.value = value;
  }

  public ReservationID(String value) {
    this.value = UUID.fromString(value);
  }

  public static ReservationID generate() {
    return new ReservationID(UUID.randomUUID());
  }

  @Override
  public String toString() {
    return this.value.toString();
  }

  @Override
  public boolean equals(Object object) {
    if (object == null) {
      return false;
    }
    if (object.getClass() != this.getClass()) {
      return false;
    }
    final ReservationID other = (ReservationID) object;
    return this.value.equals(other.value);
  }

  @Override
  public int hashCode() {
    return this.value.hashCode();
  }
}
