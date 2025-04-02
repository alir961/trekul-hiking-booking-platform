package ca.ulaval.glo4003.hiking;

public class HikingLocation {
  private final HikingRegion region;
  private final String name;

  public HikingLocation(HikingRegion region, String name) {
    this.region = region;
    this.name = name;
  }

  public HikingRegion getRegion() {
    return this.region;
  }
}
