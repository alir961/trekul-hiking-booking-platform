package ca.ulaval.glo4003.hiking;

import ca.ulaval.glo4003.distance.Distance;
import java.util.Objects;

public class Hiking {
  private final HikingLocation location;
  private final HikingDifficulty difficulty;
  private final Distance distance;

  public Hiking(HikingLocation location, HikingDifficulty difficulty, Distance distance) {
    this.location = location;
    this.difficulty = difficulty;
    this.distance = distance;
  }

  public HikingRegion getRegion() {
    return this.location.getRegion();
  }

  public HikingDifficulty getDifficulty() {
    return this.difficulty;
  }

  public Distance getDistance() {
    return this.distance;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.location.getRegion(), this.difficulty, this.distance);
  }

  @Override
  public boolean equals(Object o) {
    boolean equal = false;

    if (o == this) {
      equal = true;
    } else if (o instanceof Hiking otherHiking) {
      equal = this.isSameProperties(otherHiking);
    }

    return equal;
  }

  private boolean isSameProperties(Hiking otherHiking) {
    return this.location.getRegion().equals(otherHiking.getRegion())
        && this.difficulty.equals(otherHiking.difficulty)
        && this.distance.equals(otherHiking.distance);
  }
}
