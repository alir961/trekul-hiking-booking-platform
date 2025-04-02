package ca.ulaval.glo4003.transport;

import ca.ulaval.glo4003.transport.response.TransportOptionResponse;
import ca.ulaval.glo4003.trip.TripID;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Tag(name = "TransportResource")
@SecurityRequirement(name = "BearerAuth")
public interface TransportResource {
  @Operation(
      summary = "List transports available for specified trip",
      description = "Allows the user to do a search on the available transports of the trip",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Search successful",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = TransportOptionResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      })
  Response listTransportOptionsOfTrip(
      @Parameter(
              description = "ID of the trip for which available transports are being searched",
              required = true)
          TripID tripID);

  @Operation(
      summary = "Get details of specified transport type",
      description =
          "Show details of specified transport type, provided it contains details and specified trip contains it.",
      responses = {
        @ApiResponse(responseCode = "200", description = "Search successful"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "404", description = "No details available for transport"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      })
  Response getTransportDetails(
      @Parameter(
              description = "ID of the trip for which details are being searched",
              required = true)
          TripID tripID,
      @Parameter(
              description =
                  "Type of the searched transport"
                      + "Valid transport type: ORGANIZED_BUS, CARPOOL, NONE.",
              required = true)
          String transportType);
}
