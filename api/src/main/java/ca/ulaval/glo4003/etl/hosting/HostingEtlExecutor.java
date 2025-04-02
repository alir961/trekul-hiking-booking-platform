package ca.ulaval.glo4003.etl.hosting;

import ca.ulaval.glo4003.etl.EtlExecutor;
import ca.ulaval.glo4003.etl.Extractor;
import ca.ulaval.glo4003.etl.exception.ExtractException;
import ca.ulaval.glo4003.hosting.Hosting;

public class HostingEtlExecutor implements EtlExecutor {
  private final Extractor<Hosting> hostingExtractor;
  private final HostingLoader hostingLoader;

  public HostingEtlExecutor(Extractor<Hosting> hostingExtractor, HostingLoader hostingLoader) {
    this.hostingExtractor = hostingExtractor;
    this.hostingLoader = hostingLoader;
  }

  public void execute() throws ExtractException {
    this.hostingLoader.load(this.hostingExtractor.extract());
  }
}
