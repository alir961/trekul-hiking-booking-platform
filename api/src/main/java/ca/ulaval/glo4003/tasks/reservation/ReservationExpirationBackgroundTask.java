package ca.ulaval.glo4003.tasks.reservation;

import ca.ulaval.glo4003.backgroundtask.BackgroundTask;
import ca.ulaval.glo4003.config.TrekULConfigYaml;
import ca.ulaval.glo4003.reservation.ReservationExpirationService;

public class ReservationExpirationBackgroundTask extends BackgroundTask {
  private static final String TASK_CONFIG_KEY = "RESERVATION_EXPIRATION";
  private final ReservationExpirationService reservationExpirationService;

  public ReservationExpirationBackgroundTask(
      TrekULConfigYaml infraTrekULConfig,
      ReservationExpirationService reservationExpirationService) {
    super(infraTrekULConfig.getTaskConfig(TASK_CONFIG_KEY));
    this.reservationExpirationService = reservationExpirationService;
  }

  public void run() {
    this.reservationExpirationService.cancelExpiredReservations();
  }
}
