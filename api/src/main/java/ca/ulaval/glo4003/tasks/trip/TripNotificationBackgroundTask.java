package ca.ulaval.glo4003.tasks.trip;

import ca.ulaval.glo4003.backgroundtask.BackgroundTask;
import ca.ulaval.glo4003.config.TrekULConfigYaml;
import ca.ulaval.glo4003.notification.TripNotificationService;

public class TripNotificationBackgroundTask extends BackgroundTask {

  private static final String TASK_CONFIG_KEY = "NOTIFY_UPCOMING_TRIPS";
  private final TripNotificationService tripNotificationService;

  public TripNotificationBackgroundTask(
      TrekULConfigYaml infraTrekULConfig, TripNotificationService tripNotificationService) {
    super(infraTrekULConfig.getTaskConfig(TASK_CONFIG_KEY));
    this.tripNotificationService = tripNotificationService;
  }

  public void run() {
    this.tripNotificationService.notifyUpComingTrips();
  }
}
