package ca.ulaval.glo4003.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.ulaval.glo4003.transport.exception.TransportFullyBookedException;
import jakarta.ws.rs.core.Response;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.Test;

public class TrekULDeniedExceptionMapperTest {
  private static final TrekULDeniedException ANY_DENIED_EXCEPTION =
      new TransportFullyBookedException();

  @Test
  void whenCreatingExceptionResponse_thenReturnAForbiddenErrorResponseWithDetails() {
    TrekULDeniedExceptionMapper deniedExceptionMapper = new TrekULDeniedExceptionMapper();
    Response returnedResponse = deniedExceptionMapper.toResponse(ANY_DENIED_EXCEPTION);
    ErrorResponse expectedBody =
        new ErrorResponse(ANY_DENIED_EXCEPTION.getError(), ANY_DENIED_EXCEPTION.getMessage());

    assertTrue(returnedResponse.hasEntity());
    assertEquals(expectedBody, returnedResponse.getEntity());
    assertEquals(HttpStatus.FORBIDDEN_403, returnedResponse.getStatus());
  }
}
