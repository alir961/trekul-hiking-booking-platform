package ca.ulaval.glo4003.interfaces.rest.filters;

import jakarta.annotation.Resource;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.MultivaluedMap;
import java.io.IOException;

/**
 * This class adds headers to the response context to enable the app to send http request to a
 * different domain than the one hosting the site itself.
 *
 * <p>Cross-origin HTTP requests: <a
 * href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Access_control_CORS">https://developer.mozilla.org/en-US/docs/Web/HTTP/Access_control_CORS</a>
 */
@Resource
public class CORSResponseFilter implements ContainerResponseFilter {
  @Override
  public void filter(
      ContainerRequestContext requestContext, ContainerResponseContext responseContext)
      throws IOException {
    MultivaluedMap<String, Object> headers = responseContext.getHeaders();

    headers.add("Access-Control-Allow-Origin", "*");
    headers.add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS");
    headers.add("Access-Control-Allow-Headers", "Authorization, X-Requested-With, Content-Type");
  }
}
