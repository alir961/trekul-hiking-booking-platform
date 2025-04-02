package ca.ulaval.glo4003.payment.assembler;

import ca.ulaval.glo4003.invoice.assembler.InvoiceResponseAssembler;
import ca.ulaval.glo4003.payment.dto.PaymentConfirmationDto;
import ca.ulaval.glo4003.payment.response.PaymentResponse;
import jakarta.inject.Inject;

public class PaymentResponseAssembler {
  private final InvoiceResponseAssembler invoiceResponseAssembler;

  @Inject
  public PaymentResponseAssembler(InvoiceResponseAssembler invoiceResponseAssembler) {
    this.invoiceResponseAssembler = invoiceResponseAssembler;
  }

  public PaymentResponse assemble(PaymentConfirmationDto reservationConfirmed) {
    return new PaymentResponse(
        reservationConfirmed.paymentReceived().toString(),
        reservationConfirmed.totalPaid().toString(),
        reservationConfirmed.totalLeft().toString(),
        this.invoiceResponseAssembler.assemble(reservationConfirmed.invoice()));
  }
}
