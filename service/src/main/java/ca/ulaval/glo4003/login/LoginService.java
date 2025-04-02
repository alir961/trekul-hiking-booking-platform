package ca.ulaval.glo4003.login;

import ca.ulaval.glo4003.token.AuthToken;
import ca.ulaval.glo4003.token.AuthTokenFactory;
import ca.ulaval.glo4003.token.AuthTokenRepository;
import ca.ulaval.glo4003.traveler.user.User;
import ca.ulaval.glo4003.traveler.user.UserRepository;
import ca.ulaval.glo4003.traveler.user.exception.UserLoginWrongCredentialException;

public class LoginService {
  private final UserRepository userRepository;
  private final AuthTokenRepository authTokenRepository;
  private final AuthTokenFactory authTokenFactory;

  public LoginService(
      UserRepository userRepository,
      AuthTokenRepository authTokenRepository,
      AuthTokenFactory authTokenFactory) {
    this.userRepository = userRepository;
    this.authTokenRepository = authTokenRepository;
    this.authTokenFactory = authTokenFactory;
  }

  public AuthToken loginWithEmailAndPassword(String emailAddress, String providedPassword) {
    User user =
        this.userRepository
            .getByEmail(emailAddress)
            .orElseThrow(UserLoginWrongCredentialException::new);
    user.validatePassword(providedPassword);
    AuthToken authToken = this.authTokenFactory.createToken(user.getId());
    this.authTokenRepository.save(authToken);

    return authToken;
  }
}
