package ca.ulaval.glo4003.token;

import ca.ulaval.glo4003.traveler.TravelerID;
import java.time.Duration;
import java.time.Instant;

public class AuthToken {
  private final String token;
  private final TravelerID userID;
  private final Instant createdTime;
  private final Duration expirationTimeLimit;

  public AuthToken(
      String token, TravelerID userID, Instant createdTime, Duration expirationTimeLimit) {
    this.userID = userID;
    this.token = token;
    this.createdTime = createdTime;
    this.expirationTimeLimit = expirationTimeLimit;
  }

  public String getValue() {
    return this.token;
  }

  public Instant getCreatedTime() {
    return this.createdTime;
  }

  public TravelerID getUserID() {
    return this.userID;
  }

  public AuthToken copy() {
    return new AuthToken(this.token, this.userID, this.createdTime, this.expirationTimeLimit);
  }

  public boolean isExpired() {
    long elapsed = Duration.between(this.createdTime, Instant.now()).toSeconds();
    return elapsed > this.expirationTimeLimit.toSeconds();
  }

  @Override
  public boolean equals(Object o) {
    boolean equal = false;

    if (o == this) {
      equal = true;

    } else if (o instanceof AuthToken otherAuthToken) {
      equal = this.checkSameValues(otherAuthToken);
    }
    return equal;
  }

  public long getExpirationInMinutes() {
    return this.expirationTimeLimit.toMinutes();
  }

  private boolean checkSameValues(AuthToken otherAuthToken) {
    return this.token.equals(otherAuthToken.getValue())
        && this.userID.equals(otherAuthToken.getUserID())
        && this.createdTime.equals(otherAuthToken.getCreatedTime())
        && this.expirationTimeLimit.equals(otherAuthToken.expirationTimeLimit);
  }
}
