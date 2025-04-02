package ca.ulaval.glo4003.exception;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class DefaultExceptionMapper implements ExceptionMapper<Exception> {

  @Override
  public Response toResponse(Exception exception) {
    ErrorResponse error = new ErrorResponse("Exception during runtime", exception.getMessage());

    return Response.status(INTERNAL_SERVER_ERROR).type(APPLICATION_JSON).entity(error).build();
  }
}
