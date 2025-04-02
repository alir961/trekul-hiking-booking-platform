package ca.ulaval.glo4003.reservation;

import ca.ulaval.glo4003.auth.authentication.AuthenticationRequired;
import ca.ulaval.glo4003.auth.authentication.LoggedIn;
import ca.ulaval.glo4003.auth.authorisation.AccessAsOwnerRequired;
import ca.ulaval.glo4003.invoice.assembler.InvoiceResponseAssembler;
import ca.ulaval.glo4003.invoice.dto.InvoiceDto;
import ca.ulaval.glo4003.invoice.response.InvoiceResponse;
import ca.ulaval.glo4003.traveler.TravelerID;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/trekul/users/{userID}/reservations")
@AuthenticationRequired
public class ReservationResourceImpl implements ReservationResource {

  private final CancelReservationService cancelReservationService;
  private final InvoiceResponseAssembler invoiceResponseAssembler;

  @Inject
  public ReservationResourceImpl(
      CancelReservationService cancelReservationService,
      InvoiceResponseAssembler invoiceResponseAssembler) {
    this.cancelReservationService = cancelReservationService;
    this.invoiceResponseAssembler = invoiceResponseAssembler;
  }

  @PUT
  @Path("/{reservationID}:cancel")
  @Produces(MediaType.APPLICATION_JSON)
  @Override
  @AccessAsOwnerRequired("userID")
  public Response cancelReservation(
      @LoggedIn TravelerID travelerID, @PathParam("reservationID") ReservationID reservationID) {
    InvoiceDto invoiceDto =
        this.cancelReservationService.cancelReservation(travelerID, reservationID);
    InvoiceResponse response = this.invoiceResponseAssembler.assemble(invoiceDto);
    return Response.ok(response).build();
  }
}
