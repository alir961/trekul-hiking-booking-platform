package ca.ulaval.glo4003.etl.transport;

import ca.ulaval.glo4003.etl.Extractor;
import ca.ulaval.glo4003.etl.exception.ExtractException;
import ca.ulaval.glo4003.transport.organizedbus.OrganizedBusStopParser;
import ca.ulaval.glo4003.transport.organizedbus.TransportStop;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Collection;

public class LocalFileOrganizedBusStopExtractor implements Extractor<TransportStop> {
  private static final String BASE_FILE_NAME = "campus-departure-location.";
  private final OrganizedBusStopParser organizedBusStopParser;

  public LocalFileOrganizedBusStopExtractor(OrganizedBusStopParser organizedBusStopParser) {
    this.organizedBusStopParser = organizedBusStopParser;
  }

  @Override
  public Collection<TransportStop> extract() throws ExtractException {
    try {
      return this.organizedBusStopParser.parse(this.readFile());
    } catch (IOException e) {
      throw new ExtractException(e);
    }
  }

  private Reader readFile() throws FileNotFoundException {
    return new FileReader(BASE_FILE_NAME + this.organizedBusStopParser.getFileExtension());
  }
}
