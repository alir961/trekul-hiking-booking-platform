package ca.ulaval.glo4003.user;

import static ca.ulaval.glo4003.traveler.user.role.Permission.ROLE_GRANT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.user.User;
import ca.ulaval.glo4003.traveler.user.UserRepository;
import ca.ulaval.glo4003.traveler.user.exception.UserAccessDeniedException;
import ca.ulaval.glo4003.traveler.user.role.Permission;
import ca.ulaval.glo4003.traveler.user.role.UserAccessValidator;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserPermissionServiceTest {
  private static final TravelerID ANY_TRAVELER_ID = new TravelerID("6022f2");
  private static final Permission ANY_PERMISSION = ROLE_GRANT;
  private UserRepository userRepositoryMock;
  private UserAccessValidator userAccessValidatorMock;

  private UserPermissionService userPermissionService;

  @BeforeEach
  void setUp() {
    this.userRepositoryMock = mock(UserRepository.class);
    this.userAccessValidatorMock = mock(UserAccessValidator.class);
    this.userPermissionService =
        new UserPermissionService(this.userRepositoryMock, this.userAccessValidatorMock);
  }

  @Test
  void givenUserExists_whenValidatingPermission_thenValidateUserHasPermission() {
    User anyUser = mock(User.class);
    when(this.userRepositoryMock.getById(ANY_TRAVELER_ID)).thenReturn(Optional.of(anyUser));

    this.userPermissionService.validatePermission(ANY_TRAVELER_ID, ANY_PERMISSION);

    verify(this.userAccessValidatorMock).validateUserHasPermission(anyUser, ANY_PERMISSION);
  }

  @Test
  void givenUserDoesNotExists_whenValidatingPermission_thenThrowUserAccessDeniedException() {
    when(this.userRepositoryMock.getById(ANY_TRAVELER_ID)).thenReturn(Optional.empty());

    assertThrows(
        UserAccessDeniedException.class,
        () -> this.userPermissionService.validatePermission(ANY_TRAVELER_ID, ANY_PERMISSION));
  }
}
