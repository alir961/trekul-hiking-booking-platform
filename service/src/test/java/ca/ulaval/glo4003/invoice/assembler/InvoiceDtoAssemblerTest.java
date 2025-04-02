package ca.ulaval.glo4003.invoice.assembler;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4003.invoice.BillableItems;
import ca.ulaval.glo4003.invoice.InvoiceID;
import ca.ulaval.glo4003.invoice.TripInvoice;
import ca.ulaval.glo4003.invoice.dto.InvoiceDto;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.traveler.TravelerID;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InvoiceDtoAssemblerTest {
  private static final TravelerID ANY_TRAVELER_ID = new TravelerID("221023");
  private static final InvoiceID TRIP_INVOICE_STUB_ID = InvoiceID.generate();
  private static final Money TRIP_INVOICE_STUB_AFTER_TAXES = new Money("5403");
  private static final Money TRIP_INVOICE_STUB_BALANCE_TO_PAY = new Money("4002");
  private static final Map<BillableItems, Money> TRIP_INVOICE_STUB_ITEMS =
      new EnumMap<>(BillableItems.class);
  private static final Map<String, Money> TRIP_INVOICE_STUB_TAXES = new HashMap<>();
  private TripInvoice tripInvoiceStub = mock(TripInvoice.class);
  private InvoiceDtoAssembler invoiceDtoAssembler;

  @BeforeEach
  public void setUp() {
    when(this.tripInvoiceStub.getID()).thenReturn(TRIP_INVOICE_STUB_ID);
    when(this.tripInvoiceStub.getItems(ANY_TRAVELER_ID)).thenReturn(TRIP_INVOICE_STUB_ITEMS);
    when(this.tripInvoiceStub.calculateTaxes(ANY_TRAVELER_ID)).thenReturn(TRIP_INVOICE_STUB_TAXES);
    when(this.tripInvoiceStub.calculateTotalAfterTaxes(ANY_TRAVELER_ID))
        .thenReturn(TRIP_INVOICE_STUB_AFTER_TAXES);
    when(this.tripInvoiceStub.calculateBalanceToPay(ANY_TRAVELER_ID))
        .thenReturn(TRIP_INVOICE_STUB_BALANCE_TO_PAY);
    this.invoiceDtoAssembler = new InvoiceDtoAssembler();
  }

  @Test
  void whenAssembling_thenInvoiceDtoAssembleCorrectly() {
    InvoiceDto result = this.invoiceDtoAssembler.assemble(this.tripInvoiceStub, ANY_TRAVELER_ID);

    assertEquals(TRIP_INVOICE_STUB_ID, result.invoiceID());
    assertEquals(TRIP_INVOICE_STUB_AFTER_TAXES, result.total());
    assertEquals(TRIP_INVOICE_STUB_BALANCE_TO_PAY, result.balanceToPay());
  }

  @Test
  void whenAssembling_thenInvoiceDtoAssembleItemsMapCorrectly() {
    InvoiceDto result = this.invoiceDtoAssembler.assemble(this.tripInvoiceStub, ANY_TRAVELER_ID);

    assertSame(TRIP_INVOICE_STUB_TAXES, result.taxes());
  }
}
