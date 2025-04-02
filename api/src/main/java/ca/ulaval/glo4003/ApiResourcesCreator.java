package ca.ulaval.glo4003;

import ca.ulaval.glo4003.auth.AuthBinder;
import ca.ulaval.glo4003.backgroundtask.BackgroundTaskService;
import ca.ulaval.glo4003.config.TrekULConfigYaml;
import ca.ulaval.glo4003.etl.LocalEtlProcessor;
import ca.ulaval.glo4003.interfaces.rest.filters.CORSResponseFilter;
import ca.ulaval.glo4003.notification.UpcomingTripNotificationFrenchBuilder;
import ca.ulaval.glo4003.notification.language.FrenchDescriptions;
import ca.ulaval.glo4003.tasks.BackgroundTaskBuilder;
import org.glassfish.jersey.server.ResourceConfig;

public class ApiResourcesCreator {
  private final TrekULConfigYaml trekULConfig = new TrekULConfigYaml("config.trek-ul.yaml");
  private final InfraServiceCreator infraServiceCreator =
      new InfraServiceCreator(this.trekULConfig);
  private final FactoryCreator factoryCreator;
  private final RepositoryCreator repositoryCreator;
  private final ServiceCreator serviceCreator;

  public ApiResourcesCreator(RepositoryCreator repositoryCreator) {
    final AssemblerCreator assemblerCreator = new AssemblerCreator();
    this.factoryCreator = new FactoryCreator(repositoryCreator);
    this.serviceCreator =
        new ServiceCreator(repositoryCreator, this.factoryCreator, assemblerCreator);
    this.repositoryCreator = repositoryCreator;
  }

  public ResourceConfig createRestResources() {
    ResourceConfig config = new ResourceConfig();
    config.register(
        new ApiServiceBinder(this.serviceCreator, this.infraServiceCreator, this.trekULConfig));
    config.register(new ApiRestBinder());
    config.register(new AuthBinder());
    AuthBinder.registerFilters(config);
    config.register(new CORSResponseFilter());
    config.packages("ca.ulaval.glo4003");

    return config;
  }

  public BackgroundTaskService createBackgroundTasks() {
    final BackgroundTaskService backgroundTaskService =
        this.infraServiceCreator.createBackgroundTaskService();
    final BackgroundTaskBuilder backgroundTaskBuilder =
        new BackgroundTaskBuilder(
            backgroundTaskService,
            this.serviceCreator,
            this.trekULConfig,
            this.infraServiceCreator);
    return backgroundTaskBuilder
        .addReservationExpirationTask()
        .addTripBlockingTask()
        .addTripNotificationBackgroundTask(
            new UpcomingTripNotificationFrenchBuilder(new FrenchDescriptions()))
        .build();
  }

  public LocalEtlProcessor createEtlProcessor() {
    return new LocalEtlProcessor(this.repositoryCreator, this.factoryCreator)
        .prepareTripEtl(this.trekULConfig.getTripConfig(), this.trekULConfig.getTransportConfig())
        .prepareHostingEtl()
        .prepareOrganizerUserEtl();
  }
}
