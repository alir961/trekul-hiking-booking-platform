package ca.ulaval.glo4003.exception;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.*;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class TrekULSystemExceptionMapper implements ExceptionMapper<TrekULSystemException> {
  @Override
  public Response toResponse(TrekULSystemException e) {
    ErrorResponse error = new ErrorResponse(e.getError(), e.getMessage());

    return Response.status(INTERNAL_SERVER_ERROR).type(APPLICATION_JSON).entity(error).build();
  }
}
