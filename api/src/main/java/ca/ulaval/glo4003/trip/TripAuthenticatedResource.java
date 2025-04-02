package ca.ulaval.glo4003.trip;

import ca.ulaval.glo4003.reservation.request.ReservationRequest;
import ca.ulaval.glo4003.reservation.response.ReservationResponse;
import ca.ulaval.glo4003.traveler.TravelerID;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public interface TripAuthenticatedResource {
  @Operation(
      summary = "Create a new reservation for specified trip",
      description = "Creates a new reservation for the authenticated user",
      requestBody =
          @RequestBody(
              description = "Reservation request details",
              required = true,
              content =
                  @Content(
                      mediaType = MediaType.APPLICATION_JSON,
                      schema = @Schema(implementation = ReservationRequest.class))),
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Reservation created successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ReservationResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid reservation request data"),
        @ApiResponse(responseCode = "404", description = "Non existing trip"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      })
  Response createReservation(
      @Parameter(description = "ID of the trip to reserve", required = true) TripID tripID,
      TravelerID userID,
      ReservationRequest reservationRequest);

  @Operation(
      summary = "Block a trip by its ID",
      description = "Only allows the user to block a trip if they are the owner",
      responses = {
        @ApiResponse(responseCode = "204", description = "Trip blocked successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid user or login"),
        @ApiResponse(responseCode = "401", description = "User is not the owner"),
        @ApiResponse(responseCode = "404", description = "Trip not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      })
  Response blockTrip(
      @Parameter(description = "ID of the trip to block", required = true) TripID tripID);

  @Operation(
      summary = "Validate a Reservation's confirmation code for a trip",
      description = "Allows an organizer to validate a reservation's confirmation code",
      responses = {
        @ApiResponse(responseCode = "200", description = "Validation successful"),
        @ApiResponse(responseCode = "400", description = "Invalid confirmation code"),
        @ApiResponse(responseCode = "404", description = "Trip not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      })
  Response validateConfirmationCode(
      @Parameter(description = "ID of the trip", required = true) TripID tripID,
      @Parameter(description = "Confirmation code of the reservation", required = true)
          String code);
}
