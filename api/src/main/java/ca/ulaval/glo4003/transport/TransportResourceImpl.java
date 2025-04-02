package ca.ulaval.glo4003.transport;

import ca.ulaval.glo4003.transport.assembler.ReservableTransportResponseAssembler;
import ca.ulaval.glo4003.transport.assembler.TransportOptionResponseAssembler;
import ca.ulaval.glo4003.transport.dto.ReservableTransportDto;
import ca.ulaval.glo4003.transport.response.ReservableTransportResponse;
import ca.ulaval.glo4003.transport.response.TransportOptionResponse;
import ca.ulaval.glo4003.trip.TripID;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/api/trekul/trips/{tripId}/transports")
public class TransportResourceImpl implements TransportResource {
  private final ReservableTransportService transportService;
  private final TransportOptionResponseAssembler transportOptionResponseAssembler;
  private final ReservableTransportResponseAssembler reservableTransportResponseAssembler;

  @Inject
  public TransportResourceImpl(
      ReservableTransportService transportService,
      TransportOptionResponseAssembler transportOptionResponseAssembler,
      ReservableTransportResponseAssembler reservableTransportResponseAssembler) {
    this.transportService = transportService;
    this.transportOptionResponseAssembler = transportOptionResponseAssembler;
    this.reservableTransportResponseAssembler = reservableTransportResponseAssembler;
  }

  @Override
  @GET
  public Response listTransportOptionsOfTrip(@PathParam("tripId") TripID tripID) {
    List<TransportOption> options = this.transportService.listTransportOptions(tripID);
    List<TransportOptionResponse> response =
        this.transportOptionResponseAssembler.assemble(options);
    return Response.ok(response).build();
  }

  @Override
  @GET
  @Path("{transportType}")
  public Response getTransportDetails(
      @PathParam("tripId") TripID tripID, @PathParam("transportType") String transportTypeStr) {
    TransportType transportType = TransportType.valueOf(transportTypeStr.toUpperCase());
    List<ReservableTransportDto> transportsList =
        this.transportService.getDetailsOfTransports(tripID, transportType);
    List<ReservableTransportResponse> responses =
        this.reservableTransportResponseAssembler.assemble(transportsList);

    return Response.ok(responses).build();
  }
}
