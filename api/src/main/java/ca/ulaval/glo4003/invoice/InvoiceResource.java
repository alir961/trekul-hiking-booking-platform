package ca.ulaval.glo4003.invoice;

import ca.ulaval.glo4003.invoice.response.InvoiceResponse;
import ca.ulaval.glo4003.invoice.response.InvoiceShortResponse;
import ca.ulaval.glo4003.payment.request.PaymentRequest;
import ca.ulaval.glo4003.payment.response.PaymentResponse;
import ca.ulaval.glo4003.traveler.TravelerID;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Tag(name = "InvoiceResource")
@SecurityRequirement(name = "BearerAuth")
public interface InvoiceResource {
  @Operation(
      summary = "List invoices of logged-in user",
      description = "Only get invoices related to logged in user.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Search successful",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = InvoiceShortResponse.class))),
        @ApiResponse(responseCode = "401", description = "Invalid user or login"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      })
  Response getInvoices(TravelerID travelerID);

  @Operation(
      summary = "Get details of specified invoice",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Search successful",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = InvoiceResponse.class))),
        @ApiResponse(responseCode = "401", description = "Invalid user or login"),
        @ApiResponse(responseCode = "403", description = "Invalid access to invoice"),
        @ApiResponse(responseCode = "404", description = "Invoice not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      })
  Response getInvoice(
      @Parameter(description = "ID of ownerInvoice user", required = true) TravelerID travelerID,
      @Parameter(description = "ID of the needed invoice", required = true) InvoiceID invoiceID,
      TravelerID accessorID);

  @Operation(
      summary = "Checkout an existing reservation",
      description = "Completes the reservation process for the authenticated user",
      requestBody =
          @RequestBody(
              description = "Reservation checkout request details",
              required = true,
              content =
                  @Content(
                      mediaType = MediaType.APPLICATION_JSON,
                      schema = @Schema(implementation = PaymentRequest.class))),
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Reservation checked out successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PaymentResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid checkout request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      })
  Response payInvoice(
      @Parameter(description = "ID of ownerInvoice user", required = true) TravelerID ownerID,
      @Parameter(description = "ID of the needed invoice", required = true) InvoiceID invoiceID,
      PaymentRequest paymentRequest,
      TravelerID payerID);

  @Operation(
      summary = "Get confirmation code for the specified invoice",
      description =
          "Provides a confirmation code for a specific invoice, allowing the user to confirm the trip.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Confirmation code retrieved successfully",
            content = @Content(mediaType = MediaType.TEXT_PLAIN)),
        @ApiResponse(responseCode = "401", description = "Invalid user or login"),
        @ApiResponse(responseCode = "403", description = "Invalid access to invoice"),
        @ApiResponse(responseCode = "404", description = "Invoice not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      })
  @Parameter(description = "ID of the needed invoice", required = true)
  Response getConfirmationCode(TravelerID travelerID, InvoiceID invoiceID);
}
