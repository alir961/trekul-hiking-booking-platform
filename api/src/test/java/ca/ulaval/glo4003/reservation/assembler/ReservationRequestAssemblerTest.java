package ca.ulaval.glo4003.reservation.assembler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.guest.assembler.GuestRequestAssembler;
import ca.ulaval.glo4003.guest.request.GuestRequest;
import ca.ulaval.glo4003.reservation.dto.ReservationDto;
import ca.ulaval.glo4003.reservation.request.ReservationRequest;
import ca.ulaval.glo4003.reservation.request.ReservationRequestValidator;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.guest.dto.GuestDto;
import ca.ulaval.glo4003.trip.TripID;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ReservationRequestAssemblerTest {
  private static final TravelerID ANY_USER_ID = new TravelerID("555555555");
  private static final TripID ANY_TRIP_ID = TripID.generate();
  private static final String ANY_VALID_TRANSPORT_TYPE = "NONE";
  private static final String ANY_BUS_STOP = "busStop";
  private static final String ANY_GUEST_FIRSTNAME = "Alice";
  private static final String ANY_GUEST_LASTNAME = "Slept too long";
  private static final String ANY_GUEST_EMAIL = "email@ulaval.ca";
  private static final String ANY_GUEST_IDUL = "111222333";
  private static final GuestRequest ANY_GUEST_REQUEST =
      new GuestRequest(ANY_GUEST_FIRSTNAME, ANY_GUEST_LASTNAME, ANY_GUEST_IDUL, ANY_GUEST_EMAIL);
  private static final List<GuestRequest> ANY_GUEST_REQUEST_LIST = List.of(ANY_GUEST_REQUEST);
  private static final GuestDto ANY_GUEST_DTO =
      new GuestDto(ANY_GUEST_FIRSTNAME, ANY_GUEST_LASTNAME, ANY_GUEST_IDUL, ANY_GUEST_EMAIL);
  private static final List<GuestDto> ANY_GUEST_DTO_LIST = List.of(ANY_GUEST_DTO);
  private static final ReservationRequest ANY_RESERVATION_REQUEST =
      new ReservationRequest(ANY_VALID_TRANSPORT_TYPE, ANY_BUS_STOP, ANY_GUEST_REQUEST_LIST);
  private ReservationRequestValidator validatorMock;
  private GuestRequestAssembler guestRequestAssemblerMock;
  private ReservationRequestAssembler reservationRequestAssembler;

  @BeforeEach
  void setUp() {
    this.validatorMock = mock(ReservationRequestValidator.class);
    this.guestRequestAssemblerMock = mock(GuestRequestAssembler.class);
    when(this.guestRequestAssemblerMock.assemble(ANY_GUEST_REQUEST_LIST))
        .thenReturn(ANY_GUEST_DTO_LIST);
    this.reservationRequestAssembler =
        new ReservationRequestAssembler(this.validatorMock, this.guestRequestAssemblerMock);
  }

  @Test
  void givenValidRequest_whenAssembling_thenReservationDtoAssembleCorrectly() {
    ReservationDto reservationDto =
        this.reservationRequestAssembler.assemble(
            ANY_RESERVATION_REQUEST, ANY_USER_ID, ANY_TRIP_ID);

    assertEquals(ANY_TRIP_ID, reservationDto.tripID());
    assertEquals(ANY_VALID_TRANSPORT_TYPE, reservationDto.transportType().toString());
    assertEquals(ANY_USER_ID.toString(), reservationDto.userID().toString());
    assertEquals(ANY_GUEST_DTO_LIST, reservationDto.guests());
    assertEquals(1, reservationDto.guests().size());
  }

  @Test
  void whenAssemblingRequest_thenValidatorValidatesRequest() {
    this.reservationRequestAssembler.assemble(ANY_RESERVATION_REQUEST, ANY_USER_ID, ANY_TRIP_ID);

    verify(this.validatorMock).validate(ANY_RESERVATION_REQUEST);
  }
}
