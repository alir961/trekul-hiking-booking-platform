package ca.ulaval.glo4003.backgroundtask;

import static org.mockito.Mockito.*;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ConcurrentBackgroundTaskServiceTest {
  private static final int ANY_POOL_SIZE = 10;
  private static final long TASK_DELAY_IN_SECONDS = 0L;
  private static final long TASK_INTERVAL_IN_SECONDS = 60L;
  private static final long SHUTDOWN_TIMEOUT = 60L;
  private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;
  private static final SchedulerConfig SCHEDULER_CONFIG =
      new SchedulerConfig(SHUTDOWN_TIMEOUT, ANY_POOL_SIZE);
  private ScheduledExecutorService scheduledExecutorServiceSpy;

  private ConcurrentBackgroundTaskService concurrentBackgroundTaskService;

  @BeforeEach
  void setUp() {
    this.scheduledExecutorServiceSpy = mock(ScheduledExecutorService.class);
    this.concurrentBackgroundTaskService =
        new ConcurrentBackgroundTaskService(this.scheduledExecutorServiceSpy, SCHEDULER_CONFIG);
  }

  @Test
  void whenAddingTask_thenAddToScheduler() {
    BackgroundTask task = mock(BackgroundTask.class);
    when(task.getDelayInSeconds()).thenReturn(TASK_DELAY_IN_SECONDS);
    when(task.getIntervalInSeconds()).thenReturn(TASK_INTERVAL_IN_SECONDS);
    this.concurrentBackgroundTaskService.addTask(task);

    verify(this.scheduledExecutorServiceSpy)
        .scheduleAtFixedRate(task, TASK_DELAY_IN_SECONDS, TASK_INTERVAL_IN_SECONDS, TIME_UNIT);
  }

  @Test
  void whenShutdownScheduler_thenSchedulerIsShutdown() {
    this.concurrentBackgroundTaskService.shutdownScheduler();

    verify(this.scheduledExecutorServiceSpy).shutdown();
  }
}
