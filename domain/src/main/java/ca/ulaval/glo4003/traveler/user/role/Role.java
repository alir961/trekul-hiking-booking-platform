package ca.ulaval.glo4003.traveler.user.role;

import java.util.Set;

public abstract class Role {
  private final Set<Permission> permissions;
  private final String title;

  public Role(Set<Permission> permissions, String title) {
    this.permissions = permissions;
    this.title = title;
  }

  public boolean hasPermission(Permission permission) {
    return this.permissions.contains(permission);
  }

  public String getTitle() {
    return this.title;
  }

  public Set<Permission> getPermissions() {
    return Set.copyOf(this.permissions);
  }

  @Override
  public boolean equals(Object object) {
    if (object == null) {
      return false;
    }
    if (object.getClass() != this.getClass()) {
      return false;
    }
    final Role other = (Role) object;
    return this.title.equals(other.title);
  }

  @Override
  public int hashCode() {
    return this.title.hashCode();
  }
}
