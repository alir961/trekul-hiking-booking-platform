package ca.ulaval.glo4003.etl;

import ca.ulaval.glo4003.FactoryCreator;
import ca.ulaval.glo4003.RepositoryCreator;
import ca.ulaval.glo4003.etl.exception.ExtractException;
import ca.ulaval.glo4003.etl.hiking.LocalFileHikingExtractor;
import ca.ulaval.glo4003.etl.hiking.LocalFileHikingRegionExtractor;
import ca.ulaval.glo4003.etl.hosting.HostingEtlExecutor;
import ca.ulaval.glo4003.etl.hosting.HostingLoader;
import ca.ulaval.glo4003.etl.hosting.LocalFileHostingExtractor;
import ca.ulaval.glo4003.etl.transport.LocalFileOrganizedBusStopExtractor;
import ca.ulaval.glo4003.etl.trip.TripEtlExecutor;
import ca.ulaval.glo4003.etl.trip.TripLoader;
import ca.ulaval.glo4003.etl.user.LocalStaticUserExtractor;
import ca.ulaval.glo4003.etl.user.UserEtlExecutor;
import ca.ulaval.glo4003.etl.user.UserLoader;
import ca.ulaval.glo4003.hiking.HikingCsvParser;
import ca.ulaval.glo4003.hiking.HikingRegionJsonParser;
import ca.ulaval.glo4003.hosting.HostingXmlParser;
import ca.ulaval.glo4003.transport.TransportConfig;
import ca.ulaval.glo4003.transport.organizedbus.OrganizedBusStopJsonParser;
import ca.ulaval.glo4003.traveler.user.role.OrganizerRole;
import ca.ulaval.glo4003.trip.TripConfig;
import java.util.ArrayList;
import java.util.List;

public class LocalEtlProcessor {
  private final RepositoryCreator repositoryCreator;
  private final FactoryCreator factoryCreator;
  private final List<EtlExecutor> etlExecutors = new ArrayList<>();

  public LocalEtlProcessor(RepositoryCreator repositoryCreator, FactoryCreator factoryCreator) {
    this.repositoryCreator = repositoryCreator;
    this.factoryCreator = factoryCreator;
  }

  public LocalEtlProcessor prepareTripEtl(TripConfig tripConfig, TransportConfig transportConfig) {
    LocalFileHikingRegionExtractor regionExtractor =
        new LocalFileHikingRegionExtractor(new HikingRegionJsonParser());
    LocalFileHikingExtractor hikingExtractor =
        new LocalFileHikingExtractor(new HikingCsvParser(), regionExtractor);
    LocalFileOrganizedBusStopExtractor busStopExtractor =
        new LocalFileOrganizedBusStopExtractor(new OrganizedBusStopJsonParser());

    TripLoader tripLoader =
        new TripLoader(
            this.factoryCreator.createTripFactory(tripConfig),
            this.repositoryCreator.createTripRepository(),
            this.factoryCreator.createOrganizedBusFactory(transportConfig),
            this.factoryCreator.createTransportCollectionFactory());
    TripEtlExecutor tripEtlExecutor =
        new TripEtlExecutor(hikingExtractor, busStopExtractor, tripLoader);
    this.etlExecutors.add(tripEtlExecutor);

    return this;
  }

  public LocalEtlProcessor prepareHostingEtl() {
    LocalFileHostingExtractor hostingExtractor =
        new LocalFileHostingExtractor(new HostingXmlParser());
    HostingLoader hostingLoader =
        new HostingLoader(this.repositoryCreator.createHostingRepository());
    this.etlExecutors.add(new HostingEtlExecutor(hostingExtractor, hostingLoader));

    return this;
  }

  public LocalEtlProcessor prepareOrganizerUserEtl() {
    LocalStaticUserExtractor userExtractor = new LocalStaticUserExtractor();
    UserLoader userLoader =
        new UserLoader(
            this.repositoryCreator.createUserRepository(), this.factoryCreator.createUserFactory());
    this.etlExecutors.add(new UserEtlExecutor(userExtractor, userLoader, new OrganizerRole()));

    return this;
  }

  public void execute() throws ExtractException {
    for (EtlExecutor exe : this.etlExecutors) {
      exe.execute();
    }
  }
}
