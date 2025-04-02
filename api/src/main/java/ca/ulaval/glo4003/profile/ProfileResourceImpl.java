package ca.ulaval.glo4003.profile;

import ca.ulaval.glo4003.auth.authentication.AuthenticationRequired;
import ca.ulaval.glo4003.auth.authentication.LoggedIn;
import ca.ulaval.glo4003.profile.assembler.ProfileTripResponseAssembler;
import ca.ulaval.glo4003.profile.dto.ProfileTripDto;
import ca.ulaval.glo4003.profile.response.ProfileTripResponse;
import ca.ulaval.glo4003.reservation.ReservationStatus;
import ca.ulaval.glo4003.traveler.TravelerID;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("api/trekul/profile")
@AuthenticationRequired
public class ProfileResourceImpl implements ProfileResource {
  private final ProfileTripsService profileTripsService;
  private final ProfileTripResponseAssembler profileTripResponseAssembler;

  @Inject
  public ProfileResourceImpl(
      ProfileTripsService profileTripsService,
      ProfileTripResponseAssembler profileTripResponseAssembler) {
    this.profileTripsService = profileTripsService;
    this.profileTripResponseAssembler = profileTripResponseAssembler;
  }

  @GET
  @Path("trips")
  @Produces(MediaType.APPLICATION_JSON)
  @Override
  public Response searchProfileTrips(
      @LoggedIn TravelerID travelerID, @QueryParam(value = "status") String status) {

    ReservationStatus responseStatus = ReservationStatus.valueOf(status.toUpperCase());
    List<ProfileTripDto> trips = this.profileTripsService.searchTrips(travelerID, responseStatus);
    List<ProfileTripResponse> response = this.profileTripResponseAssembler.assemble(trips);
    return Response.ok(response).build();
  }
}
