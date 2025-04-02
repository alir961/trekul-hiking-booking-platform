package ca.ulaval.glo4003.group.exception;

import ca.ulaval.glo4003.exception.TrekULNotFoundException;

public class GroupNotFoundException extends TrekULNotFoundException {
  public GroupNotFoundException() {
    super("GROUP_NOT_FOUND", "Group not found");
  }
}
