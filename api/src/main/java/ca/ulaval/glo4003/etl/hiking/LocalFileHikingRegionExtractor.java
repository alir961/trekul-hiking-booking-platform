package ca.ulaval.glo4003.etl.hiking;

import ca.ulaval.glo4003.etl.Extractor;
import ca.ulaval.glo4003.etl.exception.ExtractException;
import ca.ulaval.glo4003.hiking.HikingRegion;
import ca.ulaval.glo4003.hiking.HikingRegionParser;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Collection;

public class LocalFileHikingRegionExtractor implements Extractor<HikingRegion> {
  private static final String BASE_FILE_NAME = "supported_regions.";
  private final HikingRegionParser hikingRegionParser;

  public LocalFileHikingRegionExtractor(HikingRegionParser hikingRegionParser) {
    this.hikingRegionParser = hikingRegionParser;
  }

  @Override
  public Collection<HikingRegion> extract() throws ExtractException {
    try {
      return this.hikingRegionParser.parse(this.readFile());
    } catch (IOException e) {
      throw new ExtractException(e);
    }
  }

  private Reader readFile() throws FileNotFoundException {
    return new FileReader(BASE_FILE_NAME + this.hikingRegionParser.getFileExtension());
  }
}
