package ca.ulaval.glo4003.guest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.traveler.guest.dto.GuestDto;
import ca.ulaval.glo4003.traveler.user.credential.EmailValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GuestFactoryTest {
  private static final String ANY_GUEST_FIRSTNAME = "Alice";
  private static final String ANY_GUEST_LASTNAME = "Wonderland";
  private static final String ANY_GUEST_IDUL = "111222333";
  private static final String ANY_GUEST_EMAIL = "email@ulaval.ca";
  private static final GuestDto ANY_GUEST_DTO =
      new GuestDto(ANY_GUEST_FIRSTNAME, ANY_GUEST_LASTNAME, ANY_GUEST_IDUL, ANY_GUEST_EMAIL);
  private EmailValidator emailValidatorMock;
  private GuestFactory guestFactory;

  @BeforeEach
  void setUp() {
    this.emailValidatorMock = mock(EmailValidator.class);
    this.guestFactory = new GuestFactory(this.emailValidatorMock);
  }

  @Test
  void whenCreatingGuest_thenEmailValidatorValidatesEachEmail() {
    this.guestFactory.createGuest(ANY_GUEST_DTO);

    verify(this.emailValidatorMock).validate(ANY_GUEST_EMAIL);
  }
}
