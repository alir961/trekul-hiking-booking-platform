package ca.ulaval.glo4003.token;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.clock.Clock;
import ca.ulaval.glo4003.traveler.TravelerID;
import java.time.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UuidAuthTokenFactoryTest {
  private static final Duration ANY_EXPIRATION_LIMIT = Duration.ofSeconds(501);
  private Clock spyClock;
  private UuidAuthTokenFactory uuidAuthTokenFactory;

  @BeforeEach
  void setUp() {
    this.spyClock = mock(Clock.class);
    this.uuidAuthTokenFactory = new UuidAuthTokenFactory(this.spyClock, ANY_EXPIRATION_LIMIT);
  }

  @Test
  void whenCreatingToken_thenTokenIsGeneratedFromNow() {
    TravelerID anyUserID = new TravelerID("7102,fk");
    this.uuidAuthTokenFactory.createToken(anyUserID);

    verify(this.spyClock).now();
  }

  @Test
  void whenCreatingToken_thenTokenHasUserID() {
    TravelerID anUserID = new TravelerID("3u5nhfdnb");
    AuthToken token = this.uuidAuthTokenFactory.createToken(anUserID);

    assertEquals(anUserID, token.getUserID());
  }
}
