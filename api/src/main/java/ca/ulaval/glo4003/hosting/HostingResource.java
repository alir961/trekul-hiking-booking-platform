package ca.ulaval.glo4003.hosting;

import ca.ulaval.glo4003.hosting.reservation.response.HostingReservationResponse;
import ca.ulaval.glo4003.hosting.response.SearchHostingResponse;
import ca.ulaval.glo4003.trip.TripID;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.core.Response;

@Tag(name = "HostingResource")
@SecurityRequirement(name = "BearerAuth")
public interface HostingResource {
  @Operation(
      summary = "Get hosts from a given region",
      description = "Retrieve the information of the hosts available in a region.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Hosts retrieved successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SearchHostingResponse.class))),
        @ApiResponse(responseCode = "400", description = "The token is invalid or expired"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      })
  Response findHostsInRegion(
      @Parameter(
              description = "ID of the trip for which compatible hosts are being searched",
              required = true)
          TripID tripID);

  @Operation(
      summary = "Select a hosting for all the passengers of a given trip",
      description =
          "Select an open hosting in the right region that has the capacity for all the passengers of a given trip",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Reservation successful",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = HostingReservationResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid hosting selection request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      })
  Response bookHosting(
      @Parameter(description = "ID of the trip to book hosting for", required = true) TripID tripID,
      @Parameter(description = "ID of the hosting to booked", required = true) String hostingId);
}
