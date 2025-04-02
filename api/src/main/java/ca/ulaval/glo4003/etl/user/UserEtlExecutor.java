package ca.ulaval.glo4003.etl.user;

import ca.ulaval.glo4003.etl.EtlExecutor;
import ca.ulaval.glo4003.etl.Extractor;
import ca.ulaval.glo4003.etl.exception.ExtractException;
import ca.ulaval.glo4003.register.dto.UserDto;
import ca.ulaval.glo4003.traveler.user.role.Role;

public class UserEtlExecutor implements EtlExecutor {
  private final Extractor<UserDto> userDtoExtractor;
  private final UserLoader userLoader;
  private final Role role;

  public UserEtlExecutor(Extractor<UserDto> userDtoExtractor, UserLoader userLoader, Role role) {
    this.userDtoExtractor = userDtoExtractor;
    this.userLoader = userLoader;
    this.role = role;
  }

  @Override
  public void execute() throws ExtractException {
    this.userLoader.load(this.userDtoExtractor.extract(), this.role);
  }
}
