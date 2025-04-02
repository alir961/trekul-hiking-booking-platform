package ca.ulaval.glo4003.transport;

import java.util.UUID;

public class TransportID {
  private final UUID value;

  public TransportID(String value) {
    this.value = UUID.fromString(value);
  }

  private TransportID(UUID value) {
    this.value = value;
  }

  public static TransportID generate() {
    return new TransportID(UUID.randomUUID());
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
    final TransportID other = (TransportID) object;
    return this.value.equals(other.value);
  }

  @Override
  public int hashCode() {
    return this.value.hashCode();
  }
}
