package ca.ulaval.glo4003.hosting.assembler;

import ca.ulaval.glo4003.hosting.dto.HostingDto;
import ca.ulaval.glo4003.hosting.response.SearchHostingResponse;
import java.util.List;

public class SearchHostingResponseAssembler {

  public List<SearchHostingResponse> assemble(List<HostingDto> hosts) {
    return hosts.stream().map(this::assemble).toList();
  }

  private SearchHostingResponse assemble(HostingDto hostingDto) {
    return new SearchHostingResponse(
        hostingDto.id().toString(),
        hostingDto.name(),
        hostingDto.region().toString(),
        hostingDto.availableCapacity(),
        hostingDto.priceRange().toString(),
        hostingDto.availabilityPeriod().toString());
  }
}
