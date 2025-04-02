package ca.ulaval.glo4003.transport.assembler;

import ca.ulaval.glo4003.transport.dto.ReservableTransportStopDto;
import ca.ulaval.glo4003.transport.response.ReservableTransportStopResponse;
import java.util.List;

public class ReservableTransportStopResponseAssembler {
  public List<ReservableTransportStopResponse> assemble(List<ReservableTransportStopDto> stops) {
    return stops.stream().map(this::assemble).toList();
  }

  private ReservableTransportStopResponse assemble(ReservableTransportStopDto stop) {
    return new ReservableTransportStopResponse(stop.location(), stop.departureTime());
  }
}
