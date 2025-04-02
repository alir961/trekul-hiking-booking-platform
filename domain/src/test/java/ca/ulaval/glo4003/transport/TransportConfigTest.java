package ca.ulaval.glo4003.transport;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ca.ulaval.glo4003.money.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TransportConfigTest {
  private static final Money ANY_COST = new Money(5.0);
  private static final int ANY_MAXIMUM_SEAT = 5;
  private TransportConfig transportConfig;

  @BeforeEach
  void setUp() {
    this.transportConfig = new TransportConfig(ANY_COST, ANY_MAXIMUM_SEAT);
  }

  @Test
  void whenGettingCost_thenGetsCorrectValue() {
    Money receivedCost = this.transportConfig.getCost();

    assertEquals(ANY_COST, receivedCost);
  }

  @Test
  void whenGettingMaximumSeats_thenGetsCorrectValue() {
    int receivedMaximumSeats = this.transportConfig.getMaximumSeats();

    assertEquals(ANY_MAXIMUM_SEAT, receivedMaximumSeats);
  }
}
