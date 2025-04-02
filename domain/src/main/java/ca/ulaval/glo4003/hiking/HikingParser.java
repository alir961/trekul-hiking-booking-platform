package ca.ulaval.glo4003.hiking;

import java.io.Reader;
import java.util.Collection;

public interface HikingParser {
  Collection<Hiking> parse(Reader reader, HikingRegion region);

  String getFileExtension();
}
