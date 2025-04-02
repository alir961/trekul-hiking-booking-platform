package ca.ulaval.glo4003.auth.authentication;

import ca.ulaval.glo4003.token.exception.UserAuthenticationException;
import ca.ulaval.glo4003.traveler.TravelerID;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;

@SuppressWarnings("checkstyle:TypeNameTestsFolder")
@AuthenticationRequired
public class AuthenticationRequestFilterMocker implements ContainerRequestFilter {
  private final TravelerID desiredTravelerID;
  private boolean asUnauthorized = false;

  public AuthenticationRequestFilterMocker(TravelerID desiredTravelerID) {
    this.desiredTravelerID = desiredTravelerID;
  }

  @Override
  public void filter(ContainerRequestContext containerRequestContext) {
    if (this.asUnauthorized) {
      throw new UserAuthenticationException();
    }
    new LoggedUserResolver().save(containerRequestContext, this.desiredTravelerID);
  }

  @SuppressWarnings("checkstyle:MethodNameTestsFolderPublicMethods")
  public void asUnauthorized() {
    this.asUnauthorized = true;
  }
}
