package ca.ulaval.glo4003.exception;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.FORBIDDEN;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class TrekULDeniedExceptionMapper implements ExceptionMapper<TrekULDeniedException> {
  @Override
  public Response toResponse(TrekULDeniedException e) {
    ErrorResponse error = new ErrorResponse(e.getError(), e.getMessage());

    return Response.status(FORBIDDEN).type(APPLICATION_JSON).entity(error).build();
  }
}
