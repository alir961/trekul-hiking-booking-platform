package ca.ulaval.glo4003.guest.assembler;

import ca.ulaval.glo4003.guest.response.GuestResponse;
import ca.ulaval.glo4003.traveler.guest.Guest;
import java.util.List;

public class GuestResponseAssembler {
  public GuestResponse assemble(Guest guest) {
    return new GuestResponse(
        guest.getId().toString(), guest.getName().toString(), guest.getEmail());
  }

  public List<GuestResponse> assemble(List<Guest> guests) {
    return guests.stream().map(this::assemble).toList();
  }
}
