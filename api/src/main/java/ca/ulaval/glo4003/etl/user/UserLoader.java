package ca.ulaval.glo4003.etl.user;

import ca.ulaval.glo4003.register.UserFactory;
import ca.ulaval.glo4003.register.dto.UserDto;
import ca.ulaval.glo4003.traveler.user.User;
import ca.ulaval.glo4003.traveler.user.UserRepository;
import ca.ulaval.glo4003.traveler.user.role.Role;
import java.util.Collection;

public class UserLoader {
  private final UserRepository userRepository;
  private final UserFactory userFactory;

  public UserLoader(UserRepository userRepository, UserFactory userFactory) {
    this.userRepository = userRepository;
    this.userFactory = userFactory;
  }

  public void load(Collection<UserDto> users, Role withRole) {
    users.forEach(user -> this.saveNewUser(user, withRole));
  }

  private void saveNewUser(UserDto userDto, Role role) {
    User user = this.userFactory.create(userDto);
    user.addRole(role);
    this.userRepository.save(user);
  }
}
