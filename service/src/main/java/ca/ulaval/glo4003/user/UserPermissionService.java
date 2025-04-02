package ca.ulaval.glo4003.user;

import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.user.User;
import ca.ulaval.glo4003.traveler.user.UserRepository;
import ca.ulaval.glo4003.traveler.user.exception.UserAccessDeniedException;
import ca.ulaval.glo4003.traveler.user.role.Permission;
import ca.ulaval.glo4003.traveler.user.role.UserAccessValidator;

public class UserPermissionService {
  private final UserRepository userRepository;
  private final UserAccessValidator userAccessValidator;

  public UserPermissionService(
      UserRepository userRepository, UserAccessValidator userAccessValidator) {
    this.userRepository = userRepository;
    this.userAccessValidator = userAccessValidator;
  }

  public void validatePermission(TravelerID travelerID, Permission permission) {
    User user =
        this.userRepository
            .getById(travelerID)
            .orElseThrow(() -> new UserAccessDeniedException(permission));
    this.userAccessValidator.validateUserHasPermission(user, permission);
  }
}
