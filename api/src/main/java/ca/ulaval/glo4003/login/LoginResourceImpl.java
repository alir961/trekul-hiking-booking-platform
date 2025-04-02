package ca.ulaval.glo4003.login;

import ca.ulaval.glo4003.login.assembler.LoginResponseAssembler;
import ca.ulaval.glo4003.login.request.LoginRequest;
import ca.ulaval.glo4003.login.response.LoginResponse;
import ca.ulaval.glo4003.token.AuthToken;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/trekul/login")
public class LoginResourceImpl implements LoginResource {

  private final LoginService loginService;
  private final LoginResponseAssembler loginResponseAssembler;

  @Inject
  public LoginResourceImpl(
      LoginService loginService, LoginResponseAssembler loginResponseAssembler) {
    this.loginService = loginService;
    this.loginResponseAssembler = loginResponseAssembler;
  }

  @Override
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response login(LoginRequest loginRequest) {
    AuthToken token =
        this.loginService.loginWithEmailAndPassword(loginRequest.email(), loginRequest.password());
    LoginResponse loginResponse = this.loginResponseAssembler.assemble(token);

    return Response.ok(loginResponse).build();
  }
}
