package ca.ulaval.glo4003.traveler;

public class TravelerID {
  private final String value;

  public TravelerID(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }

  @Override
  public boolean equals(Object object) {
    if (object == null) {
      return false;
    }
    if (object.getClass() != this.getClass()) {
      return false;
    }
    final TravelerID other = (TravelerID) object;
    return this.value.equals(other.value);
  }

  @Override
  public String toString() {
    return this.value;
  }

  @Override
  public int hashCode() {
    return this.value.hashCode();
  }
}
