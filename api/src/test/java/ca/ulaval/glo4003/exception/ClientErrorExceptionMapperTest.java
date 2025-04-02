package ca.ulaval.glo4003.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.core.Response;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class ClientErrorExceptionMapperTest {
  @ParameterizedTest
  @MethodSource("provideExceptions")
  void whenCreatingExceptionResponse_thenReturnSameStatusWithSameMessage(
      Response.Status status, String message) {
    ClientErrorException exception = new ClientErrorException(message, status);
    ClientErrorExceptionMapper clientErrorExceptionMapper = new ClientErrorExceptionMapper();
    Response returnedResponse = clientErrorExceptionMapper.toResponse(exception);
    ErrorResponse expectedBody = new ErrorResponse(status.getReasonPhrase(), message);

    assertTrue(returnedResponse.hasEntity());
    assertEquals(expectedBody, returnedResponse.getEntity());
    assertEquals(status.getStatusCode(), returnedResponse.getStatus());
  }

  private static Stream<Arguments> provideExceptions() {
    return Stream.of(
        Arguments.of(Response.Status.METHOD_NOT_ALLOWED, "any message for bad gateway"),
        Arguments.of(Response.Status.CONFLICT, "any message for conflict"));
  }
}
