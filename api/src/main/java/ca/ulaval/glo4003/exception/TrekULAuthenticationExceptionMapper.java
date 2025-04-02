package ca.ulaval.glo4003.exception;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.UNAUTHORIZED;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class TrekULAuthenticationExceptionMapper
    implements ExceptionMapper<TrekULAuthenticationException> {
  @Override
  public Response toResponse(TrekULAuthenticationException e) {
    ErrorResponse error = new ErrorResponse(e.getError(), e.getMessage());

    return Response.status(UNAUTHORIZED).type(APPLICATION_JSON).entity(error).build();
  }
}
