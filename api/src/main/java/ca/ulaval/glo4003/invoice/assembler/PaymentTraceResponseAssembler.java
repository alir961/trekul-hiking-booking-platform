package ca.ulaval.glo4003.invoice.assembler;

import ca.ulaval.glo4003.invoice.response.PaymentTraceResponse;
import ca.ulaval.glo4003.payment.PaymentTrace;
import java.util.Collection;
import java.util.List;

public class PaymentTraceResponseAssembler {
  public List<PaymentTraceResponse> assemble(Collection<PaymentTrace> paymentTraces) {
    return paymentTraces.stream().map(this::assemble).toList();
  }

  private PaymentTraceResponse assemble(PaymentTrace paymentTrace) {
    return new PaymentTraceResponse(
        paymentTrace.date(), paymentTrace.paid().toString(), paymentTrace.method().name());
  }
}
