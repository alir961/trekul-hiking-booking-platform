package ca.ulaval.glo4003.trip;

import ca.ulaval.glo4003.profile.response.ProfileTripResponse;
import ca.ulaval.glo4003.traveler.TravelerID;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public interface TripGuestResource {
  @Operation(
      summary = "Retrieve trips for a specific traveler",
      description = "Get all trips associated with a specific traveler ID.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Trips retrieved successfully",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ProfileTripResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid traveler ID"),
        @ApiResponse(responseCode = "404", description = "No trips found for the traveler"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      })
  Response searchTripByIdAndTravelerId(TripID tripID, TravelerID guestID);

  @Operation(
      summary = "Activate 2 factor authentification for a guest",
      description = "Upon activation, will send email containing 2FA code.",
      responses = {
        @ApiResponse(responseCode = "204", description = "2FA code sent successfully"),
        @ApiResponse(responseCode = "404", description = "No trips found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      })
  Response activate2FAForGuestOfTrip(
      @Parameter(description = "ID of the trip", required = true) TripID tripID,
      @Parameter(description = "ID of the traveler", required = true) String travelerID);
}
