package ca.ulaval.glo4003.trip;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

public class TripIDTest {
  @Test
  void whenGenerating_thenSameIDIsNotReturnedAgain() {
    TripID first = TripID.generate();
    TripID second = TripID.generate();

    assertNotEquals(first, second);
    assertNotEquals(first.toString(), second.toString());
  }
}
