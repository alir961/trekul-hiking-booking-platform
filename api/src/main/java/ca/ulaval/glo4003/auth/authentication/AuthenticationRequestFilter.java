package ca.ulaval.glo4003.auth.authentication;

import ca.ulaval.glo4003.token.AuthTokenService;
import ca.ulaval.glo4003.token.exception.UserAuthenticationException;
import ca.ulaval.glo4003.traveler.TravelerID;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;

@AuthenticationRequired
public class AuthenticationRequestFilter implements ContainerRequestFilter {
  private static final String AUTHENTICATION_HEADER_KEY = "Authorization";

  private final AuthTokenService authTokenService;
  private final LoggedUserResolver loggedUserResolver;

  @Inject
  public AuthenticationRequestFilter(
      AuthTokenService authTokenService, LoggedUserResolver loggedUserResolver) {
    this.authTokenService = authTokenService;
    this.loggedUserResolver = loggedUserResolver;
  }

  @Override
  public void filter(ContainerRequestContext containerRequestContext) {
    String authToken = containerRequestContext.getHeaderString(AUTHENTICATION_HEADER_KEY);
    TravelerID loggedTraveler = this.fetchTravelerID(authToken);
    this.loggedUserResolver.save(containerRequestContext, loggedTraveler);
  }

  public TravelerID fetchTravelerID(String authToken) {
    if (authToken == null || authToken.isEmpty()) {
      throw new UserAuthenticationException();
    }
    String bareToken = authToken.replace("Bearer ", "");
    return this.authTokenService.readToken(bareToken);
  }
}
