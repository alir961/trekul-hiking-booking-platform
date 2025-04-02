package ca.ulaval.glo4003.user.assembler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ca.ulaval.glo4003.register.dto.UserDto;
import ca.ulaval.glo4003.user.request.CreateUserRequest;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CreateUserRequestAssemblerTest {
  private static final String ANY_REQUEST_FIRSTNAME = "Oli";
  private static final String ANY_REQUEST_LASTNAME = "Bond";
  private static final LocalDate ANY_REQUEST_BIRTHDAY = LocalDate.now();
  private static final String ANY_REQUEST_GENDER = "GENDER_MALE";
  private static final String ANY_REQUEST_IDUL = "idul";
  private static final String ANY_EMAIL = "ANY@EMAIL'COM";
  private static final String ANY_PASSWORD = "ANY_PASSWORD";

  private CreateUserRequest anyRegisterRequest;
  private CreateUserRequestAssembler createUserRequestAssembler;

  @BeforeEach
  void setUp() {
    this.anyRegisterRequest =
        new CreateUserRequest(
            ANY_REQUEST_FIRSTNAME,
            ANY_REQUEST_LASTNAME,
            ANY_REQUEST_BIRTHDAY,
            ANY_REQUEST_GENDER,
            ANY_REQUEST_IDUL,
            ANY_EMAIL,
            ANY_PASSWORD);
    this.createUserRequestAssembler = new CreateUserRequestAssembler();
  }

  @Test
  void givenRegisterRequest_whenAssembling_thenUserDtoAssembleCorrectly() {
    UserDto userDto = this.createUserRequestAssembler.assemble(this.anyRegisterRequest);

    assertEquals(ANY_REQUEST_FIRSTNAME, userDto.firstname());
    assertEquals(ANY_REQUEST_LASTNAME, userDto.lastname());
    assertEquals(ANY_REQUEST_BIRTHDAY, userDto.birthDay());
    assertEquals(ANY_REQUEST_GENDER, userDto.gender());
    assertEquals(ANY_REQUEST_IDUL, userDto.idul());
    assertEquals(ANY_EMAIL, userDto.email());
    assertEquals(ANY_PASSWORD, userDto.password());
  }
}
