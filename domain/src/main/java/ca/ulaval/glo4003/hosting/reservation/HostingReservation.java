package ca.ulaval.glo4003.hosting.reservation;

import ca.ulaval.glo4003.hosting.HostingID;
import ca.ulaval.glo4003.money.Money;
import java.util.Map;

public class HostingReservation {
  private final HostingID hostingID;
  private final String hostingName;
  private final Map<Money, Money> priceRange;

  public HostingReservation(HostingID hostingID, String hostingName, Map<Money, Money> priceRange) {
    this.hostingID = hostingID;
    this.hostingName = hostingName;
    this.priceRange = priceRange;
  }

  public Map<Money, Money> getPriceRange() {
    return this.priceRange;
  }

  public HostingID getHostingID() {
    return this.hostingID;
  }

  public String getHostingName() {
    return this.hostingName;
  }

  public HostingReservation copy() {
    return new HostingReservation(this.hostingID, this.hostingName, this.priceRange);
  }

  @Override
  public boolean equals(Object o) {
    boolean equal = false;

    if (o == this) {
      equal = true;
    } else if (o instanceof HostingReservation otherHostingReservation) {
      equal = this.hostingID.equals(otherHostingReservation.getHostingID());
    }

    return equal;
  }
}
