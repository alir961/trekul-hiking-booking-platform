package ca.ulaval.glo4003.payment.assembler;

import ca.ulaval.glo4003.invoice.TripInvoice;
import ca.ulaval.glo4003.invoice.assembler.InvoiceDtoAssembler;
import ca.ulaval.glo4003.payment.PaymentInformation;
import ca.ulaval.glo4003.payment.dto.PaymentConfirmationDto;
import ca.ulaval.glo4003.traveler.TravelerID;

public class PaymentConfirmationDtoAssembler {
  private final InvoiceDtoAssembler invoiceDtoAssembler;

  public PaymentConfirmationDtoAssembler(InvoiceDtoAssembler invoiceDtoAssembler) {
    this.invoiceDtoAssembler = invoiceDtoAssembler;
  }

  public PaymentConfirmationDto assemble(
      PaymentInformation paymentInformation, TripInvoice invoice, TravelerID travelerID) {
    return new PaymentConfirmationDto(
        paymentInformation.payment(),
        invoice.getBalancePaid(travelerID),
        invoice.calculateBalanceToPay(travelerID),
        this.invoiceDtoAssembler.assemble(invoice, travelerID));
  }
}
