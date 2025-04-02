package ca.ulaval.glo4003.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.InputStream;

@Path("/api")
@Tag(name = "SwaggerResource")
public class SwaggerResource {

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(
      summary = "Provide swagger.json file",
      description = "Provide the OpenAPI specification file based on in code annotations.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "OpenAPI specification file provided successfully"),
        @ApiResponse(responseCode = "404", description = "OpenAPI specification file not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      })
  public Response openApiSpec() {
    InputStream openApiFile = getClass().getClassLoader().getResourceAsStream("swagger.json");
    if (openApiFile == null) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity("OpenAPI specification file not found")
          .build();
    }
    return Response.ok(openApiFile).build();
  }
}
