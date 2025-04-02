package ca.ulaval.glo4003.hosting;

import java.util.List;
import java.util.Optional;

public interface HostingRepository {

  void save(Hosting hosting);

  Optional<Hosting> get(HostingID hostingId);

  List<Hosting> getAll();
}
