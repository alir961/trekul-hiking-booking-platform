package ca.ulaval.glo4003.token;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ca.ulaval.glo4003.traveler.TravelerID;
import java.time.Duration;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AuthTokenTest {
  private static final TravelerID ANY_USER_ID = new TravelerID("lfks091");
  private static final Instant ANY_DATE_TIME = Instant.ofEpochMilli(17334612);
  private static final String ANY_TOKEN_VALUE = "tok01294";
  private static final Duration ANY_EXPIRATION_LIMIT = Duration.ofSeconds(304);
  private AuthToken anyToken;

  @BeforeEach
  void setUp() {
    this.anyToken =
        new AuthToken(ANY_TOKEN_VALUE, ANY_USER_ID, ANY_DATE_TIME, ANY_EXPIRATION_LIMIT);
  }

  @Test
  void whenGettingUserID_thenCorrectUserIDIsReturned() {
    TravelerID retrievedUserId = this.anyToken.getUserID();

    assertEquals(ANY_USER_ID, retrievedUserId);
  }

  @Test
  void whenGettingTokenCreatedTime_thenCorrectCreatedTimeIsReturned() {
    Instant tokenCreatedTime = this.anyToken.getCreatedTime();

    assertEquals(ANY_DATE_TIME, tokenCreatedTime);
  }

  @Test
  void whenGettingValue_thenCorrectTokenValueIsReturned() {
    String retrievedTokenValue = this.anyToken.getValue();

    assertEquals(ANY_TOKEN_VALUE, retrievedTokenValue);
  }
}
