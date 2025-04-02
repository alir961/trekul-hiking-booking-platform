package ca.ulaval.glo4003.etl.user;

import ca.ulaval.glo4003.etl.Extractor;
import ca.ulaval.glo4003.etl.exception.ExtractException;
import ca.ulaval.glo4003.register.dto.UserDto;
import ca.ulaval.glo4003.traveler.user.Gender;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public class LocalStaticUserExtractor implements Extractor<UserDto> {
  private static final UserDto PAUL =
      new UserDto(
          "Paul",
          "Hardcoded For Demo",
          LocalDate.now().minusYears(30),
          Gender.GENDER_MAN.toString(),
          "PAU356",
          "paul@email.com",
          "123456789!A");

  @Override
  public Collection<UserDto> extract() throws ExtractException {
    return List.of(PAUL);
  }
}
