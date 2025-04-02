package ca.ulaval.glo4003.invoice;

import java.util.UUID;

public class InvoiceID {
  private final UUID value;

  public InvoiceID(String value) {
    this.value = UUID.fromString(value);
  }

  private InvoiceID(UUID value) {
    this.value = value;
  }

  public static InvoiceID generate() {
    return new InvoiceID(UUID.randomUUID());
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
    final InvoiceID other = (InvoiceID) object;
    return this.value.equals(other.value);
  }

  @Override
  public int hashCode() {
    return this.value.hashCode();
  }
}
