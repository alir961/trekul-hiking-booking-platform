package ca.ulaval.glo4003.invoice.assembler;

import static ca.ulaval.glo4003.payment.PaymentMethod.CREDIT;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ca.ulaval.glo4003.invoice.response.PaymentTraceResponse;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.payment.PaymentMethod;
import ca.ulaval.glo4003.payment.PaymentTrace;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PaymentTraceResponseAssemblerTest {
  private static final PaymentMethod FIRST_PAYMENT_TRACE_METHOD = CREDIT;
  private static final String FIRST_PAYMENT_TRACE_PAID = "301.20";
  private static final LocalDate FIRST_PAYMENT_TRACE_DATE = LocalDate.now();
  private static final PaymentTrace FIRST_PAYMENT_TRACE =
      new PaymentTrace(
          FIRST_PAYMENT_TRACE_DATE,
          new Money(FIRST_PAYMENT_TRACE_PAID),
          FIRST_PAYMENT_TRACE_METHOD);
  private static final List<PaymentTrace> PAYMENT_TRACE_LIST = List.of(FIRST_PAYMENT_TRACE);
  private PaymentTraceResponseAssembler paymentTraceResponseAssembler;

  @BeforeEach
  public void setUp() {
    this.paymentTraceResponseAssembler = new PaymentTraceResponseAssembler();
  }

  @Test
  void whenAssembling_thenPaymentTraceResponseAssembleCorrectly() {
    List<PaymentTraceResponse> assembledResponses =
        this.paymentTraceResponseAssembler.assemble(PAYMENT_TRACE_LIST);

    this.assertPaymentTraceResponseAssembleCorrectly(assembledResponses);
  }

  private void assertPaymentTraceResponseAssembleCorrectly(
      List<PaymentTraceResponse> assembledResponses) {
    assertFalse(assembledResponses.isEmpty());
    PaymentTraceResponse result = assembledResponses.getFirst();
    assertEquals(FIRST_PAYMENT_TRACE_METHOD.name(), result.method());
    assertEquals(FIRST_PAYMENT_TRACE_PAID, result.paid());
    assertEquals(FIRST_PAYMENT_TRACE_DATE, result.date());
  }
}
