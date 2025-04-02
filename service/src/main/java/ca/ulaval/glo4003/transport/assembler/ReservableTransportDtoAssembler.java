package ca.ulaval.glo4003.transport.assembler;

import ca.ulaval.glo4003.transport.ReservableTransport;
import ca.ulaval.glo4003.transport.dto.ReservableTransportDto;
import java.util.ArrayList;
import java.util.List;

public class ReservableTransportDtoAssembler {
  private final ReservableTransportStopDtoAssembler reservableTransportStopDtoAssembler;

  public ReservableTransportDtoAssembler(
      ReservableTransportStopDtoAssembler reservableTransportStopDtoAssembler) {
    this.reservableTransportStopDtoAssembler = reservableTransportStopDtoAssembler;
  }

  public List<ReservableTransportDto> assemble(List<ReservableTransport> transports) {
    List<ReservableTransportDto> reservableTransportDtos = new ArrayList<>();
    transports.forEach(
        transport ->
            reservableTransportDtos.add(
                new ReservableTransportDto(
                    transport.getTransportID(),
                    transport.getCurrentSeatsAvailable(),
                    transport.getCost(),
                    this.reservableTransportStopDtoAssembler.assemble(transport.getStops()))));
    return reservableTransportDtos;
  }
}
