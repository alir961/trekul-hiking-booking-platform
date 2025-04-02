package ca.ulaval.glo4003.hiking.exception;

import ca.ulaval.glo4003.exception.TrekULException;

public class HikingRegionUnkown extends TrekULException {

  public HikingRegionUnkown(String regionName) {
    super("HIKING_REGION_UNKNOWN", "Unknown region: " + regionName);
  }
}
