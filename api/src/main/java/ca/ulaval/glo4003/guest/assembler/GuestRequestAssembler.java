package ca.ulaval.glo4003.guest.assembler;

import ca.ulaval.glo4003.guest.request.GuestRequest;
import ca.ulaval.glo4003.traveler.guest.dto.GuestDto;
import java.util.ArrayList;
import java.util.List;

public class GuestRequestAssembler {

  public List<GuestDto> assemble(List<GuestRequest> guestRequests) {
    List<GuestDto> guestDtos = new ArrayList<>();
    for (GuestRequest guestRequest : guestRequests) {
      GuestDto guestDto =
          new GuestDto(
              guestRequest.firstname(),
              guestRequest.lastname(),
              guestRequest.idul(),
              guestRequest.email());
      guestDtos.add(guestDto);
    }
    return guestDtos;
  }
}
