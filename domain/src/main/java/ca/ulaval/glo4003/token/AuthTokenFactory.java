package ca.ulaval.glo4003.token;

import ca.ulaval.glo4003.traveler.TravelerID;

public interface AuthTokenFactory {
  AuthToken createToken(TravelerID userID);
}
