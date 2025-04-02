package ca.ulaval.glo4003.trip;

import ca.ulaval.glo4003.trip.request.SearchTripRequest;
import ca.ulaval.glo4003.trip.response.SearchTripResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Tag(name = "TripResource")
@SecurityRequirement(name = "BearerAuth")
public interface TripResource {
  @Operation(
      summary = "Search a trip based on specifications",
      description =
          "Allows the user to search for available trips based on preferences such as region, dates, price limit, and whether to include full trips.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Search successful",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = SearchTripResponse.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid search parameters. Ensure all parameters are valid."),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      })
  Response searchTrip(
      @Schema(implementation = SearchTripRequest.class) SearchTripRequest searchTripRequest);
}
