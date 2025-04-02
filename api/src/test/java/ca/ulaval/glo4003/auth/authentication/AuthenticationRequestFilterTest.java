package ca.ulaval.glo4003.auth.authentication;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.token.AuthTokenService;
import ca.ulaval.glo4003.token.exception.UserAuthenticationException;
import ca.ulaval.glo4003.traveler.TravelerID;
import jakarta.ws.rs.container.ContainerRequestContext;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

public class AuthenticationRequestFilterTest {
  private static final String AUTHENTICATION_HEADER_KEY = "Authorization";
  private static final String ANY_VALID_TOKEN = "g954jhh";
  private LoggedUserResolver loggedUserResolverMock;
  private AuthTokenService authTokenServiceMock;
  private ContainerRequestContext containerRequestContextMock;
  private AuthenticationRequestFilter authenticationRequestFilter;

  @BeforeEach
  void setUp() {
    this.authTokenServiceMock = mock(AuthTokenService.class);
    this.loggedUserResolverMock = mock(LoggedUserResolver.class);
    this.authenticationRequestFilter =
        new AuthenticationRequestFilter(this.authTokenServiceMock, this.loggedUserResolverMock);

    this.containerRequestContextMock = mock(ContainerRequestContext.class);
  }

  @ParameterizedTest
  @NullAndEmptySource
  void givenEmptyTokenInHeader_whenFiltering_thenThrowUserAuthenticationException(
      String authToken) {
    when(this.containerRequestContextMock.getHeaderString(AUTHENTICATION_HEADER_KEY))
        .thenReturn(authToken);

    assertThrows(
        UserAuthenticationException.class,
        () -> this.authenticationRequestFilter.filter(this.containerRequestContextMock));
  }

  @ParameterizedTest
  @ValueSource(strings = {"Bearer " + ANY_VALID_TOKEN, ANY_VALID_TOKEN})
  void givenValidToken_whenFiltering_thenSaveTravelerIDInContext(String authToken)
      throws IOException {
    when(this.containerRequestContextMock.getHeaderString(AUTHENTICATION_HEADER_KEY))
        .thenReturn(authToken);
    TravelerID anyTravelerID = new TravelerID("anything");
    when(this.authTokenServiceMock.readToken(ANY_VALID_TOKEN)).thenReturn(anyTravelerID);

    this.authenticationRequestFilter.filter(this.containerRequestContextMock);

    verify(this.loggedUserResolverMock).save(this.containerRequestContextMock, anyTravelerID);
  }
}
