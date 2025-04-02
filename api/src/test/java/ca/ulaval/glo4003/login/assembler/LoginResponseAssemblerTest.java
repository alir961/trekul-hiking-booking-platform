package ca.ulaval.glo4003.login.assembler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ca.ulaval.glo4003.login.response.LoginResponse;
import ca.ulaval.glo4003.token.AuthToken;
import ca.ulaval.glo4003.traveler.TravelerID;
import java.time.Duration;
import java.time.Instant;
import org.junit.jupiter.api.Test;

public class LoginResponseAssemblerTest {
  private static final String ANY_TOKEN_AS_STR = "123";
  private static final TravelerID ANY_USER_ID = new TravelerID("456");
  private static final Instant ANY_CREATION_TIME = Instant.ofEpochMilli(17334612);
  private static final Duration ANY_EXPIRATION_LIMIT = Duration.ofSeconds(631);
  private static final AuthToken ANY_AUTH_TOKEN =
      new AuthToken(ANY_TOKEN_AS_STR, ANY_USER_ID, ANY_CREATION_TIME, ANY_EXPIRATION_LIMIT);
  private final LoginResponseAssembler loginResponseAssembler = new LoginResponseAssembler();

  @Test
  void whenAssemblingLoginResponse_thenLoginResponseAssembleCorrectly() {
    LoginResponse assembledResponse = this.loginResponseAssembler.assemble(ANY_AUTH_TOKEN);

    assertEquals(ANY_TOKEN_AS_STR, assembledResponse.token());
  }
}
