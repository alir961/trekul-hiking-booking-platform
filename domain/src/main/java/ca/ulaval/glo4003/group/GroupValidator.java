package ca.ulaval.glo4003.group;

import ca.ulaval.glo4003.group.exception.GroupTooBigException;

public class GroupValidator {
  private final int maximumGroupSize;

  public GroupValidator(GroupConfig groupConfig) {
    this.maximumGroupSize = groupConfig.getMaximumGroupSize();
  }

  public void validate(Group group) {
    if (group.calculateTotalTravelers() > this.maximumGroupSize) {
      throw new GroupTooBigException(this.maximumGroupSize);
    }
  }
}
