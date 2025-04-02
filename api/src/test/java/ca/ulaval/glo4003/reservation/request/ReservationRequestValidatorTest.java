package ca.ulaval.glo4003.reservation.request;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ca.ulaval.glo4003.guest.request.GuestRequest;
import ca.ulaval.glo4003.transport.TransportType;
import ca.ulaval.glo4003.transport.exception.TransportInvalidRequestException;
import ca.ulaval.glo4003.transport.exception.TransportInvalidTypeException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ReservationRequestValidatorTest {
  private static final String ANY_VALID_TRANSPORT_TYPE = "NONE";
  private static final String ANY_INVALID_TRANSPORT_TYPE = "INVALID";
  private static final String ANY_TRANSPORT_STOP = "stop";
  private static final String ANY_GUEST_FIRSTNAME = "Alice";
  private static final String ANY_GUEST_LASTNAME = "Down the rabbit hole";
  private static final String ANY_GUEST_EMAIL = "email@ulaval.ca";
  private static final String ANY_GUEST_IDUL = "111222333";
  private static final GuestRequest ANY_GUEST =
      new GuestRequest(ANY_GUEST_FIRSTNAME, ANY_GUEST_LASTNAME, ANY_GUEST_IDUL, ANY_GUEST_EMAIL);
  private static final List<GuestRequest> ANY_GUEST_LIST_WITHIN_SIZE_LIMIT = List.of(ANY_GUEST);
  private ReservationRequestValidator validator;
  private ReservationRequest anyValidReservationRequest;

  @BeforeEach
  void setUp() {
    this.anyValidReservationRequest =
        new ReservationRequest(
            ANY_VALID_TRANSPORT_TYPE, ANY_TRANSPORT_STOP, ANY_GUEST_LIST_WITHIN_SIZE_LIMIT);
    new ReservationRequest(
        ANY_VALID_TRANSPORT_TYPE, ANY_TRANSPORT_STOP, ANY_GUEST_LIST_WITHIN_SIZE_LIMIT);
    this.validator = new ReservationRequestValidator();
  }

  @Test
  void givenValidReservationRequest_whenValidating_thenDoesNotThrowException() {
    assertDoesNotThrow(() -> this.validator.validate(this.anyValidReservationRequest));
  }

  @Test
  void givenInvalidTransportReservationRequest_whenValidating_thenThrowException() {
    ReservationRequest invalidReservationRequest =
        new ReservationRequest(
            ANY_INVALID_TRANSPORT_TYPE, ANY_TRANSPORT_STOP, ANY_GUEST_LIST_WITHIN_SIZE_LIMIT);

    assertThrows(
        TransportInvalidTypeException.class,
        () -> this.validator.validate(invalidReservationRequest));
  }

  @Test
  void givenOrganizedBusTransportWithoutSpecifiedStopRequest_whenValidating_thenThrowException() {
    ReservationRequest invalidReservationRequest =
        new ReservationRequest(
            TransportType.ORGANIZED_BUS.toString(), null, ANY_GUEST_LIST_WITHIN_SIZE_LIMIT);

    assertThrows(
        TransportInvalidRequestException.class,
        () -> this.validator.validate(invalidReservationRequest));
  }
}
