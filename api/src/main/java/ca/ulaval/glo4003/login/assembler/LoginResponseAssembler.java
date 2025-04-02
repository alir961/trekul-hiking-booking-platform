package ca.ulaval.glo4003.login.assembler;

import ca.ulaval.glo4003.login.response.LoginResponse;
import ca.ulaval.glo4003.token.AuthToken;

public class LoginResponseAssembler {

  public LoginResponse assemble(AuthToken token) {
    return new LoginResponse(token.getValue());
  }
}
