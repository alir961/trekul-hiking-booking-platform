package ca.ulaval.glo4003.login;

import ca.ulaval.glo4003.login.request.LoginRequest;
import ca.ulaval.glo4003.login.response.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Tag(name = "LoginResource")
public interface LoginResource {
  @Operation(
      summary = "Login user",
      description = "Authenticates a user based on the provided login information.",
      requestBody =
          @RequestBody(
              description = "Login credentials",
              required = true,
              content =
                  @Content(
                      mediaType = MediaType.APPLICATION_JSON,
                      schema = @Schema(implementation = LoginRequest.class))),
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Login successful",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = LoginResponse.class))),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized - Invalid login credentials"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      })
  Response login(LoginRequest loginRequest);
}
