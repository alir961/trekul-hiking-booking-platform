package ca.ulaval.glo4003.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.ws.rs.core.Response;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DefaultExceptionMapperTest {
  private static final String ERROR_MESSAGE = "Exception during runtime";
  private static final String ANY_EXCEPTION_MESSAGE = "MESSAGE";
  private ErrorResponse errorResponse;
  private Exception exceptionMock;
  private DefaultExceptionMapper defaultExceptionMapper;

  @BeforeEach
  void setUp() {
    this.exceptionMock = mock(Exception.class);
    this.defaultExceptionMapper = new DefaultExceptionMapper();
    when(this.exceptionMock.getMessage()).thenReturn(ANY_EXCEPTION_MESSAGE);
    this.errorResponse = new ErrorResponse(ERROR_MESSAGE, this.exceptionMock.getMessage());
  }

  @Test
  void whenCreatingExceptionResponse_thenReturnCorrectResponse() {
    Response returnedResponse = this.defaultExceptionMapper.toResponse(this.exceptionMock);

    assertTrue(returnedResponse.hasEntity());
    assertEquals(this.errorResponse, returnedResponse.getEntity());
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, returnedResponse.getStatus());
  }
}
