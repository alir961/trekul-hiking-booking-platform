package ca.ulaval.glo4003.etl.hiking;

import ca.ulaval.glo4003.etl.Extractor;
import ca.ulaval.glo4003.etl.exception.ExtractException;
import ca.ulaval.glo4003.hiking.Hiking;
import ca.ulaval.glo4003.hiking.HikingParser;
import ca.ulaval.glo4003.hiking.HikingRegion;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collection;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalFileHikingExtractor implements Extractor<Hiking> {
  private static final Logger LOGGER = LoggerFactory.getLogger(LocalFileHikingExtractor.class);
  private final HikingParser hikingParser;
  private final Extractor<HikingRegion> regionExtractor;

  public LocalFileHikingExtractor(
      HikingParser hikingParser, Extractor<HikingRegion> regionExtractor) {
    this.regionExtractor = regionExtractor;
    this.hikingParser = hikingParser;
  }

  @Override
  public Collection<Hiking> extract() throws ExtractException {
    return this.regionExtractor.extract().stream().flatMap(this::extractRegion).toList();
  }

  private Stream<Hiking> extractRegion(HikingRegion region) {
    String filename = String.format("%s_treks.%s", region, this.hikingParser.getFileExtension());
    try {
      return this.hikingParser.parse(new FileReader(filename), region).stream();
    } catch (FileNotFoundException e) {
      LOGGER.warn("The file '{}' was not found", filename);
      return Stream.<Hiking>builder().build();
    }
  }
}
