package ca.ulaval.glo4003.traveler.user.role;

import ca.ulaval.glo4003.traveler.user.User;
import ca.ulaval.glo4003.traveler.user.exception.UserAccessDeniedException;

public class UserAccessValidator {
  public void validateUserHasPermission(User user, Permission permission) {
    if (!user.hasPermission(permission)) {
      throw new UserAccessDeniedException(permission);
    }
  }
}
