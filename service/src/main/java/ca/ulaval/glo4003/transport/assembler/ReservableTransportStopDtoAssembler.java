package ca.ulaval.glo4003.transport.assembler;

import ca.ulaval.glo4003.transport.dto.ReservableTransportStopDto;
import ca.ulaval.glo4003.transport.organizedbus.TransportStop;
import java.util.Collection;
import java.util.List;

public class ReservableTransportStopDtoAssembler {
  public List<ReservableTransportStopDto> assemble(Collection<TransportStop> stops) {
    return stops.stream().map(this::assemble).toList();
  }

  private ReservableTransportStopDto assemble(TransportStop stop) {
    return new ReservableTransportStopDto(stop.getLocationName(), stop.getDepartureTime());
  }
}
