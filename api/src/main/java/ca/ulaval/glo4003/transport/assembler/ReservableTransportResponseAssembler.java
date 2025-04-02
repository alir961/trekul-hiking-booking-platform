package ca.ulaval.glo4003.transport.assembler;

import ca.ulaval.glo4003.transport.dto.ReservableTransportDto;
import ca.ulaval.glo4003.transport.response.ReservableTransportResponse;
import ca.ulaval.glo4003.transport.response.ReservableTransportStopResponse;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class ReservableTransportResponseAssembler {
  private final ReservableTransportStopResponseAssembler reservableTransportStopResponseAssembler;

  @Inject
  public ReservableTransportResponseAssembler(
      ReservableTransportStopResponseAssembler reservableTransportStopResponseAssembler) {
    this.reservableTransportStopResponseAssembler = reservableTransportStopResponseAssembler;
  }

  public List<ReservableTransportResponse> assemble(
      List<ReservableTransportDto> reservableTransportDtos) {
    List<ReservableTransportResponse> reservableTransportRespons = new ArrayList<>();
    List<ReservableTransportStopResponse> stops =
        this.reservableTransportStopResponseAssembler.assemble(
            reservableTransportDtos.getFirst().stops());
    reservableTransportDtos.forEach(
        transportDto ->
            reservableTransportRespons.add(
                new ReservableTransportResponse(
                    transportDto.transportID().toString(),
                    transportDto.availableSeats(),
                    transportDto.cost().toString(),
                    stops)));
    return reservableTransportRespons;
  }
}
