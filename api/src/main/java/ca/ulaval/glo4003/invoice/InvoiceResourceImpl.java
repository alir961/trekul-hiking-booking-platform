package ca.ulaval.glo4003.invoice;

import ca.ulaval.glo4003.auth.authentication.AuthenticationRequired;
import ca.ulaval.glo4003.auth.authentication.LoggedIn;
import ca.ulaval.glo4003.auth.authorisation.AccessAsOwnerRequired;
import ca.ulaval.glo4003.invoice.assembler.InvoiceResponseAssembler;
import ca.ulaval.glo4003.invoice.dto.InvoiceDto;
import ca.ulaval.glo4003.invoice.response.ConfirmationCodeResponse;
import ca.ulaval.glo4003.invoice.response.InvoiceResponse;
import ca.ulaval.glo4003.invoice.response.InvoiceShortResponse;
import ca.ulaval.glo4003.payment.request.PaymentRequest;
import ca.ulaval.glo4003.payment.usecase.PaymentUseCase;
import ca.ulaval.glo4003.traveler.TravelerID;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/api/trekul/users/{userID}/invoices")
@AuthenticationRequired
public class InvoiceResourceImpl implements InvoiceResource {
  private final TripInvoiceService tripInvoiceService;
  private final InvoiceResponseAssembler invoiceResponseAssembler;
  private final PaymentUseCase paymentUseCase;

  @Inject
  public InvoiceResourceImpl(
      TripInvoiceService tripInvoiceService,
      InvoiceResponseAssembler invoiceResponseAssembler,
      PaymentUseCase paymentUseCase) {
    this.tripInvoiceService = tripInvoiceService;
    this.invoiceResponseAssembler = invoiceResponseAssembler;
    this.paymentUseCase = paymentUseCase;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Override
  @AccessAsOwnerRequired("userID")
  public Response getInvoices(@LoggedIn TravelerID travelerID) {
    List<InvoiceDto> invoices = this.tripInvoiceService.listInvoices(travelerID);
    List<InvoiceShortResponse> response = this.invoiceResponseAssembler.assembleShort(invoices);

    return Response.ok(response).build();
  }

  @GET
  @Path("{invoiceID}")
  @Produces(MediaType.APPLICATION_JSON)
  @Override
  public Response getInvoice(
      @PathParam("userID") TravelerID ownerID,
      @PathParam("invoiceID") InvoiceID invoiceID,
      @LoggedIn TravelerID accessorID) {
    InvoiceDto invoice = this.tripInvoiceService.getInvoice(ownerID, accessorID, invoiceID);
    InvoiceResponse response = this.invoiceResponseAssembler.assemble(invoice);

    return Response.ok(response).build();
  }

  @POST
  @Path("{invoiceID}:pay")
  @Produces(MediaType.APPLICATION_JSON)
  public Response payInvoice(
      @PathParam("userID") TravelerID ownerID,
      @PathParam("invoiceID") InvoiceID invoiceID,
      PaymentRequest paymentRequest,
      @LoggedIn TravelerID payerID) {
    return Response.ok()
        .entity(this.paymentUseCase.payInvoice(paymentRequest, invoiceID, ownerID, payerID))
        .build();
  }

  @GET
  @Path("{invoiceID}/code")
  @Produces(MediaType.APPLICATION_JSON)
  @Override
  @AccessAsOwnerRequired("userID")
  public Response getConfirmationCode(
      @LoggedIn TravelerID travelerID, @PathParam("invoiceID") InvoiceID invoiceID) {
    String confirmationCode = this.tripInvoiceService.getConfirmationCode(travelerID, invoiceID);
    ConfirmationCodeResponse response = new ConfirmationCodeResponse(confirmationCode);
    return Response.ok(response).build();
  }
}
