package ca.ulaval.glo4003.etl.hosting;

import ca.ulaval.glo4003.etl.Extractor;
import ca.ulaval.glo4003.etl.exception.ExtractException;
import ca.ulaval.glo4003.hosting.Hosting;
import ca.ulaval.glo4003.hosting.HostingParser;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Collection;

public class LocalFileHostingExtractor implements Extractor<Hosting> {
  private static final String BASE_FILE_NAME = "hebergement.";
  private final HostingParser hostingParser;

  public LocalFileHostingExtractor(HostingParser hostingParser) {
    this.hostingParser = hostingParser;
  }

  @Override
  public Collection<Hosting> extract() throws ExtractException {
    try {
      return this.hostingParser.parse(this.readFile());
    } catch (IOException e) {
      throw new ExtractException(e);
    }
  }

  private Reader readFile() throws FileNotFoundException {
    return new FileReader(BASE_FILE_NAME + this.hostingParser.getFileExtension());
  }
}
