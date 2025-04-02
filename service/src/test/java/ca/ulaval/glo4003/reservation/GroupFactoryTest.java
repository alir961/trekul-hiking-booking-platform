package ca.ulaval.glo4003.reservation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.group.Group;
import ca.ulaval.glo4003.group.GroupValidator;
import ca.ulaval.glo4003.guest.GuestFactory;
import ca.ulaval.glo4003.traveler.PersonName;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.guest.Guest;
import ca.ulaval.glo4003.traveler.guest.dto.GuestDto;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GroupFactoryTest {
  private static final TravelerID ANY_USER_ID = new TravelerID("555555555");
  private static final String ANY_GUEST_FIRSTNAME = "Alice";
  private static final String ANY_GUEST_LASTNAME = "Wonderland";
  private static final PersonName ANY_GUEST_FULLNAME =
      new PersonName(ANY_GUEST_FIRSTNAME, ANY_GUEST_LASTNAME);
  private static final String ANY_GUEST_IDUL = "111222333";
  private static final String ANY_GUEST_EMAIL = "email@ulaval.ca";
  private static final GuestDto ANY_GUEST_DTO =
      new GuestDto(ANY_GUEST_FIRSTNAME, ANY_GUEST_LASTNAME, ANY_GUEST_IDUL, ANY_GUEST_EMAIL);
  private static final List<GuestDto> ANY_GUEST_DTO_LIST = List.of(ANY_GUEST_DTO);
  private static final TravelerID ANY_GUEST_ID = new TravelerID(ANY_GUEST_IDUL);
  private static final Guest ANY_GUEST =
      new Guest(ANY_GUEST_FULLNAME, ANY_GUEST_ID, ANY_GUEST_EMAIL);
  private GroupValidator groupValidatorMock;
  private GroupFactory groupFactory;

  @BeforeEach
  void setUp() {
    GuestFactory guestFactoryMock = mock(GuestFactory.class);
    this.groupValidatorMock = mock(GroupValidator.class);
    this.groupFactory = new GroupFactory(this.groupValidatorMock, guestFactoryMock);

    when(guestFactoryMock.createGuest(ANY_GUEST_DTO)).thenReturn(ANY_GUEST);
  }

  @Test
  void whenCreatingGroup_thenGroupWithCorrectFieldsIsReturned() {
    Group createdGroup = this.groupFactory.create(ANY_USER_ID, ANY_GUEST_DTO_LIST);

    assertEquals(1, createdGroup.getNumberOfGuests());
    assertEquals(ANY_GUEST, createdGroup.getGuests().getFirst());
  }

  @Test
  void whenCreatingGroup_thenGroupValidatorValidatesCreatedGroup() {
    Group createdGroup = this.groupFactory.create(ANY_USER_ID, ANY_GUEST_DTO_LIST);

    verify(this.groupValidatorMock).validate(createdGroup);
  }
}
