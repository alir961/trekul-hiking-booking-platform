package ca.ulaval.glo4003.reservation;

import static ca.ulaval.glo4003.invoice.BillableItems.TRANSPORT;
import static ca.ulaval.glo4003.reservation.ReservationStatus.CANCELLED;
import static ca.ulaval.glo4003.reservation.ReservationStatus.CONFIRMED;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.confirmationcode.ConfirmationCode;
import ca.ulaval.glo4003.group.Group;
import ca.ulaval.glo4003.invoice.TripInvoice;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.payment.PaymentInformation;
import ca.ulaval.glo4003.payment.PaymentProcessor;
import ca.ulaval.glo4003.reservation.exception.ReservationInvalidStatusException;
import ca.ulaval.glo4003.transport.TransportType;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.trip.TripID;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ReservationTest {
  private static final TransportType ANY_TRANSPORT_TYPE = TransportType.ORGANIZED_BUS;
  private static final String ANY_TRANSPORT_STOP = "stop";
  private static final ZonedDateTime NOW = ZonedDateTime.now();
  private static final Money ANY_COST = new Money(5.0);
  private static final TravelerID ANY_USER_ID = new TravelerID("5555");
  private static final TravelerID ANY_PAYER_ID = new TravelerID("payerID");
  private static final TripID ANY_TRIP_ID = TripID.generate();
  private static final ReservationID ANY_RESERVATION_ID = ReservationID.generate();
  private static final ZonedDateTime ANY_RESERVATION_CREATION_TIME = NOW.minusMinutes(10);
  private static final ZonedDateTime ANY_FUTURE_RESERVATION_EXPIRATION_TIME = NOW.plusMinutes(3);
  private static final ZonedDateTime ANY_PASSED_RESERVATION_EXPIRATION_TIME = NOW.minusMinutes(5);
  private static final ConfirmationCode ANY_CONFIRMATION_CODE =
      new ConfirmationCode("ANY_CONFIRMATION_CODE");
  private static final ConfirmationCode ANY_OTHER_CONFIRMATION_CODE =
      new ConfirmationCode("ANY_OTHER_CONFIRMATION_CODE");
  private static final int ANY_GROUP_SIZE = 3;
  private TripInvoice tripInvoiceMock;
  private Group groupMock;
  private PaymentProcessor paymentProcessorMock;
  private PaymentInformation paymentInformationMock;

  @BeforeEach
  void setUp() {
    this.tripInvoiceMock = mock(TripInvoice.class);
    this.groupMock = mock(Group.class);
    this.paymentInformationMock = mock(PaymentInformation.class);
    this.paymentProcessorMock = mock(PaymentProcessor.class);
  }

  @Test
  void givenExpiredReservation_whenCheckingIfExpired_thenShouldReturnTrue() {
    Reservation reservationWithOldDate =
        new Reservation(
            ANY_RESERVATION_ID,
            this.tripInvoiceMock,
            this.groupMock,
            ANY_RESERVATION_CREATION_TIME,
            ANY_PASSED_RESERVATION_EXPIRATION_TIME,
            ANY_CONFIRMATION_CODE,
            ANY_TRANSPORT_STOP,
            ANY_TRANSPORT_TYPE);

    assertTrue(reservationWithOldDate.isExpired());
  }

  @Test
  void givenActiveReservation_whenCheckingIfExpired_thenShouldReturnFalse() {
    Reservation activeReservation =
        new Reservation(
            ANY_RESERVATION_ID,
            this.tripInvoiceMock,
            this.groupMock,
            ANY_RESERVATION_CREATION_TIME,
            ANY_FUTURE_RESERVATION_EXPIRATION_TIME,
            ANY_CONFIRMATION_CODE,
            ANY_TRANSPORT_STOP,
            ANY_TRANSPORT_TYPE);

    assertFalse(activeReservation.isExpired());
  }

  @Test
  void whenSettingTransport_thenSaveTransportCostToInvoice() {
    Reservation reservation = this.givenReservation();
    TransportType anyTransportType = TransportType.ORGANIZED_BUS;

    reservation.setTransportCost(ANY_COST);

    verify(this.tripInvoiceMock).saveItem(TRANSPORT, ANY_COST);
  }

  @Test
  void whenCalculatingTotalPeople_thenReturnCorrectAmountOfPeopleForReservation() {
    when(this.groupMock.calculateTotalTravelers()).thenReturn(ANY_GROUP_SIZE);
    Reservation reservation = this.givenReservation();

    int calculatedNumberOfPeople = reservation.calculateTotalTravelers();

    assertEquals(ANY_GROUP_SIZE, calculatedNumberOfPeople);
  }

  @Test
  void givenSameObject_whenCallingEquals_thenObjectIsEqual() {
    when(this.tripInvoiceMock.getUserID()).thenReturn(ANY_USER_ID);
    when(this.tripInvoiceMock.getTripID()).thenReturn(ANY_TRIP_ID);
    Reservation anyReservation =
        new Reservation(
            ANY_RESERVATION_ID,
            this.tripInvoiceMock,
            this.groupMock,
            ANY_RESERVATION_CREATION_TIME,
            ANY_FUTURE_RESERVATION_EXPIRATION_TIME,
            ANY_CONFIRMATION_CODE,
            ANY_TRANSPORT_STOP,
            ANY_TRANSPORT_TYPE);

    assertEquals(anyReservation, anyReservation);
  }

  @Test
  void givenAnotherObjectWithSameProperties_whenCallingEquals_thenObjectIsEqual() {
    when(this.tripInvoiceMock.getUserID()).thenReturn(ANY_USER_ID);
    when(this.tripInvoiceMock.getTripID()).thenReturn(ANY_TRIP_ID);
    Reservation firstReservationSameFields =
        new Reservation(
            ANY_RESERVATION_ID,
            this.tripInvoiceMock,
            this.groupMock,
            ANY_RESERVATION_CREATION_TIME,
            ANY_FUTURE_RESERVATION_EXPIRATION_TIME,
            ANY_CONFIRMATION_CODE,
            ANY_TRANSPORT_STOP,
            ANY_TRANSPORT_TYPE);
    Reservation secondReservationSameFields =
        new Reservation(
            ANY_RESERVATION_ID,
            this.tripInvoiceMock,
            this.groupMock,
            ANY_RESERVATION_CREATION_TIME,
            ANY_FUTURE_RESERVATION_EXPIRATION_TIME,
            ANY_CONFIRMATION_CODE,
            ANY_TRANSPORT_STOP,
            ANY_TRANSPORT_TYPE);

    assertEquals(firstReservationSameFields, secondReservationSameFields);
  }

  @Test
  void givenAnotherObjectWithDifferentTripID_whenCallingEquals_thenObjectIsNotEqual() {
    TripInvoice otherTripInvoiceMock = this.givenAnotherObjectWithDifferentTripId();
    Reservation reservation =
        new Reservation(
            ANY_RESERVATION_ID,
            this.tripInvoiceMock,
            this.groupMock,
            ANY_RESERVATION_CREATION_TIME,
            ANY_FUTURE_RESERVATION_EXPIRATION_TIME,
            ANY_CONFIRMATION_CODE,
            ANY_TRANSPORT_STOP,
            ANY_TRANSPORT_TYPE);
    Reservation reservationWithDifferentFields =
        new Reservation(
            ANY_RESERVATION_ID,
            otherTripInvoiceMock,
            this.groupMock,
            ANY_RESERVATION_CREATION_TIME,
            ANY_FUTURE_RESERVATION_EXPIRATION_TIME,
            ANY_CONFIRMATION_CODE,
            ANY_TRANSPORT_STOP,
            ANY_TRANSPORT_TYPE);

    assertNotEquals(reservation, reservationWithDifferentFields);
  }

  @Test
  void givenAnotherObjectWithDifferentUserID_whenCallingEquals_thenObjectIsNotEqual() {
    TripInvoice otherTripInvoiceMock = this.givenAnotherObjectWithDifferentUserId();
    Reservation reservation =
        new Reservation(
            ANY_RESERVATION_ID,
            this.tripInvoiceMock,
            this.groupMock,
            ANY_RESERVATION_CREATION_TIME,
            ANY_FUTURE_RESERVATION_EXPIRATION_TIME,
            ANY_CONFIRMATION_CODE,
            ANY_TRANSPORT_STOP,
            ANY_TRANSPORT_TYPE);
    Reservation reservationWithDifferentFields =
        new Reservation(
            ANY_RESERVATION_ID,
            otherTripInvoiceMock,
            this.groupMock,
            ANY_RESERVATION_CREATION_TIME,
            ANY_FUTURE_RESERVATION_EXPIRATION_TIME,
            ANY_CONFIRMATION_CODE,
            ANY_TRANSPORT_STOP,
            ANY_TRANSPORT_TYPE);

    assertNotEquals(reservation, reservationWithDifferentFields);
  }

  @Test
  void whenCopy_thenReservationIsCopiedWithSameFields() {
    when(this.tripInvoiceMock.getUserID()).thenReturn(ANY_USER_ID);
    when(this.tripInvoiceMock.getTripID()).thenReturn(ANY_TRIP_ID);
    Reservation anyReservation = this.givenReservation();

    Reservation copiedReservation = anyReservation.copy();

    assertNotSame(anyReservation, copiedReservation);
    assertEquals(anyReservation, copiedReservation);
  }

  @Test
  void givenValidConfirmationCode_whenValidatingCode_thenCodeIsValid() {
    Reservation reservation = this.givenReservation();

    boolean result =
        reservation.validateConfirmationCode(ANY_CONFIRMATION_CODE.getConfirmationCode());

    assertTrue(result);
  }

  @Test
  void givenInvalidConfirmationCode_whenValidatingCode_thenCodeIsInvalid() {
    Reservation reservation = this.givenReservation();

    boolean result =
        reservation.validateConfirmationCode(ANY_OTHER_CONFIRMATION_CODE.getConfirmationCode());

    assertFalse(result);
  }

  @Test
  void givenConfirmedReservation_whenCancelling_thenReservationHasStatusCancelled() {
    Reservation reservation = this.givenReservation();
    reservation.setStatus(CONFIRMED);
    LocalDate anyCancellationDate = LocalDate.now();

    reservation.cancel(anyCancellationDate);

    assertTrue(reservation.hasStatus(CANCELLED));
  }

  @Test
  void
      givenReservationAlreadyCancelled_whenCancelling_thenThrowReservationInvalidStatusException() {
    Reservation reservation = this.givenReservation();
    reservation.setStatus(CANCELLED);
    LocalDate anyCancellationDate = LocalDate.now();

    assertThrows(
        ReservationInvalidStatusException.class, () -> reservation.cancel(anyCancellationDate));
  }

  @Test
  void givenReservation_whenPayReservation_thenProcessPaymentIsCalledWithCorrectParameters() {
    when(this.tripInvoiceMock.getUserID()).thenReturn(ANY_USER_ID);
    when(this.tripInvoiceMock.getTripID()).thenReturn(ANY_TRIP_ID);

    Reservation reservation = this.givenReservation();

    reservation.payReservation(
        this.paymentProcessorMock, this.paymentInformationMock, ANY_PAYER_ID);

    verify(this.paymentProcessorMock)
        .processPayment(this.paymentInformationMock, this.tripInvoiceMock, ANY_PAYER_ID);
  }

  @Test
  void givenReservation_whenPayReservation_thenReservationStatusIsSetToConfirmed() {
    when(this.tripInvoiceMock.getUserID()).thenReturn(ANY_USER_ID);
    when(this.tripInvoiceMock.getTripID()).thenReturn(ANY_TRIP_ID);

    Reservation reservation = this.givenReservation();

    reservation.payReservation(
        this.paymentProcessorMock, this.paymentInformationMock, ANY_PAYER_ID);
    assertEquals(ReservationStatus.CONFIRMED, reservation.getStatus());
  }

  @Test
  void whenCalculatingUserCredits_thenTripInvoiceCalculatesUserCredits() {
    Reservation reservation = this.givenReservation();

    reservation.calculateUserCredits();

    verify(this.tripInvoiceMock).calculateUserCredits();
  }

  private Reservation givenReservation() {
    return new Reservation(
        ANY_RESERVATION_ID,
        this.tripInvoiceMock,
        this.groupMock,
        ANY_RESERVATION_CREATION_TIME,
        ANY_FUTURE_RESERVATION_EXPIRATION_TIME,
        ANY_CONFIRMATION_CODE,
        ANY_TRANSPORT_STOP,
        ANY_TRANSPORT_TYPE);
  }

  private TripInvoice givenAnotherObjectWithDifferentTripId() {
    when(this.tripInvoiceMock.getUserID()).thenReturn(ANY_USER_ID);
    when(this.tripInvoiceMock.getTripID()).thenReturn(ANY_TRIP_ID);
    TripInvoice otherTripInvoiceMock = mock(TripInvoice.class);
    TripID otherTripID = TripID.generate();
    when(otherTripInvoiceMock.getTripID()).thenReturn(otherTripID);
    when(otherTripInvoiceMock.getUserID()).thenReturn(ANY_USER_ID);
    return otherTripInvoiceMock;
  }

  private TripInvoice givenAnotherObjectWithDifferentUserId() {
    when(this.tripInvoiceMock.getUserID()).thenReturn(ANY_USER_ID);
    when(this.tripInvoiceMock.getTripID()).thenReturn(ANY_TRIP_ID);
    TripInvoice otherTripInvoiceMock = mock(TripInvoice.class);
    TravelerID otherUserID = new TravelerID("111");
    when(otherTripInvoiceMock.getUserID()).thenReturn(otherUserID);
    when(otherTripInvoiceMock.getTripID()).thenReturn(ANY_TRIP_ID);
    return otherTripInvoiceMock;
  }
}
