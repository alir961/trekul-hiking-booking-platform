package ca.ulaval.glo4003.traveler;

public record PersonName(String firstname, String lastname) {
  @Override
  public String toString() {
    return String.format("%s %s", this.firstname, this.lastname);
  }
}
