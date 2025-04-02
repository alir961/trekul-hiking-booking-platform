package ca.ulaval.glo4003.etl.user;

import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.register.UserFactory;
import ca.ulaval.glo4003.register.dto.UserDto;
import ca.ulaval.glo4003.traveler.user.User;
import ca.ulaval.glo4003.traveler.user.UserRepository;
import ca.ulaval.glo4003.traveler.user.role.Role;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

public class UserLoaderTest {
  private static final Role ANY_ROLE = mock(Role.class);
  private static final UserDto ANY_USER_DTO = mock(UserDto.class);
  private User userMock;
  private UserRepository userRepositoryMock;
  private UserFactory userFactoryMock;
  private UserLoader userLoader;

  @BeforeEach
  void setUp() {
    this.userRepositoryMock = mock(UserRepository.class);
    this.userFactoryMock = mock(UserFactory.class);
    this.userLoader = new UserLoader(this.userRepositoryMock, this.userFactoryMock);

    this.userMock = this.mockUserFromUserDto(ANY_USER_DTO);
  }

  @Test
  void givenMultipleUserDto_whenLoading_thenSaveAllNewUsers() {
    UserDto anotherUserDtoMock = mock(UserDto.class);
    User anotherUserMock = this.mockUserFromUserDto(anotherUserDtoMock);
    this.userLoader.load(List.of(ANY_USER_DTO, anotherUserDtoMock), ANY_ROLE);

    verify(this.userRepositoryMock).save(this.userMock);
    verify(this.userRepositoryMock).save(anotherUserMock);
  }

  @Test
  void whenLoading_thenAddRoleBeforeSaving() {
    this.userLoader.load(List.of(ANY_USER_DTO), ANY_ROLE);
    InOrder order = inOrder(this.userMock, this.userRepositoryMock);

    order.verify(this.userMock).addRole(ANY_ROLE);
    order.verify(this.userRepositoryMock).save(this.userMock);
  }

  private User mockUserFromUserDto(UserDto userDto) {
    User newUserMock = mock(User.class);
    when(this.userFactoryMock.create(userDto)).thenReturn(newUserMock);

    return newUserMock;
  }
}
