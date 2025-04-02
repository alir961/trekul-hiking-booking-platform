package ca.ulaval.glo4003.group;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.guest.Guest;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GroupTest {
  private static final TravelerID ANY_USER_ID = new TravelerID("111222333");
  private static final TravelerID ANY_OTHER_USER_ID = new TravelerID("6023r21");
  private Guest firstGuestMock;
  private Group anyGroup;
  private Group anyOtherGroup;

  @BeforeEach
  void setUp() {
    this.firstGuestMock = mock(Guest.class);
    this.anyGroup = new Group(ANY_USER_ID);
    this.anyOtherGroup = new Group(ANY_OTHER_USER_ID);
  }

  @Test
  void whenAddingGuest_thenGuestIsAdded() {
    this.anyGroup.addGuest(this.firstGuestMock);

    Collection<Guest> guestsInGroup = this.anyGroup.getGuests();
    assertTrue(guestsInGroup.contains(this.firstGuestMock));
  }

  @Test
  void givenEmptyGroupsOfDifferentUserInCharge_whenIsSharingTravelers_thenReturnFalse() {
    boolean result = this.anyGroup.isSharingTravelers(this.anyOtherGroup);

    assertFalse(result);
  }

  @Test
  void givenEmptyGroupsOfSameUserInCharge_whenIsSharingTravelers_thenReturnTrue() {
    boolean result = this.anyGroup.isSharingTravelers(this.anyGroup);

    assertTrue(result);
  }

  @Test
  void givenNonEmptyGroupsWithNoGuestShared_whenIsSharingTravelers_thenReturnFalse() {
    this.givenNonEmptyGroups();
    boolean result = this.anyGroup.isSharingTravelers(this.anyOtherGroup);

    assertFalse(result);
  }

  @Test
  void givenNonEmptyGroupsWithOneGuestShared_whenIsSharingTravelers_thenReturnTrue() {
    Guest sameGuest = this.mockGuestWithID("any shared guest");
    this.givenNonEmptyGroups();
    this.anyOtherGroup.addGuest(sameGuest);
    this.anyGroup.addGuest(sameGuest);
    boolean result = this.anyGroup.isSharingTravelers(this.anyOtherGroup);

    assertTrue(result);
  }

  private void givenNonEmptyGroups() {
    this.anyOtherGroup.addGuest(this.mockGuestWithID("any guest 1"));
    this.anyOtherGroup.addGuest(this.mockGuestWithID("any guest 2"));
    this.anyGroup.addGuest(this.mockGuestWithID("any guest 3"));
    this.anyGroup.addGuest(this.mockGuestWithID("any guest 4"));
    this.anyGroup.addGuest(this.mockGuestWithID("any guest 5"));
  }

  private Guest mockGuestWithID(String anyTravelerIDasString) {
    TravelerID anyTravelerID = new TravelerID(anyTravelerIDasString);
    Guest guest = mock(Guest.class);
    when(guest.getId()).thenReturn(anyTravelerID);

    return guest;
  }
}
