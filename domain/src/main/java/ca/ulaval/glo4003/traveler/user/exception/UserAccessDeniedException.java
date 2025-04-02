package ca.ulaval.glo4003.traveler.user.exception;

import ca.ulaval.glo4003.exception.TrekULDeniedException;
import ca.ulaval.glo4003.traveler.user.role.Permission;

public class UserAccessDeniedException extends TrekULDeniedException {
  private static final String EXCEPTION_ERROR = "USER_ACCESS_DENIED";

  public UserAccessDeniedException(Permission permission) {
    super(
        EXCEPTION_ERROR,
        String.format("User doesn't have permission '%s' in its roles", permission));
  }

  public UserAccessDeniedException() {
    super(EXCEPTION_ERROR, "User doesn't have access to this resource");
  }
}
