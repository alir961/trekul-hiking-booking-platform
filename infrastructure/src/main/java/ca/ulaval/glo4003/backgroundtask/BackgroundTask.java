package ca.ulaval.glo4003.backgroundtask;

public abstract class BackgroundTask implements Runnable {
  private final long delayInSeconds;
  private final long intervalInSeconds;

  public BackgroundTask(BackgroundTaskConfig config) {
    this.delayInSeconds = config.delayInSeconds();
    this.intervalInSeconds = config.intervalInSeconds();
  }

  public long getDelayInSeconds() {
    return this.delayInSeconds;
  }

  public long getIntervalInSeconds() {
    return this.intervalInSeconds;
  }
}
