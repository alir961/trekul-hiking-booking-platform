package ca.ulaval.glo4003.token;

import java.util.Optional;

public interface AuthTokenRepository {
  Optional<AuthToken> get(String tokenValue);

  void save(AuthToken authToken);

  void remove(AuthToken authToken);
}
