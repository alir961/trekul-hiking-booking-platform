package ca.ulaval.glo4003.group.exception;

import ca.ulaval.glo4003.exception.TrekULException;

public class GroupTooBigException extends TrekULException {
  public GroupTooBigException(int sizeLimit) {
    super(
        "GROUP_TOO_BIG",
        String.format("There shouldn't be more than %d people in the group.", sizeLimit));
  }
}
