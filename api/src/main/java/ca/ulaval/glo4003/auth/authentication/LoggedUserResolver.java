package ca.ulaval.glo4003.auth.authentication;

import ca.ulaval.glo4003.traveler.TravelerID;
import jakarta.ws.rs.container.ContainerRequestContext;
import java.util.function.Function;
import org.glassfish.jersey.server.ContainerRequest;

public class LoggedUserResolver implements Function<ContainerRequest, TravelerID> {
  private static final String AUTHENTICATED_USER_PROPERTY = "x-traveler-id";

  @Override
  public TravelerID apply(ContainerRequest containerRequest) {
    return (TravelerID) containerRequest.getProperty(AUTHENTICATED_USER_PROPERTY);
  }

  public TravelerID resolve(ContainerRequestContext containerRequest) {
    return (TravelerID) containerRequest.getProperty(AUTHENTICATED_USER_PROPERTY);
  }

  public void save(ContainerRequestContext containerRequest, TravelerID loggedUser) {
    containerRequest.setProperty(AUTHENTICATED_USER_PROPERTY, loggedUser);
  }
}
