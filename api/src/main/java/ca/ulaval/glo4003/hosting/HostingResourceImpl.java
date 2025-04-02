package ca.ulaval.glo4003.hosting;

import static ca.ulaval.glo4003.traveler.user.role.Permission.HOSTING_RESERVATION;

import ca.ulaval.glo4003.auth.authentication.AuthenticationRequired;
import ca.ulaval.glo4003.auth.authorisation.PermissionRequired;
import ca.ulaval.glo4003.hosting.assembler.SearchHostingResponseAssembler;
import ca.ulaval.glo4003.hosting.dto.HostingDto;
import ca.ulaval.glo4003.hosting.reservation.HostingReservationService;
import ca.ulaval.glo4003.hosting.reservation.assembler.CreateHostingReservationResponseAssembler;
import ca.ulaval.glo4003.hosting.reservation.dto.CreatedHostingReservationDto;
import ca.ulaval.glo4003.hosting.reservation.response.HostingReservationResponse;
import ca.ulaval.glo4003.hosting.response.SearchHostingResponse;
import ca.ulaval.glo4003.trip.TripID;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/api/trekul/trips/{tripID}/hosts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HostingResourceImpl implements HostingResource {

  private final HostingService hostingService;
  private final SearchHostingResponseAssembler hostingResponseAssembler;
  private final HostingReservationService hostingReservationService;
  private final CreateHostingReservationResponseAssembler hostingReservationResponseAssembler;

  @Inject
  public HostingResourceImpl(
      HostingService hostingService,
      SearchHostingResponseAssembler hostingResponseAssembler,
      HostingReservationService hostingReservationService,
      CreateHostingReservationResponseAssembler hostingReservationResponseAssembler) {
    this.hostingService = hostingService;
    this.hostingResponseAssembler = hostingResponseAssembler;
    this.hostingReservationService = hostingReservationService;
    this.hostingReservationResponseAssembler = hostingReservationResponseAssembler;
  }

  @GET
  @Override
  public Response findHostsInRegion(@PathParam("tripID") TripID tripID) {
    List<HostingDto> hosts = this.hostingService.searchCompatibleHosts(tripID);
    List<SearchHostingResponse> response = this.hostingResponseAssembler.assemble(hosts);

    return Response.ok(response).build();
  }

  @POST
  @Override
  @Path("/{hostingID}:book")
  @AuthenticationRequired
  @PermissionRequired(HOSTING_RESERVATION)
  public Response bookHosting(
      @PathParam("tripID") TripID tripID, @PathParam("hostingID") String hostingIDString) {

    HostingID hostingID = new HostingID(Integer.valueOf(hostingIDString));
    CreatedHostingReservationDto createdHostingReservationDto =
        this.hostingReservationService.bookHosting(tripID, hostingID);
    HostingReservationResponse hostingReservationResponse =
        this.hostingReservationResponseAssembler.assemble(createdHostingReservationDto);
    return Response.ok(hostingReservationResponse).build();
  }
}
