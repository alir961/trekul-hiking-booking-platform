package ca.ulaval.glo4003.hosting;

public class HostingID {
  private final Integer value;

  public HostingID(Integer value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    boolean equal = false;

    if (o == this) {
      equal = true;
    } else if (o instanceof HostingID otherID) {
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
