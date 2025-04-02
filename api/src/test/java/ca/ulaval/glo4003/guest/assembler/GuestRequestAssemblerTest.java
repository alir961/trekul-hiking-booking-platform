package ca.ulaval.glo4003.guest.assembler;

import static org.junit.jupiter.api.Assertions.*;

import ca.ulaval.glo4003.guest.request.GuestRequest;
import ca.ulaval.glo4003.traveler.guest.dto.GuestDto;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GuestRequestAssemblerTest {
  private static final String ANY_GUEST_FIRSTNAME = "Alice";
  private static final String ANY_GUEST_LASTNAME = "In Borderlands";
  private static final String ANY_GUEST_IDUL = "111111111";
  private static final String ANY_GUEST_EMAIL = "email1@ulaval.ca";
  private static final GuestRequest ANY_GUEST_REQUEST =
      new GuestRequest(ANY_GUEST_FIRSTNAME, ANY_GUEST_LASTNAME, ANY_GUEST_IDUL, ANY_GUEST_EMAIL);
  private static final List<GuestRequest> ANY_GUEST_REQUEST_LIST = List.of(ANY_GUEST_REQUEST);
  private static final GuestDto ANY_GUEST_DTO =
      new GuestDto(ANY_GUEST_FIRSTNAME, ANY_GUEST_LASTNAME, ANY_GUEST_IDUL, ANY_GUEST_EMAIL);
  private static final List<GuestDto> ANY_GUEST_DTO_LIST = List.of(ANY_GUEST_DTO);
  private GuestRequestAssembler guestRequestAssembler;

  @BeforeEach
  void setUp() {
    this.guestRequestAssembler = new GuestRequestAssembler();
  }

  @Test
  void whenAssembling_thenGuestDtoAssembleCorrectly() {
    List<GuestDto> guestDto = this.guestRequestAssembler.assemble(ANY_GUEST_REQUEST_LIST);

    assertEquals(ANY_GUEST_REQUEST_LIST.size(), guestDto.size());
    assertEquals(ANY_GUEST_DTO_LIST, guestDto);
  }
}
