package ca.ulaval.glo4003.invoice.assembler;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4003.invoice.InvoiceID;
import ca.ulaval.glo4003.invoice.dto.InvoiceDto;
import ca.ulaval.glo4003.invoice.response.InvoiceResponse;
import ca.ulaval.glo4003.invoice.response.InvoiceShortResponse;
import ca.ulaval.glo4003.invoice.response.PaymentTraceResponse;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.payment.PaymentTrace;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InvoiceResponseAssemblerTest {
  private static final String ANY_ITEM_KEY = "item1";
  private static final String ANY_ITEM_COST = "873.78";
  private static final String ANY_TAX_KEY = "tax1";
  private static final String ANY_TAX_COST = "1113.22";
  private static final InvoiceID ANY_INVOICE_ID = InvoiceID.generate();
  private static final String TOTAL_STRING = "548.50";
  private static final String BALANCE_TO_PAY_STRING = "543.90";
  private static final List<PaymentTrace> ANY_PAYMENT_TRACE_LIST = new ArrayList<>();
  private static final List<PaymentTraceResponse> ANY_PAYMENT_TRACE_LIST_RESPONSE =
      new ArrayList<>();
  private InvoiceDto anyInvoiceDto;
  private InvoiceResponseAssembler invoiceResponseAssembler;

  @BeforeEach
  public void setUp() {
    PaymentTraceResponseAssembler paymentTraceResponseAssemblerMock =
        mock(PaymentTraceResponseAssembler.class);
    when(paymentTraceResponseAssemblerMock.assemble(ANY_PAYMENT_TRACE_LIST))
        .thenReturn(ANY_PAYMENT_TRACE_LIST_RESPONSE);
    this.anyInvoiceDto =
        new InvoiceDto(
            ANY_INVOICE_ID,
            this.createMoneyMapOfOneEntry(ANY_ITEM_KEY, ANY_ITEM_COST),
            this.createMoneyMapOfOneEntry(ANY_TAX_KEY, ANY_TAX_COST),
            new Money(TOTAL_STRING),
            new Money(BALANCE_TO_PAY_STRING),
            ANY_PAYMENT_TRACE_LIST);
    this.invoiceResponseAssembler = new InvoiceResponseAssembler(paymentTraceResponseAssemblerMock);
  }

  @Test
  void whenAssembling_thenInvoiceResponseAssembleCorrectly() {
    InvoiceResponse result = this.invoiceResponseAssembler.assemble(this.anyInvoiceDto);

    assertEquals(ANY_INVOICE_ID.toString(), result.invoiceID());
    assertEquals(TOTAL_STRING, result.total());
    assertSame(ANY_PAYMENT_TRACE_LIST_RESPONSE, result.payments());
  }

  @Test
  void whenAssembling_thenInvoiceResponseAssembleItemsMapCorrectly() {
    InvoiceResponse result = this.invoiceResponseAssembler.assemble(this.anyInvoiceDto);

    assertTrue(result.items().containsKey(ANY_ITEM_KEY));
    assertEquals(ANY_ITEM_COST, result.items().get(ANY_ITEM_KEY));
  }

  @Test
  void whenAssembling_thenInvoiceResponseAssembleTaxesMapCorrectly() {
    InvoiceResponse result = this.invoiceResponseAssembler.assemble(this.anyInvoiceDto);

    assertTrue(result.taxes().containsKey(ANY_TAX_KEY));
    assertEquals(ANY_TAX_COST, result.taxes().get(ANY_TAX_KEY));
  }

  @Test
  void whenAssemblingShortResponse_thenInvoiceShortResponseAssembleCorrectly() {
    InvoiceShortResponse assembledInvoiceShortResponse =
        this.invoiceResponseAssembler.assembleShort(this.anyInvoiceDto);

    this.assertInvoiceShortResponseAssembledCorrectly(assembledInvoiceShortResponse);
  }

  private void assertInvoiceShortResponseAssembledCorrectly(
      InvoiceShortResponse assembledInvoiceShortResponse) {
    assertEquals(ANY_INVOICE_ID.toString(), assembledInvoiceShortResponse.invoiceID());
    assertEquals(TOTAL_STRING, assembledInvoiceShortResponse.total());
    assertEquals(BALANCE_TO_PAY_STRING, assembledInvoiceShortResponse.balanceToPay());
  }

  private Map<String, Money> createMoneyMapOfOneEntry(String key, String value) {
    return Map.ofEntries(Map.entry(key, new Money(value)));
  }
}
