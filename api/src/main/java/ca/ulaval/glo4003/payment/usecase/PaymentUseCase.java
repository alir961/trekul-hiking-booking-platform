package ca.ulaval.glo4003.payment.usecase;

import ca.ulaval.glo4003.invoice.InvoiceID;
import ca.ulaval.glo4003.payment.PaymentInformation;
import ca.ulaval.glo4003.payment.PaymentService;
import ca.ulaval.glo4003.payment.assembler.PaymentRequestAssembler;
import ca.ulaval.glo4003.payment.assembler.PaymentResponseAssembler;
import ca.ulaval.glo4003.payment.dto.PaymentConfirmationDto;
import ca.ulaval.glo4003.payment.request.PaymentRequest;
import ca.ulaval.glo4003.payment.response.PaymentResponse;
import ca.ulaval.glo4003.traveler.TravelerID;
import jakarta.inject.Inject;

public class PaymentUseCase {

  private final PaymentService paymentService;
  private final PaymentRequestAssembler paymentRequestAssembler;
  private final PaymentResponseAssembler paymentResponseAssembler;

  @Inject
  public PaymentUseCase(
      PaymentService paymentService,
      PaymentRequestAssembler paymentRequestAssembler,
      PaymentResponseAssembler paymentResponseAssembler) {
    this.paymentService = paymentService;
    this.paymentRequestAssembler = paymentRequestAssembler;
    this.paymentResponseAssembler = paymentResponseAssembler;
  }

  public PaymentResponse payInvoice(
      PaymentRequest paymentRequest, InvoiceID invoiceID, TravelerID ownerID, TravelerID payerID) {
    PaymentInformation paymentInformation = this.paymentRequestAssembler.assemble(paymentRequest);

    PaymentConfirmationDto paymentConfirmationDto =
        this.paymentService.payInvoice(paymentInformation, invoiceID, ownerID, payerID);
    return this.paymentResponseAssembler.assemble(paymentConfirmationDto);
  }
}
