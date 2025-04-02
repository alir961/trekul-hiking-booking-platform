package ca.ulaval.glo4003.guest;

import ca.ulaval.glo4003.traveler.PersonName;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.guest.Guest;
import ca.ulaval.glo4003.traveler.guest.dto.GuestDto;
import ca.ulaval.glo4003.traveler.user.credential.EmailValidator;

public class GuestFactory {
  private final EmailValidator emailValidator;

  public GuestFactory(EmailValidator emailValidator) {
    this.emailValidator = emailValidator;
  }

  public Guest createGuest(GuestDto guestDto) {
    this.emailValidator.validate(guestDto.email());
    TravelerID guestID = new TravelerID(guestDto.idul());
    PersonName guestName = new PersonName(guestDto.firstname(), guestDto.lastname());
    return new Guest(guestName, guestID, guestDto.email());
  }
}
