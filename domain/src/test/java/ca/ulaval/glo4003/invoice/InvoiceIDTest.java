package ca.ulaval.glo4003.invoice;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

public class InvoiceIDTest {
  @Test
  void whenGenerating_thenSameIDIsNotReturnedAgain() {
    InvoiceID first = InvoiceID.generate();
    InvoiceID second = InvoiceID.generate();

    assertNotEquals(first, second);
    assertNotEquals(first.toString(), second.toString());
  }
}
