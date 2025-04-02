package ca.ulaval.glo4003.trip;

import ca.ulaval.glo4003.trip.request.SearchTripRequest;
import ca.ulaval.glo4003.trip.usecase.SearchTripUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

@Path("/api/trekul/trips")
public class TripResourceImpl implements TripResource {

  private final SearchTripUseCase searchTripUseCase;

  @Inject
  public TripResourceImpl(SearchTripUseCase searchTripUseCase) {
    this.searchTripUseCase = searchTripUseCase;
  }

  @Override
  @GET
  public Response searchTrip(@BeanParam SearchTripRequest searchTripRequest) {
    return Response.ok(this.searchTripUseCase.searchTrip(searchTripRequest)).build();
  }

  @Path("/")
  public Class<TripAuthenticatedResourceImpl> requireAuthentication() {
    return TripAuthenticatedResourceImpl.class;
  }
}
