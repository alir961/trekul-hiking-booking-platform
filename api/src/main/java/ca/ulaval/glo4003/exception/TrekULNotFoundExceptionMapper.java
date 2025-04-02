package ca.ulaval.glo4003.exception;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class TrekULNotFoundExceptionMapper implements ExceptionMapper<TrekULNotFoundException> {
  @Override
  public Response toResponse(TrekULNotFoundException e) {
    ErrorResponse error = new ErrorResponse(e.getError(), e.getMessage());

    return Response.status(NOT_FOUND).type(APPLICATION_JSON).entity(error).build();
  }
}
