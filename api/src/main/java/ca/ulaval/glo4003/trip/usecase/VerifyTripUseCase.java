package ca.ulaval.glo4003.trip.usecase;

import ca.ulaval.glo4003.confirmationcode.ConfirmationCodeVerificationService;
import ca.ulaval.glo4003.confirmationcode.ValidationResult;
import ca.ulaval.glo4003.trip.TripID;
import jakarta.inject.Inject;

public class VerifyTripUseCase {
  private final ConfirmationCodeVerificationService confirmationCodeVerificationService;

  @Inject
  public VerifyTripUseCase(
      ConfirmationCodeVerificationService confirmationCodeVerificationService) {
    this.confirmationCodeVerificationService = confirmationCodeVerificationService;
  }

  public ValidationResult verifyTrip(TripID tripId, String code) {
    return this.confirmationCodeVerificationService.verifyConfirmationCode(tripId, code);
  }
}
