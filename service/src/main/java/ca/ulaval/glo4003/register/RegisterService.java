package ca.ulaval.glo4003.register;

import ca.ulaval.glo4003.register.dto.UserDto;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.user.User;
import ca.ulaval.glo4003.traveler.user.UserRepository;
import ca.ulaval.glo4003.traveler.user.credential.UserCredentialValidator;
import ca.ulaval.glo4003.traveler.user.exception.UserAlreadyExistsException;
import ca.ulaval.glo4003.traveler.user.exception.UserAlreadyExistsWithThisEmailException;
import java.util.Optional;

public class RegisterService {

  final UserFactory userFactory;
  final UserRepository userRepository;
  final UserCredentialValidator userCredentialValidator;

  public RegisterService(
      UserFactory userFactory,
      UserRepository userRepository,
      UserCredentialValidator userCredentialValidator) {
    this.userFactory = userFactory;
    this.userRepository = userRepository;
    this.userCredentialValidator = userCredentialValidator;
  }

  public void register(UserDto userDto) {
    TravelerID userID = new TravelerID(userDto.idul());
    Optional<User> optionalUserById = this.userRepository.getById(userID);
    Optional<User> optionalUserByEmail = this.userRepository.getByEmail(userDto.email());

    if (optionalUserById.isPresent()) {
      throw new UserAlreadyExistsException();
    }

    if (optionalUserByEmail.isPresent()) {
      throw new UserAlreadyExistsWithThisEmailException();
    }
    this.userCredentialValidator.validate(userDto.email(), userDto.password());
    User user = this.userFactory.create(userDto);
    this.userRepository.save(user);
  }
}
