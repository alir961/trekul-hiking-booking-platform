package ca.ulaval.glo4003.group;

public class GroupConfig {
  private int maximumGroupSize;

  public GroupConfig(int maximumGroupSize) {
    this.maximumGroupSize = maximumGroupSize;
  }

  public int getMaximumGroupSize() {
    return this.maximumGroupSize;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    } else if (o instanceof GroupConfig otherConfig) {
      return this.maximumGroupSize == otherConfig.maximumGroupSize;
    }

    return false;
  }
}
