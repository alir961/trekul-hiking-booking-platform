package ca.ulaval.glo4003.hiking;

import java.io.IOException;
import java.io.Reader;
import java.util.Collection;

public interface HikingRegionParser {
  Collection<HikingRegion> parse(Reader content) throws IOException;

  String getFileExtension();
}
