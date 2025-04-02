package ca.ulaval.glo4003.reservation.assembler;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.group.Group;
import ca.ulaval.glo4003.group.assembler.GroupResponseAssembler;
import ca.ulaval.glo4003.group.reponse.GroupResponse;
import ca.ulaval.glo4003.guest.assembler.GuestResponseAssembler;
import ca.ulaval.glo4003.guest.response.GuestResponse;
import ca.ulaval.glo4003.traveler.guest.Guest;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GroupResponseAssemblerTest {
  private static final String ANY_ID = "123456";
  private static final String ANY_NAME = "name";
  private static final String ANY_EMAIL = "email@ulaval.ca";
  private static final GuestResponse ANY_GUEST_RESPONSE =
      new GuestResponse(ANY_ID, ANY_NAME, ANY_EMAIL);
  private Group groupMock;
  private Guest guestMock;
  private GuestResponseAssembler guestResponseAssemblerMock;
  private GroupResponseAssembler groupResponseAssembler;

  @BeforeEach
  void setUp() {
    this.groupMock = mock(Group.class);
    this.guestMock = mock(Guest.class);
    this.guestResponseAssemblerMock = mock(GuestResponseAssembler.class);
    this.groupResponseAssembler = new GroupResponseAssembler(this.guestResponseAssemblerMock);
  }

  @Test
  void whenAssemblingGroupResponse_thenGroupResponseAssembleCorrectly() {
    List<Guest> mockedGuestList = List.of(this.guestMock);
    List<GuestResponse> mockedGuestResponseList = List.of(ANY_GUEST_RESPONSE);
    when(this.groupMock.getGuests()).thenReturn(mockedGuestList);
    when(this.guestResponseAssemblerMock.assemble(mockedGuestList))
        .thenReturn(mockedGuestResponseList);

    GroupResponse assembledGroupResponse = this.groupResponseAssembler.assemble(this.groupMock);

    assertEquals(mockedGuestResponseList, assembledGroupResponse.guests());
  }
}
