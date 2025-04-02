package ca.ulaval.glo4003.backgroundtask;

public interface BackgroundTaskService {
  void addTask(BackgroundTask task);

  void shutdownScheduler();
}
