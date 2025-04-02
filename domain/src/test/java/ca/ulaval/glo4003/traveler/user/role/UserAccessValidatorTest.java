package ca.ulaval.glo4003.traveler.user.role;

import static ca.ulaval.glo4003.traveler.user.role.Permission.TRIP_UPDATE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4003.traveler.user.User;
import ca.ulaval.glo4003.traveler.user.exception.UserAccessDeniedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserAccessValidatorTest {
  private static final Permission ANY_PERMISSION = TRIP_UPDATE;
  private UserAccessValidator userAccessValidator;

  @BeforeEach
  void setUp() {
    this.userAccessValidator = new UserAccessValidator();
  }

  @Test
  void
      givenUserDoNotHaveTargetPermission_whenValidatingUserHasPermission_thenThrowUserAccessDeniedException() {
    User userWithoutPermission = mock(User.class);
    when(userWithoutPermission.hasPermission(ANY_PERMISSION)).thenReturn(false);

    assertThrows(
        UserAccessDeniedException.class,
        () ->
            this.userAccessValidator.validateUserHasPermission(
                userWithoutPermission, ANY_PERMISSION));
  }

  @Test
  void givenUserHasTargetPermission_whenValidatingUserHasPermission_thenDoNothing() {
    User userWithPermission = mock(User.class);
    when(userWithPermission.hasPermission(ANY_PERMISSION)).thenReturn(true);

    assertDoesNotThrow(
        () ->
            this.userAccessValidator.validateUserHasPermission(userWithPermission, ANY_PERMISSION));
  }
}
