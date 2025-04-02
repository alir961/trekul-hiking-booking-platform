package ca.ulaval.glo4003.traveler.guest;

import ca.ulaval.glo4003.traveler.Contactable;
import ca.ulaval.glo4003.traveler.PersonName;
import ca.ulaval.glo4003.traveler.TravelerID;

public class Guest implements Contactable {
  private final PersonName name;
  private final TravelerID id;
  private final String email;

  public Guest(PersonName name, TravelerID id, String email) {
    this.name = name;
    this.id = id;
    this.email = email;
  }

  public PersonName getName() {
    return this.name;
  }

  public TravelerID getId() {
    return this.id;
  }

  @Override
  public String getEmail() {
    return this.email;
  }

  @Override
  public boolean equals(Object o) {
    boolean equal = false;

    if (o == this) {
      equal = true;
    } else if (o instanceof Guest otherGuest) {
      equal = this.id.equals(otherGuest.getId());
    }

    return equal;
  }
}
