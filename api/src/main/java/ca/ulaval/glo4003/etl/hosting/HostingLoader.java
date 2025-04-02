package ca.ulaval.glo4003.etl.hosting;

import ca.ulaval.glo4003.hosting.Hosting;
import ca.ulaval.glo4003.hosting.HostingRepository;
import java.util.Collection;

public class HostingLoader {
  private final HostingRepository hostingRepository;

  public HostingLoader(HostingRepository hostingRepository) {
    this.hostingRepository = hostingRepository;
  }

  public void load(Collection<Hosting> hostings) {
    hostings.forEach(this.hostingRepository::save);
  }
}
