package ca.ulaval.glo4003.traveler.user.role;

import static ca.ulaval.glo4003.traveler.user.role.Permission.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collection;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RolesCollectionTest {
  private static final Permission MOCKED_ROLE_PERMISSION = ROLE_GRANT;
  private static final String ANY_TITLE_ONE = "any title 1";
  private static final String ANY_TITLE_TWO = "any title 2";
  private RolesCollection rolesCollection;

  @BeforeEach
  void setUp() {
    this.rolesCollection = new RolesCollection();
  }

  @Test
  void givenTwoRolesWithSamePermission_whenGatheringPermissions_thenGetNoDuplicate() {
    this.rolesCollection.addRole(this.mockRoleOfOnePermission(ANY_TITLE_ONE));
    this.rolesCollection.addRole(this.mockRoleOfOnePermission(ANY_TITLE_TWO));

    Collection<Permission> permissions = this.rolesCollection.gatherPermissions();

    assertEquals(1, permissions.size());
    assertTrue(permissions.contains(MOCKED_ROLE_PERMISSION));
  }

  @Test
  void givenTwoRoles_whenGatheringTitles_thenGetTitlesOfAllRoles() {
    this.rolesCollection.addRole(this.mockRoleOfOnePermission(ANY_TITLE_ONE));
    this.rolesCollection.addRole(this.mockRoleOfOnePermission(ANY_TITLE_TWO));

    Collection<String> titles = this.rolesCollection.gatherTitles();

    assertEquals(2, titles.size());
    assertTrue(titles.contains(ANY_TITLE_ONE));
    assertTrue(titles.contains(ANY_TITLE_TWO));
  }

  @Test
  void givenRoleWithTargetPermission_whenCallingHasPermission_thenReturnTrue() {
    this.rolesCollection.addRole(this.mockRoleOfOnePermission(ANY_TITLE_ONE));
    boolean result = this.rolesCollection.hasPermission(MOCKED_ROLE_PERMISSION);

    assertTrue(result);
  }

  @Test
  void givenNoRoles_whenCallingHasPermission_thenReturnFalse() {
    boolean result = this.rolesCollection.hasPermission(MOCKED_ROLE_PERMISSION);

    assertFalse(result);
  }

  @Test
  void givenMultipleRoles_whenCallingHasPermission_thenVerifyAllOfThem() {
    Permission anotherPermission = ROLE_REMOVE;
    Role roleOne = this.mockRoleOfOnePermission(ANY_TITLE_ONE);
    Role roleTwo = this.mockRoleOfOnePermission(ANY_TITLE_TWO);
    this.rolesCollection.addRole(roleOne);
    this.rolesCollection.addRole(roleTwo);
    this.rolesCollection.hasPermission(anotherPermission);

    verify(roleOne).hasPermission(anotherPermission);
    verify(roleTwo).hasPermission(anotherPermission);
  }

  @Test
  void whenCopying_thenNewRolesInCopyAreNotInOriginal() {
    RolesCollection copied = this.rolesCollection.copy();
    copied.addRole(this.mockRoleOfOnePermission(ANY_TITLE_ONE));

    Collection<String> originalTitles = this.rolesCollection.gatherTitles();
    Collection<String> copiedTitles = copied.gatherTitles();

    assertNotEquals(originalTitles, copiedTitles);
  }

  @Test
  void givenTwoRoles_whenCopying_thenRolesAreTheSameInCopy() {
    this.rolesCollection.addRole(this.mockRoleOfOnePermission(ANY_TITLE_ONE));
    this.rolesCollection.addRole(this.mockRoleOfOnePermission(ANY_TITLE_TWO));

    Collection<String> originalTitles = this.rolesCollection.gatherTitles();
    Collection<String> copiedTitles = this.rolesCollection.copy().gatherTitles();

    assertEquals(originalTitles, copiedTitles);
  }

  private Role mockRoleOfOnePermission(String title) {
    Role role = mock(Role.class);
    when(role.getTitle()).thenReturn(title);
    when(role.getPermissions()).thenReturn(Set.of(MOCKED_ROLE_PERMISSION));
    when(role.hasPermission(MOCKED_ROLE_PERMISSION)).thenReturn(true);

    return role;
  }
}
