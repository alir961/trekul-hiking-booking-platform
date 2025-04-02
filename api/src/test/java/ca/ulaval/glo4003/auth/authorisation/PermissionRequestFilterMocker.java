package ca.ulaval.glo4003.auth.authorisation;

import static ca.ulaval.glo4003.traveler.user.role.Permission.ROLE_GRANT;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ca.ulaval.glo4003.traveler.user.exception.UserAccessDeniedException;
import ca.ulaval.glo4003.traveler.user.role.Permission;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import org.glassfish.jersey.server.ExtendedUriInfo;

@SuppressWarnings("checkstyle:TypeNameTestsFolder")
@PermissionRequired(ROLE_GRANT)
public class PermissionRequestFilterMocker implements ContainerRequestFilter {
  private Permission permissionToVerify = null;

  @Override
  public void filter(ContainerRequestContext containerRequestContext) {
    if (this.permissionToVerify != null) {
      ExtendedUriInfo extendedUriInfo = (ExtendedUriInfo) containerRequestContext.getUriInfo();
      Permission permission =
          extendedUriInfo
              .getMatchedResourceMethod()
              .getInvocable()
              .getHandlingMethod()
              .getAnnotation(PermissionRequired.class)
              .value();
      assertEquals(this.permissionToVerify, permission);
      throw new UserAccessDeniedException(this.permissionToVerify);
    }
  }

  @SuppressWarnings("checkstyle:MethodNameTestsFolderPublicMethods")
  public void verifyPermission(Permission permission) {
    this.permissionToVerify = permission;
  }
}
