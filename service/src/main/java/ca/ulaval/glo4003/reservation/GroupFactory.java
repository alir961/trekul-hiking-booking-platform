package ca.ulaval.glo4003.reservation;

import ca.ulaval.glo4003.group.Group;
import ca.ulaval.glo4003.group.GroupValidator;
import ca.ulaval.glo4003.guest.GuestFactory;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.guest.dto.GuestDto;
import java.util.List;

public class GroupFactory {
  private final GroupValidator groupValidator;
  private final GuestFactory guestFactory;

  public GroupFactory(GroupValidator groupValidator, GuestFactory guestFactory) {
    this.groupValidator = groupValidator;
    this.guestFactory = guestFactory;
  }

  public Group create(TravelerID userInCharge, List<GuestDto> guestDtos) {
    Group group = new Group(userInCharge);

    guestDtos.stream().map(this.guestFactory::createGuest).forEach(group::addGuest);

    this.groupValidator.validate(group);

    return group;
  }
}
