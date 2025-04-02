package ca.ulaval.glo4003.auth.authorisation;

import static ca.ulaval.glo4003.traveler.user.role.Permission.ROLE_GRANT;

import ca.ulaval.glo4003.auth.authentication.LoggedUserResolver;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.user.role.Permission;
import ca.ulaval.glo4003.user.UserPermissionService;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import java.lang.reflect.Method;

// A permission must be set as default, but the value doesn't matter for name binding.
@PermissionRequired(ROLE_GRANT)
public class PermissionRequestFilter implements ContainerRequestFilter {
  private final ResourceInfo resourceInfo;
  private final UserPermissionService userPermissionService;
  private final LoggedUserResolver loggedUserResolver;

  @Inject
  public PermissionRequestFilter(
      @Context ResourceInfo resourceInfo,
      UserPermissionService userPermissionService,
      LoggedUserResolver loggedUserResolver) {
    this.resourceInfo = resourceInfo;
    this.userPermissionService = userPermissionService;
    this.loggedUserResolver = loggedUserResolver;
  }

  @Override
  public void filter(ContainerRequestContext requestContext) {
    Method method = this.resourceInfo.getResourceMethod();
    Permission permission = method.getAnnotation(PermissionRequired.class).value();
    TravelerID loggedUser = this.loggedUserResolver.resolve(requestContext);
    this.userPermissionService.validatePermission(loggedUser, permission);
  }
}
