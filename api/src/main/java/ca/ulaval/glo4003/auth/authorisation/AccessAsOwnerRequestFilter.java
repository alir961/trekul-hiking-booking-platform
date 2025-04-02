package ca.ulaval.glo4003.auth.authorisation;

import ca.ulaval.glo4003.auth.authentication.LoggedUserResolver;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.user.exception.UserAccessDeniedException;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import java.lang.reflect.Method;

@AccessAsOwnerRequired("")
public class AccessAsOwnerRequestFilter implements ContainerRequestFilter {
  private final ResourceInfo resourceInfo;
  private final LoggedUserResolver loggedUserResolver;

  @Inject
  public AccessAsOwnerRequestFilter(
      @Context ResourceInfo resourceInfo, LoggedUserResolver loggedUserResolver) {
    this.resourceInfo = resourceInfo;
    this.loggedUserResolver = loggedUserResolver;
  }

  @Override
  public void filter(ContainerRequestContext requestContext) {
    Method method = this.resourceInfo.getResourceMethod();
    String userPathParamKey = method.getAnnotation(AccessAsOwnerRequired.class).value();
    TravelerID loggedUser = this.loggedUserResolver.resolve(requestContext);
    String ownerIDStr = requestContext.getUriInfo().getPathParameters().getFirst(userPathParamKey);

    if (this.isLoggedUserTheOwner(loggedUser, ownerIDStr)) {
      throw new UserAccessDeniedException();
    }
  }

  private boolean isLoggedUserTheOwner(TravelerID loggedUser, String ownerIDStr) {
    return loggedUser == null || !loggedUser.toString().equals(ownerIDStr);
  }
}
