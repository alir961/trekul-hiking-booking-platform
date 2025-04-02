package ca.ulaval.glo4003.auth;

import ca.ulaval.glo4003.auth.authentication.AuthenticationRequestFilter;
import ca.ulaval.glo4003.auth.authentication.LoggedUserParamProvider;
import ca.ulaval.glo4003.auth.authentication.LoggedUserResolver;
import ca.ulaval.glo4003.auth.authorisation.AccessAsOwnerRequestFilter;
import ca.ulaval.glo4003.auth.authorisation.PermissionRequestFilter;
import jakarta.inject.Singleton;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spi.internal.ValueParamProvider;

public class AuthBinder extends AbstractBinder {
  private static final int AUTHENTICATION_FILTER_PRIORITY = 1000;
  private static final int OWNER_ACCESS_FILTER_PRIORITY = 1050;
  private static final int PERMISSION_FILTER_PRIORITY = 1100;

  @Override
  protected void configure() {
    bind(LoggedUserParamProvider.class).to(ValueParamProvider.class).in(Singleton.class);
    bind(LoggedUserResolver.class).to(LoggedUserResolver.class);
  }

  public static void registerFilters(ResourceConfig config) {
    config.register(AuthenticationRequestFilter.class, AUTHENTICATION_FILTER_PRIORITY);
    config.register(PermissionRequestFilter.class, PERMISSION_FILTER_PRIORITY);
    config.register(AccessAsOwnerRequestFilter.class, OWNER_ACCESS_FILTER_PRIORITY);
  }
}
