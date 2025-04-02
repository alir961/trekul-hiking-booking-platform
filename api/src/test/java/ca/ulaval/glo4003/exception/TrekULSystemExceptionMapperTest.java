package ca.ulaval.glo4003.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.ulaval.glo4003.config.exception.ConfigurationException;
import jakarta.ws.rs.core.Response;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.Test;

public class TrekULSystemExceptionMapperTest {
  private static final TrekULSystemException ANY_SYSTEM_EXCEPTION =
      new ConfigurationException("any details");

  @Test
  void whenCreatingExceptionResponse_thenReturnAnInternalErrorResponseWithDetails() {
    TrekULSystemExceptionMapper systemExceptionMapper = new TrekULSystemExceptionMapper();
    Response returnedResponse = systemExceptionMapper.toResponse(ANY_SYSTEM_EXCEPTION);
    ErrorResponse expectedBody =
        new ErrorResponse(ANY_SYSTEM_EXCEPTION.getError(), ANY_SYSTEM_EXCEPTION.getMessage());

    assertTrue(returnedResponse.hasEntity());
    assertEquals(expectedBody, returnedResponse.getEntity());
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, returnedResponse.getStatus());
  }
}
