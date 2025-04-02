package ca.ulaval.glo4003.auth.authorisation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4003.auth.authentication.LoggedUserResolver;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.user.exception.UserAccessDeniedException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.UriInfo;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AccessAsOwnerRequestFilterTest {
  private static final String ANY_PATH_PARAM_KEY = "ownerID";
  private static final TravelerID ANY_TRAVELER_ID = new TravelerID("500fgq");
  private static final TravelerID ANY_DIFFERENT_TRAVELER_ID = new TravelerID("g4hg4g");
  private ContainerRequestContext containerRequestContextMock;
  private ResourceInfo resourceInfoMock;
  private LoggedUserResolver loggedUserResolverMock;
  private AccessAsOwnerRequestFilter accessAsOwnerRequestFilter;

  @BeforeEach
  void setUp() {
    this.containerRequestContextMock = mock(ContainerRequestContext.class);
    this.resourceInfoMock = mock(ResourceInfo.class);
    this.loggedUserResolverMock = mock(LoggedUserResolver.class);

    this.accessAsOwnerRequestFilter =
        new AccessAsOwnerRequestFilter(this.resourceInfoMock, this.loggedUserResolverMock);
    this.mockResourceInfoMethodWithAccessAsOwnerRequiredAnnotation();
  }

  @Test
  void givenLoggedInUserDoesNotExist_whenFiltering_thenThrowUserAccessDeniedException() {
    this.mockRequestContentWithAnyTravelerIDInPath();
    assertThrows(
        UserAccessDeniedException.class,
        () -> this.accessAsOwnerRequestFilter.filter(this.containerRequestContextMock));
  }

  @Test
  void givenLoggedInUserIsNotSameAsPathOwner_whenFiltering_thenThrowUserAccessDeniedException() {
    this.mockRequestContentWithAnyTravelerIDInPath();
    when(this.loggedUserResolverMock.resolve(this.containerRequestContextMock))
        .thenReturn(ANY_DIFFERENT_TRAVELER_ID);
    assertThrows(
        UserAccessDeniedException.class,
        () -> this.accessAsOwnerRequestFilter.filter(this.containerRequestContextMock));
  }

  @Test
  void givenLoggedInUserIsSameAsPathOwner_whenFiltering_thenDoNothing() {
    this.mockRequestContentWithAnyTravelerIDInPath();
    when(this.loggedUserResolverMock.resolve(this.containerRequestContextMock))
        .thenReturn(ANY_TRAVELER_ID);
    assertDoesNotThrow(
        () -> this.accessAsOwnerRequestFilter.filter(this.containerRequestContextMock));
  }

  @Test
  void givenPathDoesNotHaveAValidParamForOwner_whenFiltering_thenThrowUserAccessDeniedException() {
    this.mockRequestContentWithPathParameters();
    when(this.loggedUserResolverMock.resolve(this.containerRequestContextMock))
        .thenReturn(ANY_DIFFERENT_TRAVELER_ID);
    assertThrows(
        UserAccessDeniedException.class,
        () -> this.accessAsOwnerRequestFilter.filter(this.containerRequestContextMock));
  }

  private void mockResourceInfoMethodWithAccessAsOwnerRequiredAnnotation() {
    AccessAsOwnerRequired accessAsOwnerRequiredMock = mock(AccessAsOwnerRequired.class);
    when(accessAsOwnerRequiredMock.value()).thenReturn(ANY_PATH_PARAM_KEY);

    Method methodMock = mock(Method.class);
    when(this.resourceInfoMock.getResourceMethod()).thenReturn(methodMock);
    when(methodMock.getAnnotation(AccessAsOwnerRequired.class))
        .thenReturn(accessAsOwnerRequiredMock);
  }

  private MultivaluedMap<String, String> mockRequestContentWithPathParameters() {
    UriInfo uriInfoMock = mock(UriInfo.class);
    when(this.containerRequestContextMock.getUriInfo()).thenReturn(uriInfoMock);

    MultivaluedMap<String, String> pathParams = new MultivaluedHashMap<>();
    when(uriInfoMock.getPathParameters()).thenReturn(pathParams);

    return pathParams;
  }

  private void mockRequestContentWithAnyTravelerIDInPath() {
    MultivaluedMap<String, String> pathParams = this.mockRequestContentWithPathParameters();
    pathParams.add(ANY_PATH_PARAM_KEY, ANY_TRAVELER_ID.toString());
  }
}
