package ca.ulaval.glo4003.traveler.user;

import ca.ulaval.glo4003.traveler.TravelerID;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
  Optional<User> getById(TravelerID userId);

  Optional<User> getByEmail(String email);

  List<User> getBunch(List<TravelerID> bunch);

  void save(User user);

  void save(Collection<User> users);

  void remove(User user);

  List<User> getAll();
}
