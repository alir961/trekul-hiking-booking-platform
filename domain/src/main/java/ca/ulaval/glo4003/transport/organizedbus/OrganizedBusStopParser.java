package ca.ulaval.glo4003.transport.organizedbus;

import java.io.IOException;
import java.io.Reader;
import java.util.Collection;

public interface OrganizedBusStopParser {
  Collection<TransportStop> parse(Reader content) throws IOException;

  String getFileExtension();
}
