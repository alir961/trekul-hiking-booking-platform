package ca.ulaval.glo4003.auth.authentication;

import ca.ulaval.glo4003.traveler.TravelerID;
import java.util.function.Function;
import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.model.Parameter;
import org.glassfish.jersey.server.spi.internal.ValueParamProvider;

public class LoggedUserParamProvider implements ValueParamProvider {
  @Override
  public Function<ContainerRequest, ?> getValueProvider(Parameter parameter) {
    if (parameter.getRawType().equals(TravelerID.class)
        && parameter.isAnnotationPresent(LoggedIn.class)) {
      return new LoggedUserResolver();
    }

    return null;
  }

  @Override
  public PriorityType getPriority() {
    return Priority.HIGH;
  }
}
