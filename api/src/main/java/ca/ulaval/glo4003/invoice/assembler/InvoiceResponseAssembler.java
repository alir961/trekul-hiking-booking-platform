package ca.ulaval.glo4003.invoice.assembler;

import ca.ulaval.glo4003.invoice.dto.InvoiceDto;
import ca.ulaval.glo4003.invoice.response.InvoiceResponse;
import ca.ulaval.glo4003.invoice.response.InvoiceShortResponse;
import ca.ulaval.glo4003.money.Money;
import jakarta.inject.Inject;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvoiceResponseAssembler {
  private final PaymentTraceResponseAssembler paymentTraceResponseAssembler;

  @Inject
  public InvoiceResponseAssembler(PaymentTraceResponseAssembler paymentTraceResponseAssembler) {
    this.paymentTraceResponseAssembler = paymentTraceResponseAssembler;
  }

  public InvoiceResponse assemble(InvoiceDto invoiceDto) {
    return new InvoiceResponse(
        invoiceDto.invoiceID().toString(),
        this.mapMoneyToString(invoiceDto.items()),
        this.mapMoneyToString(invoiceDto.taxes()),
        invoiceDto.total().toString(),
        invoiceDto.balanceToPay().toString(),
        this.paymentTraceResponseAssembler.assemble(invoiceDto.payments()));
  }

  public InvoiceShortResponse assembleShort(InvoiceDto invoiceDto) {
    return new InvoiceShortResponse(
        invoiceDto.invoiceID().toString(),
        invoiceDto.total().toString(),
        invoiceDto.balanceToPay().toString());
  }

  public List<InvoiceShortResponse> assembleShort(Collection<InvoiceDto> invoiceDto) {
    return invoiceDto.stream().map(this::assembleShort).toList();
  }

  private Map<String, String> mapMoneyToString(Map<String, Money> map) {
    Map<String, String> result = new HashMap<>();
    for (Map.Entry<String, Money> entry : map.entrySet()) {
      result.put(entry.getKey(), entry.getValue().toString());
    }
    return result;
  }
}
