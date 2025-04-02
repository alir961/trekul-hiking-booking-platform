package ca.ulaval.glo4003.token;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryAuthTokenRepositoryImpl implements AuthTokenRepository {
  private final Map<String, AuthToken> tokenStorage = Collections.synchronizedMap(new HashMap<>());

  @Override
  public Optional<AuthToken> get(String tokenValue) {
    AuthToken found = this.tokenStorage.get(tokenValue);

    if (found != null) {
      return Optional.of(found.copy());
    }

    return Optional.empty();
  }

  @Override
  public void save(AuthToken authToken) {
    this.tokenStorage.put(authToken.getValue(), authToken.copy());
  }

  @Override
  public void remove(AuthToken authToken) {
    this.tokenStorage.remove(authToken.getValue());
  }
}
