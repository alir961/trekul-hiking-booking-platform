package ca.ulaval.glo4003.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.ulaval.glo4003.trip.exception.TripInvalidCostFilterException;
import jakarta.ws.rs.core.Response;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.Test;

public class TrekULExceptionMapperTest {
  private static final TrekULException ANY_BASE_EXCEPTION = new TripInvalidCostFilterException();

  @Test
  void whenCreatingExceptionResponse_thenReturnABadRequestResponseWithDetails() {
    TrekULExceptionMapper baseExceptionMapper = new TrekULExceptionMapper();
    Response returnedResponse = baseExceptionMapper.toResponse(ANY_BASE_EXCEPTION);
    ErrorResponse expectedBody =
        new ErrorResponse(ANY_BASE_EXCEPTION.getError(), ANY_BASE_EXCEPTION.getMessage());

    assertTrue(returnedResponse.hasEntity());
    assertEquals(expectedBody, returnedResponse.getEntity());
    assertEquals(HttpStatus.BAD_REQUEST_400, returnedResponse.getStatus());
  }
}
