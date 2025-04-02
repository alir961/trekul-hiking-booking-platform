package ca.ulaval.glo4003.group;

import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.guest.Guest;
import java.util.*;

public class Group {
  private final TravelerID userInCharge;
  private final Map<TravelerID, Guest> guests = new HashMap<>();

  public Group(TravelerID userInCharge) {
    this.userInCharge = userInCharge;
  }

  public void addGuest(Guest guest) {
    this.guests.put(guest.getId(), guest);
  }

  public List<Guest> getGuests() {
    return new ArrayList<>(this.guests.values());
  }

  public List<TravelerID> getGuestsIDs() {
    return new ArrayList<>(this.guests.keySet());
  }

  public int getNumberOfGuests() {
    return this.guests.size();
  }

  public TravelerID getUserInCharge() {
    return this.userInCharge;
  }

  public int calculateTotalTravelers() {
    return this.guests.size() + 1;
  }

  public boolean hasTraveler(TravelerID travelerID) {
    return this.userInCharge.equals(travelerID) || this.guests.containsKey(travelerID);
  }

  public Optional<Guest> getGuest(TravelerID travelerID) {
    if (this.guests.containsKey(travelerID)) {
      return Optional.of(this.guests.get(travelerID));
    }

    return Optional.empty();
  }

  public boolean isSharingTravelers(Group group) {
    Set<TravelerID> thisTravelers = this.gatherAllTravelerIDs();
    Set<TravelerID> otherGroupTravelers = group.gatherAllTravelerIDs();

    return !Collections.disjoint(thisTravelers, otherGroupTravelers);
  }

  private Set<TravelerID> gatherAllTravelerIDs() {
    Set<TravelerID> travelerIDs = new HashSet<>(this.guests.keySet());
    travelerIDs.add(this.userInCharge);

    return travelerIDs;
  }
}
