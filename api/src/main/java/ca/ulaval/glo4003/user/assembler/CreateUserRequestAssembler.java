package ca.ulaval.glo4003.user.assembler;

import ca.ulaval.glo4003.register.dto.UserDto;
import ca.ulaval.glo4003.user.request.CreateUserRequest;

public class CreateUserRequestAssembler {

  public UserDto assemble(CreateUserRequest registerRequest) {
    return new UserDto(
        registerRequest.firstname(),
        registerRequest.lastname(),
        registerRequest.birthDay(),
        registerRequest.gender().toUpperCase(),
        registerRequest.idul(),
        registerRequest.email(),
        registerRequest.password());
  }
}
