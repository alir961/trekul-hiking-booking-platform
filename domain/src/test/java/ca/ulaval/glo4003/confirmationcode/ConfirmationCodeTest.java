package ca.ulaval.glo4003.confirmationcode;

import static ca.ulaval.glo4003.reservation.ReservationStatus.*;
import static ca.ulaval.glo4003.transport.TransportType.NONE;
import static ca.ulaval.glo4003.transport.TransportType.ORGANIZED_BUS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.invoice.TripInvoice;
import ca.ulaval.glo4003.reservation.ReservationStatus;
import ca.ulaval.glo4003.transport.TransportType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConfirmationCodeTest {
  private static final ReservationStatus VALID_STATUS = CONFIRMED;
  private static final TransportType NOT_ORGANIZED_BUS = NONE;
  private static final TransportType ANY_TRANSPORT_TYPE = ORGANIZED_BUS;
  private static final String ANY_CODE = "rg9wkbw";
  private static final int ANY_NUMBER_OF_PAYERS = 2;
  private static final int VALID_FOR_NONE = 0;
  private ConfirmationCode confirmationCode;

  private TripInvoice tripInvoiceMock;

  @BeforeEach
  void setUp() {
    this.tripInvoiceMock = mock(TripInvoice.class);
    this.confirmationCode = new ConfirmationCode(ANY_CODE);
  }

  @Test
  void
      givenAnyTripInvoiceIsNotFullPaid_whenCreatingValidationResult_thenReturnNotPaidValidationResult() {
    when(this.tripInvoiceMock.isFullyPaidByGroup()).thenReturn(false);

    ValidationResult result =
        this.confirmationCode.createValidationResult(
            this.tripInvoiceMock, ANY_TRANSPORT_TYPE, VALID_STATUS);

    assertFalse(result.canBoard());
    assertEquals(ConfirmationCodeResult.NOT_PAID.toString(), result.message());
    assertEquals(VALID_FOR_NONE, result.validFor());
  }

  @Test
  void
      givenAnyTripInvoiceIsFullPaidWithAnyTransportType_whenCreatingValidationResult_thenReturnValidValidationResult() {
    this.givenAnyTripInvoiceIsFullPaid();

    ValidationResult result =
        this.confirmationCode.createValidationResult(
            this.tripInvoiceMock, ANY_TRANSPORT_TYPE, VALID_STATUS);

    assertEquals(ConfirmationCodeResult.VALID.toString(), result.message());
    assertEquals(ANY_NUMBER_OF_PAYERS, result.validFor());
  }

  @Test
  void
      givenAnyTripInvoiceIsFullPaidWithAnythingButOrganizedBus_whenCreatingValidationResult_thenResultCanNotBoard() {
    this.givenAnyTripInvoiceIsFullPaid();

    ValidationResult result =
        this.confirmationCode.createValidationResult(
            this.tripInvoiceMock, NOT_ORGANIZED_BUS, VALID_STATUS);

    assertFalse(result.canBoard());
    assertEquals(ConfirmationCodeResult.VALID.toString(), result.message());
    assertEquals(ANY_NUMBER_OF_PAYERS, result.validFor());
  }

  @Test
  void
      givenAnyTripInvoiceIsFullPaidWithOrganizedBus_whenCreatingValidationResult_thenResultCanBoard() {
    this.givenAnyTripInvoiceIsFullPaid();

    ValidationResult result =
        this.confirmationCode.createValidationResult(
            this.tripInvoiceMock, ORGANIZED_BUS, VALID_STATUS);

    assertTrue(result.canBoard());
    assertEquals(ConfirmationCodeResult.VALID.toString(), result.message());
    assertEquals(ANY_NUMBER_OF_PAYERS, result.validFor());
  }

  private void givenAnyTripInvoiceIsFullPaid() {
    when(this.tripInvoiceMock.isFullyPaidByGroup()).thenReturn(true);
    when(this.tripInvoiceMock.getNumberOfPayers()).thenReturn(ANY_NUMBER_OF_PAYERS);
  }

  @Test
  void givenReservationIsCancelled_whenCreatingValidationResult_thenResultIsInvalid() {
    ValidationResult result =
        this.confirmationCode.createValidationResult(
            this.tripInvoiceMock, ORGANIZED_BUS, CANCELLED);

    this.assertResultIsInvalid(result);
  }

  @Test
  void givenReservationIsPending_whenCreatingValidationResult_thenResultIsInvalid() {
    ValidationResult result =
        this.confirmationCode.createValidationResult(this.tripInvoiceMock, ORGANIZED_BUS, PENDING);

    this.assertResultIsInvalid(result);
  }

  private void assertResultIsInvalid(ValidationResult result) {
    assertFalse(result.canBoard());
    assertEquals(ConfirmationCodeResult.INVALID.toString(), result.message());
    assertEquals(VALID_FOR_NONE, result.validFor());
  }
}
