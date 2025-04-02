package ca.ulaval.glo4003.reservation.assembler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ca.ulaval.glo4003.guest.assembler.GuestResponseAssembler;
import ca.ulaval.glo4003.guest.response.GuestResponse;
import ca.ulaval.glo4003.traveler.PersonName;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.guest.Guest;
import org.junit.jupiter.api.Test;

class GuestResponseAssemblerTest {
  private static final String ANY_IDUL = "111222333";
  private static final TravelerID ANY_ID = new TravelerID(ANY_IDUL);
  private static final String ANY_FIRSTNAME = "Name";
  private static final String ANY_LASTNAME = "LAst";
  private static final PersonName ANY_FULLNAME = new PersonName(ANY_FIRSTNAME, ANY_LASTNAME);
  private static final String ANY_EMAIL = "email@ulaval.ca";
  private static final Guest ANY_GUEST = new Guest(ANY_FULLNAME, ANY_ID, ANY_EMAIL);
  private final GuestResponseAssembler guestResponseAssembler = new GuestResponseAssembler();

  @Test
  void whenAssemble_thenGuestResponseIsAssembledCorrectly() {
    GuestResponse response = this.guestResponseAssembler.assemble(ANY_GUEST);

    assertEquals(ANY_ID.toString(), response.id());
    assertEquals(ANY_FULLNAME.toString(), response.name());
    assertEquals(ANY_EMAIL, response.email());
  }
}
