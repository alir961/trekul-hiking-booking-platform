package ca.ulaval.glo4003.hosting;

import java.io.Reader;
import java.util.List;

public interface HostingParser {

  List<Hosting> parse(Reader reader);

  String getFileExtension();
}
