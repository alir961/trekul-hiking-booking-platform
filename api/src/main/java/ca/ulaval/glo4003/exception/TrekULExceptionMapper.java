package ca.ulaval.glo4003.exception;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.BAD_REQUEST;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class TrekULExceptionMapper implements ExceptionMapper<TrekULException> {
  @Override
  public Response toResponse(TrekULException e) {
    ErrorResponse error = new ErrorResponse(e.getError(), e.getMessage());

    return Response.status(BAD_REQUEST).type(APPLICATION_JSON).entity(error).build();
  }
}
