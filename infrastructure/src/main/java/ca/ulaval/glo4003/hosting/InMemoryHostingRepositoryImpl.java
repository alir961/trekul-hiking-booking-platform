package ca.ulaval.glo4003.hosting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryHostingRepositoryImpl implements HostingRepository {
  private final Map<HostingID, Hosting> allHosts = new HashMap<>();

  @Override
  public void save(Hosting hosting) {
    this.allHosts.put(hosting.getID(), hosting.copy());
  }

  @Override
  public Optional<Hosting> get(HostingID hostingId) {
    Hosting found = this.allHosts.get(hostingId);

    if (found != null) {
      return Optional.of(found.copy());
    }

    return Optional.empty();
  }

  @Override
  public List<Hosting> getAll() {
    return this.allHosts.values().stream().map(Hosting::copy).toList();
  }
}
