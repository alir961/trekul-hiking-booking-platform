package ca.ulaval.glo4003.reservation;

import ca.ulaval.glo4003.payment.response.PaymentResponse;
import ca.ulaval.glo4003.traveler.TravelerID;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.core.Response;

@Tag(name = "ReservationResource")
@SecurityRequirement(name = "BearerAuth")
public interface ReservationResource {
  @Operation(
      summary = "Cancel a reservation",
      description =
          "Cancel the reservation of an authenticated user and issue refunds according to the policy",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Reservation cancelled successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PaymentResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid cancellation data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      })
  Response cancelReservation(TravelerID userID, ReservationID reservationId);
}
