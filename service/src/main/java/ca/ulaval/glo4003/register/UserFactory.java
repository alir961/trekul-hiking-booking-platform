package ca.ulaval.glo4003.register;

import ca.ulaval.glo4003.register.dto.UserDto;
import ca.ulaval.glo4003.traveler.PersonName;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.user.Gender;
import ca.ulaval.glo4003.traveler.user.ProfileValidator;
import ca.ulaval.glo4003.traveler.user.User;
import ca.ulaval.glo4003.traveler.user.credential.UserCredential;
import ca.ulaval.glo4003.traveler.user.profile.Profile;
import ca.ulaval.glo4003.traveler.user.role.RolesCollection;

public class UserFactory {

  private final ProfileValidator profileValidator;
  private final UserCredentialFactory userCredentialFactory;

  public UserFactory(
      ProfileValidator profileValidator, UserCredentialFactory userCredentialFactory) {
    this.profileValidator = profileValidator;
    this.userCredentialFactory = userCredentialFactory;
  }

  public User create(UserDto dto) {
    TravelerID userID = new TravelerID(dto.idul());
    Gender genderValue = Gender.valueOf(dto.gender());
    UserCredential userCredential = this.userCredentialFactory.create(dto.email(), dto.password());
    PersonName personName = new PersonName(dto.firstname(), dto.lastname());
    Profile profile = new Profile(personName, dto.birthDay(), genderValue, userID, userCredential);
    this.profileValidator.validate(profile);

    return new User(profile, new RolesCollection());
  }
}
