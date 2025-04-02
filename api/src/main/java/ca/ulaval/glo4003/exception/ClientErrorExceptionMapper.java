package ca.ulaval.glo4003.exception;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ClientErrorExceptionMapper implements ExceptionMapper<ClientErrorException> {
  @Override
  public Response toResponse(ClientErrorException exception) {
    Response.StatusType status = exception.getResponse().getStatusInfo();
    ErrorResponse error = new ErrorResponse(status.getReasonPhrase(), exception.getMessage());

    return Response.status(status).type(APPLICATION_JSON).entity(error).build();
  }
}
