package ca.ulaval.glo4003.invoice.assembler;

import ca.ulaval.glo4003.invoice.BillableItems;
import ca.ulaval.glo4003.invoice.TripInvoice;
import ca.ulaval.glo4003.invoice.dto.InvoiceDto;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.traveler.TravelerID;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvoiceDtoAssembler {
  public InvoiceDto assemble(TripInvoice invoice, TravelerID travelerID) {
    Map<String, Money> itemsMapped = new HashMap<>();
    Map<BillableItems, Money> items = invoice.getItems(travelerID);
    for (Map.Entry<BillableItems, Money> entry : items.entrySet()) {
      itemsMapped.put(entry.getKey().toString(), entry.getValue());
    }

    return new InvoiceDto(
        invoice.getID(),
        itemsMapped,
        invoice.calculateTaxes(travelerID),
        invoice.calculateTotalAfterTaxes(travelerID),
        invoice.calculateBalanceToPay(travelerID),
        invoice.getPaymentList(travelerID));
  }

  public List<InvoiceDto> assemble(Collection<TripInvoice> invoices) {
    return invoices.stream().map(invoice -> this.assemble(invoice, invoice.getUserID())).toList();
  }
}
