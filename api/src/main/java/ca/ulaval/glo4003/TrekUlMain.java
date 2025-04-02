package ca.ulaval.glo4003;

import static java.lang.Thread.currentThread;

import java.net.URI;
import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrekUlMain {
  private static final Logger LOGGER = LoggerFactory.getLogger(TrekUlMain.class);
  private static final String BASE_URI = "http://localhost:8080/";

  public static void main(String[] args) {
    LOGGER.info("Setup resources (API)");

    final RepositoryCreator repositoryCreator = new InMemoryRepositoryCreator();

    final ApiResourcesCreator apiResourcesCreator = new ApiResourcesCreator(repositoryCreator);
    final ApiSubServers subServers = new ApiSubServers(apiResourcesCreator);
    subServers.withBackgroundTasks().withLocalEtlProcess();

    runServer(apiResourcesCreator, subServers);
  }

  private static void runServer(ApiResourcesCreator apiResourcesCreator, ApiSubServers subServers) {
    try {
      LOGGER.info("Setup http server");
      subServers.start();
      final Server server =
          JettyHttpContainerFactory.createServer(
              URI.create(BASE_URI), apiResourcesCreator.createRestResources());

      Runtime.getRuntime()
          .addShutdownHook(
              new Thread(
                  () -> {
                    try {
                      LOGGER.info("Shutting down the application...");
                      subServers.stop();
                      server.stop();
                      LOGGER.info("Done, exit.");
                    } catch (Exception e) {
                      LOGGER.error("Error shutting down the server", e);
                    }
                  }));

      LOGGER.info("Application started.%nStop the application using CTRL+C");

      // block and wait shut down signal, like CTRL+C
      currentThread().join();
    } catch (InterruptedException e) {
      LOGGER.error("Error starting up the server", e);
    }
  }
}
