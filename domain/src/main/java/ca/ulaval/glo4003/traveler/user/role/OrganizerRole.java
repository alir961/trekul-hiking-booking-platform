package ca.ulaval.glo4003.traveler.user.role;

import java.util.EnumSet;

public class OrganizerRole extends Role {
  private static final String TITLE = "Trip organizer";
  private static final EnumSet<Permission> PERMISSIONS =
      EnumSet.of(
          Permission.TRIP_UPDATE, Permission.HOSTING_RESERVATION, Permission.CODE_VERIFICATION);

  public OrganizerRole() {
    super(PERMISSIONS, TITLE);
  }
}
