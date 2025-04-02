package ca.ulaval.glo4003.transport;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

public class TransportIDTest {
  @Test
  void whenGenerating_thenSameIDIsNotReturnedAgain() {
    TransportID first = TransportID.generate();
    TransportID second = TransportID.generate();

    assertNotEquals(first, second);
    assertNotEquals(first.toString(), second.toString());
  }
}
