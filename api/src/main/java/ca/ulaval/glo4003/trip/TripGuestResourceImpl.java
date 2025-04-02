package ca.ulaval.glo4003.trip;

import ca.ulaval.glo4003.auth.authentication.AuthenticationRequired;
import ca.ulaval.glo4003.auth.authentication.LoggedIn;
import ca.ulaval.glo4003.token.Guest2FAService;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.trip.usecase.SearchTripUnauthenticatedUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class TripGuestResourceImpl implements TripGuestResource {
  private final SearchTripUnauthenticatedUseCase searchTripUnauthenticatedUseCase;
  private final Guest2FAService guest2FAService;

  @Inject
  public TripGuestResourceImpl(
      SearchTripUnauthenticatedUseCase searchTripUnauthenticatedUseCase,
      Guest2FAService guest2FAService) {
    this.searchTripUnauthenticatedUseCase = searchTripUnauthenticatedUseCase;
    this.guest2FAService = guest2FAService;
  }

  @GET
  @Path("{tripId}:search")
  @Produces(MediaType.APPLICATION_JSON)
  @AuthenticationRequired
  public Response searchTripByIdAndTravelerId(
      @PathParam("tripId") TripID tripID, @LoggedIn TravelerID guestID) {
    return Response.ok(
            this.searchTripUnauthenticatedUseCase.searchTripUnauthenticated(tripID, guestID))
        .build();
  }

  @GET
  @Path("{tripId}/guests/{travelerId}/2FA")
  @Produces(MediaType.APPLICATION_JSON)
  public Response activate2FAForGuestOfTrip(
      @PathParam("tripId") TripID tripID, @PathParam("travelerId") String travelerId) {
    TravelerID travelerID = new TravelerID(travelerId);
    this.guest2FAService.send2FAToken(tripID, travelerID);
    return Response.noContent().build();
  }
}
