package ca.ulaval.glo4003.trip;

import static ca.ulaval.glo4003.traveler.user.role.Permission.CODE_VERIFICATION;
import static ca.ulaval.glo4003.traveler.user.role.Permission.TRIP_UPDATE;

import ca.ulaval.glo4003.auth.authentication.AuthenticationRequired;
import ca.ulaval.glo4003.auth.authentication.LoggedIn;
import ca.ulaval.glo4003.auth.authorisation.PermissionRequired;
import ca.ulaval.glo4003.reservation.request.ReservationRequest;
import ca.ulaval.glo4003.reservation.response.ReservationResponse;
import ca.ulaval.glo4003.reservation.usecase.ReservationCreationUseCase;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.trip.usecase.BlockTripUseCase;
import ca.ulaval.glo4003.trip.usecase.VerifyTripUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@AuthenticationRequired
public class TripAuthenticatedResourceImpl implements TripAuthenticatedResource {
  private final BlockTripUseCase blockTripUseCase;
  private final ReservationCreationUseCase reservationCreationUseCase;
  private final VerifyTripUseCase verifyTripUseCase;

  @Inject
  public TripAuthenticatedResourceImpl(
      BlockTripUseCase blockTripUseCase,
      ReservationCreationUseCase reservationCreationUseCase,
      VerifyTripUseCase verifyTripUseCase) {
    this.blockTripUseCase = blockTripUseCase;
    this.reservationCreationUseCase = reservationCreationUseCase;
    this.verifyTripUseCase = verifyTripUseCase;
  }

  @POST
  @Path("{tripId}:reserve")
  public Response createReservation(
      @PathParam("tripId") TripID tripID,
      @LoggedIn TravelerID userID,
      ReservationRequest reservationRequest) {
    ReservationResponse response =
        this.reservationCreationUseCase.createReservation(reservationRequest, userID, tripID);
    return Response.status(Response.Status.CREATED).entity(response).build();
  }

  @PUT
  @Path("{tripId}:block")
  @Produces(MediaType.APPLICATION_JSON)
  @PermissionRequired(TRIP_UPDATE)
  public Response blockTrip(@PathParam("tripId") TripID tripID) {
    this.blockTripUseCase.blockTrip(tripID);
    return Response.noContent().build();
  }

  @GET
  @Path("{tripId}:validate")
  @Produces(MediaType.APPLICATION_JSON)
  @PermissionRequired(CODE_VERIFICATION)
  public Response validateConfirmationCode(
      @PathParam("tripId") TripID tripID, @QueryParam("code") String code) {

    return Response.ok(this.verifyTripUseCase.verifyTrip(tripID, code)).build();
  }

  @Path("/")
  public Class<TripGuestResourceImpl> require2FA() {
    return TripGuestResourceImpl.class;
  }
}
