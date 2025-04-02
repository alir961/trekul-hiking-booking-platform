package ca.ulaval.glo4003.profile;

import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.user.User;
import ca.ulaval.glo4003.traveler.user.UserRepository;
import ca.ulaval.glo4003.traveler.user.exception.UserNotFoundException;
import ca.ulaval.glo4003.traveler.user.profile.Profile;

public class ProfileService {
  private final UserRepository userRepository;

  public ProfileService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public Profile getProfile(String userID) {
    TravelerID travelerID = new TravelerID(userID);
    User user = this.userRepository.getById(travelerID).orElseThrow(UserNotFoundException::new);

    return user.getProfile();
  }

  public Money getUserCredits(String userID) {
    TravelerID travelerID = new TravelerID(userID);
    User user = this.userRepository.getById(travelerID).orElseThrow(UserNotFoundException::new);

    return user.getCredits();
  }
}
