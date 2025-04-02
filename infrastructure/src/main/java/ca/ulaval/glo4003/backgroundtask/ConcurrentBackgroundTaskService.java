package ca.ulaval.glo4003.backgroundtask;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ConcurrentBackgroundTaskService implements BackgroundTaskService {
  private final ScheduledExecutorService scheduler;
  private final SchedulerConfig schedulerConfig;

  public ConcurrentBackgroundTaskService(
      ScheduledExecutorService scheduler, SchedulerConfig schedulerConfig) {
    this.scheduler = scheduler;
    this.schedulerConfig = schedulerConfig;
  }

  @Override
  public void addTask(BackgroundTask task) {
    this.scheduler.scheduleAtFixedRate(
        task, task.getDelayInSeconds(), task.getIntervalInSeconds(), TimeUnit.SECONDS);
  }

  @Override
  public void shutdownScheduler() {
    if (this.scheduler != null) {
      this.scheduler.shutdown();
      try {
        if (!this.scheduler.awaitTermination(
            this.schedulerConfig.shutdownTimeoutInSeconds(), TimeUnit.SECONDS)) {
          this.scheduler.shutdownNow();
        }
      } catch (InterruptedException e) {
        this.scheduler.shutdownNow();
        Thread.currentThread().interrupt();
      }
    }
  }
}
