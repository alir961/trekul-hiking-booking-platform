package ca.ulaval.glo4003.hosting.assembler;

import ca.ulaval.glo4003.hosting.Hosting;
import ca.ulaval.glo4003.hosting.dto.HostingDto;
import java.util.List;

public class HostingDtoAssembler {

  public List<HostingDto> assemble(List<Hosting> hosts) {
    return hosts.stream().map(this::assembleSingle).toList();
  }

  private HostingDto assembleSingle(Hosting hosting) {
    return new HostingDto(
        hosting.getID(),
        hosting.getName(),
        hosting.getRegion(),
        hosting.getPriceRange(),
        hosting.getMaxCapacity(),
        hosting.getAvailableCapacity(),
        hosting.getAvailablePeriod());
  }
}
