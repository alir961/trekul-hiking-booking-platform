package ca.ulaval.glo4003.confirmationcode;

import static ca.ulaval.glo4003.confirmationcode.ConfirmationCodeResult.*;
import static ca.ulaval.glo4003.reservation.ReservationStatus.CONFIRMED;
import static ca.ulaval.glo4003.transport.TransportType.ORGANIZED_BUS;

import ca.ulaval.glo4003.invoice.TripInvoice;
import ca.ulaval.glo4003.reservation.ReservationStatus;
import ca.ulaval.glo4003.transport.TransportType;

public class ConfirmationCode {
  private static final int VALID_FOR_NONE = 0;

  private final String code;

  public ConfirmationCode(String code) {
    this.code = code;
  }

  public boolean verifyConfirmationCode(String code) {
    return this.code.equals(code);
  }

  public ValidationResult createValidationResult(
      TripInvoice invoice, TransportType transportType, ReservationStatus status) {
    ValidationResult validationResult;

    if (!CONFIRMED.equals(status)) {
      validationResult = new ValidationResult(false, INVALID.toString(), VALID_FOR_NONE);
    } else if (!invoice.isFullyPaidByGroup()) {
      validationResult = new ValidationResult(false, NOT_PAID.toString(), VALID_FOR_NONE);
    } else {
      validationResult =
          new ValidationResult(
              ORGANIZED_BUS.equals(transportType), VALID.toString(), invoice.getNumberOfPayers());
    }

    return validationResult;
  }

  public String getConfirmationCode() {
    return this.code;
  }

  @Override
  public boolean equals(Object o) {
    boolean equal = false;
    if (o == this) {
      equal = true;
    } else if (o instanceof ConfirmationCode confirmationCode) {
      equal = this.code.equals(confirmationCode.code);
    }
    return equal;
  }
}
