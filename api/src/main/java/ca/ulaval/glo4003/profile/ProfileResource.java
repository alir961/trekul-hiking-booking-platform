package ca.ulaval.glo4003.profile;

import ca.ulaval.glo4003.profile.response.ProfileTripResponse;
import ca.ulaval.glo4003.traveler.TravelerID;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Tag(name = "ProfileResource")
@SecurityRequirement(name = "BearerAuth")
public interface ProfileResource {
  @Operation(
      summary = "List trips of logged-in user",
      description =
          "Retrieve trips related to the logged-in user based on the specified status. "
              + "Valid status values: CONFIRMED, CANCELLED.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Search successful",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ProfileTripResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid user or login"),
        @ApiResponse(responseCode = "422", description = "Invalid reservation status format"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      })
  Response searchProfileTrips(
      TravelerID travelerID,
      @Parameter(
              description =
                  "Status of the trips to retrieve. "
                      + "Valid status values: CONFIRMED, CANCELLED.",
              required = true)
          String status);
}
