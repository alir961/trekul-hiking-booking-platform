package ca.ulaval.glo4003.group.assembler;

import ca.ulaval.glo4003.group.Group;
import ca.ulaval.glo4003.group.reponse.GroupResponse;
import ca.ulaval.glo4003.guest.assembler.GuestResponseAssembler;
import jakarta.inject.Inject;

public class GroupResponseAssembler {
  private final GuestResponseAssembler guestResponseAssembler;

  @Inject
  public GroupResponseAssembler(GuestResponseAssembler guestResponseAssembler) {
    this.guestResponseAssembler = guestResponseAssembler;
  }

  public GroupResponse assemble(Group group) {
    return new GroupResponse(this.guestResponseAssembler.assemble(group.getGuests()));
  }
}
