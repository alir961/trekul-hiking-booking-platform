package ca.ulaval.glo4003.hiking;

import static ca.ulaval.glo4003.hiking.HikingRegion.SAGLAC;

import ca.ulaval.glo4003.filereader.xml.XmlContent;
import ca.ulaval.glo4003.hiking.exception.HikingRegionUnkown;

public class HikingRegionXmlExtractor {
  private static final String LANDS_TAG = "TERRITOIRES";
  private static final String LAND_DETAILS_TAG = "TERRITOIRE";
  private static final String ZONES_TAG = "TERR_ZONES";
  private static final String ZONE_DETAILS_TAG = "TERR_ZONE";
  private static final String ZONE_DETAIL_TYPE_TAG = "TERR_ZONE_GENRE_EN";
  private static final String REGION_GENRE = "Area";
  private static final String REGION_NAME_TAG = "TERR_ZONE_NOM_EN";

  public HikingRegion extract(XmlContent content) {
    XmlContent regionContent = this.extractRegion(content);
    return this.resolveHikingRegion(regionContent.asString());
  }

  private XmlContent extractRegion(XmlContent content) throws HikingRegionUnkown {
    return content
        .extractField(LANDS_TAG)
        .extractField(LAND_DETAILS_TAG)
        .extractField(ZONES_TAG)
        .extractFirstSectionContaining(ZONE_DETAILS_TAG, ZONE_DETAIL_TYPE_TAG, REGION_GENRE)
        .extractField(REGION_NAME_TAG);
  }

  private HikingRegion resolveHikingRegion(String region) throws HikingRegionUnkown {
    String regionName = this.normalizeRegionName(region);
    if ("SAGUENAY_LAC_SAINT_JEAN".equalsIgnoreCase(regionName)) {
      return SAGLAC;
    }
    try {
      return HikingRegion.valueOf(regionName);
    } catch (IllegalArgumentException e) {
      throw new HikingRegionUnkown(regionName);
    }
  }

  private String normalizeRegionName(String regionName) {
    return regionName.replace("-", "_").replace(" ", "_").replace("é", "e").toUpperCase();
  }
}
