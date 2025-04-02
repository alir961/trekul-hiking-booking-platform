package ca.ulaval.glo4003.tasks.trip;

import ca.ulaval.glo4003.backgroundtask.BackgroundTask;
import ca.ulaval.glo4003.config.TrekULConfigYaml;
import ca.ulaval.glo4003.trip.TripBlockingService;

public class TripBlockingBackgroundTask extends BackgroundTask {
  private static final String TASK_CONFIG_KEY = "BLOCKING_TRIPS_BEFORE_DATE";
  private final TripBlockingService tripBlockingService;

  public TripBlockingBackgroundTask(
      TrekULConfigYaml infraTrekULConfig, TripBlockingService tripBlockingService) {
    super(infraTrekULConfig.getTaskConfig(TASK_CONFIG_KEY));
    this.tripBlockingService = tripBlockingService;
  }

  public void run() {
    this.tripBlockingService.blockUpcomingTrips();
  }
}
