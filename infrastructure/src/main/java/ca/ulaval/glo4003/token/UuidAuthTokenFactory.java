package ca.ulaval.glo4003.token;

import ca.ulaval.glo4003.clock.Clock;
import ca.ulaval.glo4003.traveler.TravelerID;
import java.time.Duration;
import java.util.UUID;

public class UuidAuthTokenFactory implements AuthTokenFactory {
  private final Clock clock;
  private final Duration expirationTime;

  public UuidAuthTokenFactory(Clock clock, Duration expirationTime) {
    this.clock = clock;
    this.expirationTime = expirationTime;
  }

  @Override
  public AuthToken createToken(TravelerID userID) {
    return new AuthToken(
        UUID.randomUUID().toString(), userID, this.clock.now(), this.expirationTime);
  }
}
