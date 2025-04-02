package ca.ulaval.glo4003;

import ca.ulaval.glo4003.backgroundtask.BackgroundTaskService;
import ca.ulaval.glo4003.backgroundtask.ConcurrentBackgroundTaskService;
import ca.ulaval.glo4003.backgroundtask.SchedulerConfig;
import ca.ulaval.glo4003.config.TrekULConfigYaml;
import ca.ulaval.glo4003.notification.EmailNotificationSender;
import ca.ulaval.glo4003.notification.SmtpEmailSender;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class InfraServiceCreator {
  private final TrekULConfigYaml infraTrekULConfig;

  public InfraServiceCreator(TrekULConfigYaml infraTrekULConfig) {
    this.infraTrekULConfig = infraTrekULConfig;
  }

  public BackgroundTaskService createBackgroundTaskService() {
    SchedulerConfig schedulerConfig = this.infraTrekULConfig.getSchedulerConfig();
    ScheduledExecutorService schedulerService =
        Executors.newScheduledThreadPool(schedulerConfig.poolsize());
    return new ConcurrentBackgroundTaskService(schedulerService, schedulerConfig);
  }

  public EmailNotificationSender createEmailNotificationSender() {
    return new SmtpEmailSender(this.infraTrekULConfig.getEmailSmtpConfig());
  }
}
