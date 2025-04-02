package ca.ulaval.glo4003.traveler.guest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import ca.ulaval.glo4003.traveler.PersonName;
import ca.ulaval.glo4003.traveler.TravelerID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GuestTest {
  private static final String ANY_GUEST_FIRSTNAME = "Alice";
  private static final String ANY_GUEST_LASTNAME = "Alice";
  private static final PersonName ANY_GUEST_FULLNAME =
      new PersonName(ANY_GUEST_FIRSTNAME, ANY_GUEST_LASTNAME);
  private static final String ANY_GUEST_EMAIL = "email@ulaval.ca";
  private static final TravelerID ANY_GUEST_ID = new TravelerID("111222333");
  private static final TravelerID ANY_OTHER_GUEST_ID = new TravelerID("555555555");
  private Guest anyGuest;

  @BeforeEach
  void setUp() {
    this.anyGuest = new Guest(ANY_GUEST_FULLNAME, ANY_GUEST_ID, ANY_GUEST_EMAIL);
  }

  @Test
  void givenSameObject_whenCallingEquals_thenObjectIsEqual() {
    assertEquals(this.anyGuest, this.anyGuest);
  }

  @Test
  void givenAnotherGuestWithSameId_whenCallingEquals_thenObjectIsEqual() {
    Guest guestWithSameProperties = new Guest(ANY_GUEST_FULLNAME, ANY_GUEST_ID, ANY_GUEST_EMAIL);
    assertEquals(guestWithSameProperties, this.anyGuest);
  }

  @Test
  void givenAnotherGuestWithDifferentId_whenCallingEquals_thenObjectIsNotEqual() {
    Guest guestWithDifferentId = new Guest(ANY_GUEST_FULLNAME, ANY_OTHER_GUEST_ID, ANY_GUEST_EMAIL);

    assertNotEquals(guestWithDifferentId, this.anyGuest);
  }
}
