package ca.ulaval.glo4003.auth.authorisation;

import static ca.ulaval.glo4003.traveler.user.role.Permission.ROLE_REMOVE;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.auth.authentication.LoggedUserResolver;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.user.role.Permission;
import ca.ulaval.glo4003.user.UserPermissionService;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ResourceInfo;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PermissionRequestFilterTest {
  private static final TravelerID ANY_TRAVELER_ID = new TravelerID("h094jhh");
  private static final Permission ANY_PERMISSION = ROLE_REMOVE;
  private ContainerRequestContext containerRequestContextMock;
  private LoggedUserResolver loggedUserResolverMock;
  private ResourceInfo resourceInfoMock;
  private UserPermissionService userPermissionServiceMock;
  private PermissionRequestFilter permissionRequestFilter;

  @BeforeEach
  void setUp() {
    this.resourceInfoMock = mock(ResourceInfo.class);
    this.userPermissionServiceMock = mock(UserPermissionService.class);
    this.containerRequestContextMock = mock(ContainerRequestContext.class);
    this.loggedUserResolverMock = mock(LoggedUserResolver.class);

    this.permissionRequestFilter =
        new PermissionRequestFilter(
            this.resourceInfoMock, this.userPermissionServiceMock, this.loggedUserResolverMock);

    this.mockResourceInfoMethodWithPermissionRequiredAnnotation();
  }

  @Test
  void givenLoggedUserDoesNotExist_whenFiltering_thenValidatePermissionWithNull() {
    this.permissionRequestFilter.filter(this.containerRequestContextMock);

    verify(this.userPermissionServiceMock).validatePermission(null, ANY_PERMISSION);
  }

  @Test
  void givenLoggedUserExist_whenFiltering_thenValidatePermissionWithIt() {
    when(this.loggedUserResolverMock.resolve(this.containerRequestContextMock))
        .thenReturn(ANY_TRAVELER_ID);
    this.permissionRequestFilter.filter(this.containerRequestContextMock);

    verify(this.userPermissionServiceMock).validatePermission(ANY_TRAVELER_ID, ANY_PERMISSION);
  }

  private void mockResourceInfoMethodWithPermissionRequiredAnnotation() {
    PermissionRequired permissionRequiredMock = mock(PermissionRequired.class);
    when(permissionRequiredMock.value()).thenReturn(ANY_PERMISSION);

    Method methodMock = mock(Method.class);
    when(this.resourceInfoMock.getResourceMethod()).thenReturn(methodMock);
    when(methodMock.getAnnotation(PermissionRequired.class)).thenReturn(permissionRequiredMock);
  }
}
