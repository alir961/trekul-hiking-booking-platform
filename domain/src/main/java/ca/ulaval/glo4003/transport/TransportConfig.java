package ca.ulaval.glo4003.transport;

import ca.ulaval.glo4003.money.Money;

public class TransportConfig {
  private Money cost;
  private int maximumSeats;

  public TransportConfig(Money cost, int maximumSeats) {
    this.cost = cost;
    this.maximumSeats = maximumSeats;
  }

  public Money getCost() {
    return this.cost;
  }

  public int getMaximumSeats() {
    return this.maximumSeats;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    } else if (o instanceof TransportConfig otherConfig) {
      return this.cost.equals(otherConfig.cost) && this.maximumSeats == otherConfig.maximumSeats;
    }

    return false;
  }
}
