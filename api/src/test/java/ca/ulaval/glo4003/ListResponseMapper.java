package ca.ulaval.glo4003;

import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("checkstyle:TypeNameTestsFolder")
public class ListResponseMapper {

  private ListResponseMapper() {}

  @SuppressWarnings("checkstyle:MethodNameTestsFolderPublicMethods")
  public static List<HashMap<String, Object>> mapListResponse(Response response) {
    return response.readEntity(new GenericType<>() {});
  }
}
