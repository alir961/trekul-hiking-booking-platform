package ca.ulaval.glo4003;

import ca.ulaval.glo4003.backgroundtask.BackgroundTaskService;
import ca.ulaval.glo4003.etl.LocalEtlProcessor;
import java.util.ArrayList;
import java.util.List;

public class ApiSubServers {
  private final ApiResourcesCreator resourcesCreator;
  private final List<Runnable> startServers = new ArrayList<>();
  private final List<Runnable> stopServers = new ArrayList<>();

  public ApiSubServers(ApiResourcesCreator resourcesCreator) {
    this.resourcesCreator = resourcesCreator;
  }

  public ApiSubServers withBackgroundTasks() {
    BackgroundTaskService service = this.resourcesCreator.createBackgroundTasks();
    this.stopServers.add(service::shutdownScheduler);

    return this;
  }

  public ApiSubServers withLocalEtlProcess() {
    LocalEtlProcessor localEtlProcessor = this.resourcesCreator.createEtlProcessor();
    this.startServers.add(localEtlProcessor::execute);

    return this;
  }

  public void start() {
    this.startServers.forEach(Runnable::run);
  }

  public void stop() {
    this.stopServers.forEach(Runnable::run);
  }
}
