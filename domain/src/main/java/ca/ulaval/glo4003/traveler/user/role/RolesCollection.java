package ca.ulaval.glo4003.traveler.user.role;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class RolesCollection {
  private final Set<Role> roles = new HashSet<>();

  public boolean hasPermission(Permission permission) {
    return this.roles.stream().anyMatch(role -> role.hasPermission(permission));
  }

  public void addRole(Role role) {
    this.roles.add(role);
  }

  public Collection<String> gatherTitles() {
    return this.roles.stream().map(Role::getTitle).toList();
  }

  public Collection<Permission> gatherPermissions() {
    return this.roles.stream()
        .flatMap(role -> role.getPermissions().stream())
        .collect(Collectors.toSet());
  }

  public RolesCollection copy() {
    RolesCollection copied = new RolesCollection();
    copied.roles.addAll(this.roles);

    return copied;
  }
}
