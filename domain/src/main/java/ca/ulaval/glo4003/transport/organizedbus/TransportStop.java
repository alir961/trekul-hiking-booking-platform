package ca.ulaval.glo4003.transport.organizedbus;

import java.time.LocalDateTime;

public class TransportStop {
  private TransportStopLocation departureLocation;
  private LocalDateTime departureTime;

  public TransportStop(TransportStopLocation departureLocation, LocalDateTime departureTime) {
    this.departureLocation = departureLocation;
    this.departureTime = departureTime;
  }

  public boolean isNamed(String name) {
    return this.departureLocation.getName().equals(name);
  }

  public String getLocationName() {
    return this.departureLocation.getName();
  }

  public LocalDateTime getDepartureTime() {
    return this.departureTime;
  }
}
