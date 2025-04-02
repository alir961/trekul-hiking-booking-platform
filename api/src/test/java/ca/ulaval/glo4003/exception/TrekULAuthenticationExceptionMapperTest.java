package ca.ulaval.glo4003.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.ulaval.glo4003.token.exception.UserAuthenticationException;
import jakarta.ws.rs.core.Response;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.Test;

public class TrekULAuthenticationExceptionMapperTest {
  private static final TrekULAuthenticationException ANY_AUTHENTICATION_EXCEPTION =
      new UserAuthenticationException();

  @Test
  void whenCreatingExceptionResponse_thenReturnAnUnauthorizedErrorResponseWithDetails() {
    TrekULAuthenticationExceptionMapper authenticationExceptionMapper =
        new TrekULAuthenticationExceptionMapper();
    Response returnedResponse =
        authenticationExceptionMapper.toResponse(ANY_AUTHENTICATION_EXCEPTION);
    ErrorResponse expectedBody =
        new ErrorResponse(
            ANY_AUTHENTICATION_EXCEPTION.getError(), ANY_AUTHENTICATION_EXCEPTION.getMessage());

    assertTrue(returnedResponse.hasEntity());
    assertEquals(expectedBody, returnedResponse.getEntity());
    assertEquals(HttpStatus.UNAUTHORIZED_401, returnedResponse.getStatus());
  }
}
