package ca.ulaval.glo4003.user.assembler;

import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.traveler.user.profile.Profile;
import ca.ulaval.glo4003.user.response.ProfileResponse;

public class ProfileResponseAssembler {
  public ProfileResponse assemble(Profile profile, Money credits) {
    String gender = profile.gender().toString();
    return new ProfileResponse(
        profile.name().toString(),
        profile.birthDate(),
        gender,
        profile.idul().toString(),
        profile.getEmail(),
        profile.calculateAge(),
        credits.toString());
  }
}
