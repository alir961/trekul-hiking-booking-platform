package ca.ulaval.glo4003.transport;

import ca.ulaval.glo4003.transport.assembler.ReservableTransportDtoAssembler;
import ca.ulaval.glo4003.transport.dto.ReservableTransportDto;
import ca.ulaval.glo4003.trip.Trip;
import ca.ulaval.glo4003.trip.TripID;
import ca.ulaval.glo4003.trip.TripRepository;
import ca.ulaval.glo4003.trip.exception.TripNotFoundException;
import java.util.List;

public class ReservableTransportService {
  private final TripRepository tripRepository;
  private final ReservableTransportDtoAssembler reservableTransportDtoAssembler;

  public ReservableTransportService(
      TripRepository tripRepository,
      ReservableTransportDtoAssembler reservableTransportDtoAssembler) {
    this.tripRepository = tripRepository;
    this.reservableTransportDtoAssembler = reservableTransportDtoAssembler;
  }

  public List<ReservableTransportDto> getDetailsOfTransports(
      TripID tripID, TransportType transportType) {
    Trip trip = this.tripRepository.get(tripID).orElseThrow(TripNotFoundException::new);
    List<ReservableTransport> transports = trip.getReservableTransports(transportType);
    return this.reservableTransportDtoAssembler.assemble(transports);
  }

  public List<TransportOption> listTransportOptions(TripID tripID) {
    Trip foundTrip = this.tripRepository.get(tripID).orElseThrow(TripNotFoundException::new);
    return foundTrip.readCurrentTransportOptions();
  }
}
