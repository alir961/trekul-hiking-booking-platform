package ca.ulaval.glo4003.distance;

public class Distance {
  private static final String UNIT = "km";
  private final String value;

  public Distance(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return this.value + UNIT;
  }

  @Override
  public int hashCode() {
    return this.value.hashCode();
  }

  @Override
  public boolean equals(Object o) {
    boolean equal = false;

    if (o == this) {
      equal = true;
    } else if (o instanceof Distance otherDistance) {
      equal = this.value.equals(otherDistance.value);
    }

    return equal;
  }
}
