package ca.ulaval.glo4003.transport.assembler;

import ca.ulaval.glo4003.transport.TransportOption;
import ca.ulaval.glo4003.transport.response.TransportOptionResponse;
import java.util.Collection;
import java.util.List;

public class TransportOptionResponseAssembler {
  public List<TransportOptionResponse> assemble(Collection<TransportOption> options) {
    return options.stream().map(this::assemble).toList();
  }

  private TransportOptionResponse assemble(TransportOption option) {
    return new TransportOptionResponse(
        option.transportType().toString(),
        option.transportType().getDescription(),
        option.isAvailable(),
        option.optionsQuantity(),
        option.minimumPrice().toString());
  }
}
