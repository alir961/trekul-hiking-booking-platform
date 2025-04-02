package ca.ulaval.glo4003.user;

import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.user.User;
import ca.ulaval.glo4003.traveler.user.UserRepository;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryUserRepositoryImpl implements UserRepository {
  private final Map<TravelerID, User> userStorageId = Collections.synchronizedMap(new HashMap<>());
  private final Map<String, User> userStorageEmail = Collections.synchronizedMap(new HashMap<>());

  @Override
  public Optional<User> getById(TravelerID userId) {
    User found = this.userStorageId.get(userId);

    if (found != null) {
      return Optional.of(found.copy());
    }

    return Optional.empty();
  }

  @Override
  public Optional<User> getByEmail(String email) {
    User found = this.userStorageEmail.get(email);

    if (found != null) {
      return Optional.of(found.copy());
    }

    return Optional.empty();
  }

  @Override
  public List<User> getBunch(List<TravelerID> bunch) {
    if (bunch == null || bunch.isEmpty()) {
      return List.of();
    }
    return bunch.stream()
        .map(this.userStorageId::get)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  @Override
  public void save(User user) {
    this.userStorageId.put(user.getId(), user.copy());
    this.userStorageEmail.put(user.getEmail(), user.copy());
  }

  @Override
  public void save(Collection<User> users) {
    for (User user : users) {
      this.save(user);
    }
  }

  @Override
  public void remove(User user) {
    this.userStorageId.remove(user.getId());
    this.userStorageEmail.remove(user.getEmail());
  }

  @Override
  public List<User> getAll() {
    synchronized (this.userStorageId) {
      return new ArrayList<>(this.userStorageId.values());
    }
  }
}
