package ca.ulaval.glo4003.transport;

import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.reservation.Reservation;
import ca.ulaval.glo4003.transport.organizedbus.TransportStop;
import java.util.Collection;

public interface ReservableTransport {
  void reserveSeat(String stop, Reservation reservation);

  void cancelSeat(Reservation reservation);

  int getCurrentSeatsAvailable();

  TransportOption getTransportOption();

  TransportID getTransportID();

  Money getCost();

  Collection<TransportStop> getStops();

  boolean isAvailable();
}
