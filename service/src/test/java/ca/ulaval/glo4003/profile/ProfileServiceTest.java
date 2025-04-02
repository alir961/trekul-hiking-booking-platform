package ca.ulaval.glo4003.profile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.user.User;
import ca.ulaval.glo4003.traveler.user.UserRepository;
import ca.ulaval.glo4003.traveler.user.exception.UserNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProfileServiceTest {
  private static final TravelerID ANY_USER_ID = new TravelerID("10xxx9932");
  private User userMock;
  private UserRepository mockUserRepositoryMock;
  private ProfileService profileService;

  @BeforeEach
  void setUp() {
    this.userMock = mock(User.class);
    this.mockUserRepositoryMock = mock(UserRepository.class);

    this.profileService = new ProfileService(this.mockUserRepositoryMock);
  }

  @Test
  void givenUserNotInRepository_whenGettingProfile_thenThrowUserNotFoundException() {
    when(this.mockUserRepositoryMock.getById(ANY_USER_ID)).thenReturn(Optional.empty());

    assertThrows(
        UserNotFoundException.class, () -> this.profileService.getProfile(ANY_USER_ID.toString()));
  }

  @Test
  void givenUserInRepository_whenGettingProfile_thenUserGetsProfile() {
    when(this.mockUserRepositoryMock.getById(ANY_USER_ID)).thenReturn(Optional.of(this.userMock));

    this.profileService.getProfile(ANY_USER_ID.toString());

    verify(this.userMock).getProfile();
  }

  @Test
  void givenUserNotInRepository_whenGettingCredits_thenThrowUserNotFoundException() {
    when(this.mockUserRepositoryMock.getById(ANY_USER_ID)).thenReturn(Optional.empty());

    assertThrows(
        UserNotFoundException.class,
        () -> this.profileService.getUserCredits(ANY_USER_ID.toString()));
  }

  @Test
  void givenUserInRepository_whenGettingCredits_thenUserGetsCredits() {
    when(this.mockUserRepositoryMock.getById(ANY_USER_ID)).thenReturn(Optional.of(this.userMock));

    this.profileService.getUserCredits(ANY_USER_ID.toString());

    verify(this.userMock).getCredits();
  }
}
