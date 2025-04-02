package ca.ulaval.glo4003.etl.user;

import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.etl.Extractor;
import ca.ulaval.glo4003.etl.exception.ExtractException;
import ca.ulaval.glo4003.register.dto.UserDto;
import ca.ulaval.glo4003.traveler.user.role.Role;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserEtlExecutorTest {
  private static final Role ANY_ROLE = mock(Role.class);
  private static final Collection<UserDto> ANY_EXTRACTED_USER_DTO = List.of(mock(UserDto.class));
  private UserLoader userLoaderMock;
  private UserEtlExecutor userEtlExecutor;

  @BeforeEach
  void setUp() throws ExtractException {
    this.userLoaderMock = mock(UserLoader.class);
    Extractor<UserDto> userDtoExtractorMock = mock(Extractor.class);
    when(userDtoExtractorMock.extract()).thenReturn(ANY_EXTRACTED_USER_DTO);

    this.userEtlExecutor = new UserEtlExecutor(userDtoExtractorMock, this.userLoaderMock, ANY_ROLE);
  }

  @Test
  void whenExecuting_thenLoadUserFromExtractorWithAssignedRole() throws ExtractException {
    this.userEtlExecutor.execute();

    verify(this.userLoaderMock).load(ANY_EXTRACTED_USER_DTO, ANY_ROLE);
  }
}
