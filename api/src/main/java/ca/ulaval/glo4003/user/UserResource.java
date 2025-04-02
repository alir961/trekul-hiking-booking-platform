package ca.ulaval.glo4003.user;

import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.user.request.CreateUserRequest;
import ca.ulaval.glo4003.user.response.ProfileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public interface UserResource {
  @Operation(
      summary = "Create a new user",
      description = "Creates a new user with the provided registration details.",
      requestBody =
          @RequestBody(
              description = "Information of the user to register",
              required = true,
              content =
                  @Content(
                      mediaType = MediaType.APPLICATION_JSON,
                      schema = @Schema(implementation = CreateUserRequest.class))),
      responses = {
        @ApiResponse(responseCode = "200", description = "User registered successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid user information provided"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      })
  Response createUser(CreateUserRequest createUserRequest);

  @SecurityRequirement(name = "BearerAuth")
  @Operation(
      summary = "Read user profile",
      description = "Retrieve the profile information of the currently logged-in user.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Profile retrieved successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProfileResponse.class))),
        @ApiResponse(responseCode = "400", description = "The token is invalid or expired"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      })
  Response readProfile(TravelerID travelerID, String userID);
}
