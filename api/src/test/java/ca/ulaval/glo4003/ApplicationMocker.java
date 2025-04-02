package ca.ulaval.glo4003;

import static org.eclipse.jetty.ee10.servlet.ServletContextHandler.SESSIONS;

import java.util.stream.Stream;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

@SuppressWarnings("checkstyle:TypeNameTestsFolder")
public class ApplicationMocker {

  private final Server server = new Server(8080);

  @SuppressWarnings("checkstyle:MethodNameTestsFolderPublicMethods")
  public void startServer(Object... components) throws Exception {
    ServletContextHandler context = new ServletContextHandler(SESSIONS);
    ServletHolder servlet = this.createServletHolder(components);

    context.setContextPath("/");
    context.addServlet(servlet, "/*");
    this.server.setHandler(context);
    this.server.start();
  }

  @SuppressWarnings("checkstyle:MethodNameTestsFolderPublicMethods")
  public void stopServer() throws Exception {
    this.server.stop();
  }

  @SuppressWarnings("checkstyle:MethodNameTestsFolderPublicMethods")
  private ServletHolder createServletHolder(Object... components) {
    ResourceConfig resource = new ResourceConfig();

    Stream.of(components).forEach(resource::register);
    return new ServletHolder(new ServletContainer(resource));
  }
}
