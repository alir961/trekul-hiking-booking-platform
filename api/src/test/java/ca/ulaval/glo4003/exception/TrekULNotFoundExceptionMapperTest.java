package ca.ulaval.glo4003.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.ulaval.glo4003.trip.exception.TripNotFoundException;
import jakarta.ws.rs.core.Response;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.Test;

public class TrekULNotFoundExceptionMapperTest {
  private static final TrekULNotFoundException ANY_NOT_EXCEPTION = new TripNotFoundException();

  @Test
  void whenCreatingExceptionResponse_thenReturnANotFoundErrorResponseWithDetails() {
    TrekULNotFoundExceptionMapper notFoundExceptionMapper = new TrekULNotFoundExceptionMapper();
    Response returnedResponse = notFoundExceptionMapper.toResponse(ANY_NOT_EXCEPTION);
    ErrorResponse expectedBody =
        new ErrorResponse(ANY_NOT_EXCEPTION.getError(), ANY_NOT_EXCEPTION.getMessage());

    assertTrue(returnedResponse.hasEntity());
    assertEquals(expectedBody, returnedResponse.getEntity());
    assertEquals(HttpStatus.NOT_FOUND_404, returnedResponse.getStatus());
  }
}
