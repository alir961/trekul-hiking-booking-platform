package ca.ulaval.glo4003.tasks;

import ca.ulaval.glo4003.InfraServiceCreator;
import ca.ulaval.glo4003.ServiceCreator;
import ca.ulaval.glo4003.backgroundtask.BackgroundTaskService;
import ca.ulaval.glo4003.clock.SystemClock;
import ca.ulaval.glo4003.config.TrekULConfigYaml;
import ca.ulaval.glo4003.notification.types.trip.upcoming.UpcomingTripNotificationBuilder;
import ca.ulaval.glo4003.tasks.reservation.ReservationExpirationBackgroundTask;
import ca.ulaval.glo4003.tasks.trip.TripBlockingBackgroundTask;
import ca.ulaval.glo4003.tasks.trip.TripNotificationBackgroundTask;

public class BackgroundTaskBuilder {
  private final BackgroundTaskService backgroundTaskService;
  private final ServiceCreator serviceCreator;
  private final TrekULConfigYaml infraTrekULConfig;
  private final InfraServiceCreator infraServiceCreator;

  public BackgroundTaskBuilder(
      BackgroundTaskService backgroundTaskService,
      ServiceCreator serviceCreator,
      TrekULConfigYaml trekULConfig,
      InfraServiceCreator infraServiceCreator) {
    this.backgroundTaskService = backgroundTaskService;
    this.serviceCreator = serviceCreator;
    this.infraTrekULConfig = trekULConfig;
    this.infraServiceCreator = infraServiceCreator;
  }

  public BackgroundTaskBuilder addReservationExpirationTask() {
    ReservationExpirationBackgroundTask task =
        new ReservationExpirationBackgroundTask(
            this.infraTrekULConfig, this.serviceCreator.createReservationExpirationService());
    this.backgroundTaskService.addTask(task);

    return this;
  }

  public BackgroundTaskBuilder addTripBlockingTask() {
    TripBlockingBackgroundTask task =
        new TripBlockingBackgroundTask(
            this.infraTrekULConfig,
            this.serviceCreator.createTripBlockingService(
                this.infraTrekULConfig.getTripConfig().getDurationBeforeBlockingTrip()));
    this.backgroundTaskService.addTask(task);

    return this;
  }

  public BackgroundTaskBuilder addTripNotificationBackgroundTask(
      UpcomingTripNotificationBuilder builder) {
    TripNotificationBackgroundTask task =
        new TripNotificationBackgroundTask(
            this.infraTrekULConfig,
            this.serviceCreator.createTripNotificationService(
                builder,
                this.infraServiceCreator.createEmailNotificationSender(),
                new SystemClock(),
                this.infraTrekULConfig.getTripConfig().getDurationBeforeNotifyingTravelers()));
    this.backgroundTaskService.addTask(task);
    return this;
  }

  public BackgroundTaskService build() {
    return this.backgroundTaskService;
  }
}
