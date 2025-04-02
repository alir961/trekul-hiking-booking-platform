package ca.ulaval.glo4003.config.dto;

public class SchedulerConfigDto {
  public long shutdownTimeoutInSeconds;
  public int poolSize;
  public BackgroundTaskConfigDto[] tasks;
}
