package ca.ulaval.glo4003.transport;

import static ca.ulaval.glo4003.transport.TransportType.NONE;

import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.reservation.Reservation;
import ca.ulaval.glo4003.transport.exception.TransportFullyBookedException;
import ca.ulaval.glo4003.transport.exception.TransportNotAvailableException;
import java.util.*;

public class TransportCollection {
  private final Map<TransportType, List<ReservableTransport>> transports;

  public TransportCollection(Map<TransportType, List<ReservableTransport>> transports) {
    this.transports = transports;
  }

  public void reserve(Reservation reservation) {
    if (reservation.getTransportType() != NONE) {
      this.reserveTransport(reservation);
    } else {
      reservation.setTransportCost(new Money());
    }
  }

  public void cancelSeat(Reservation reservation) {
    if (reservation.getTransportType() != NONE) {
      List<ReservableTransport> reservableTransports =
          this.transports.get(reservation.getTransportType());
      for (ReservableTransport transport : reservableTransports) {
        transport.cancelSeat(reservation);
      }
    }
  }

  public List<TransportOption> readCurrentTransportOptions() {
    List<TransportOption> options = new ArrayList<>();
    Collection<List<ReservableTransport>> allTransportsList = this.transports.values();
    allTransportsList.forEach(
        reservableTransportList ->
            reservableTransportList.forEach(
                transport -> options.add(transport.getTransportOption())));
    return options;
  }

  public void addTransport(TransportType transportType, ReservableTransport reservableTransport) {
    List<ReservableTransport> reservableTransports = this.transports.get(transportType);
    reservableTransports.add(reservableTransport);
  }

  public List<ReservableTransport> getTransportsOfType(TransportType transportType) {
    List<ReservableTransport> reservableTransports = this.transports.get(transportType);
    if (reservableTransports == null || reservableTransports.isEmpty()) {
      throw new TransportNotAvailableException();
    }
    return reservableTransports;
  }

  private void reserveTransport(Reservation reservation) {
    List<ReservableTransport> reservableTransports =
        this.getTransportsOfType(reservation.getTransportType());

    ReservableTransport availableTransport =
        reservableTransports.stream()
            .filter(ReservableTransport::isAvailable)
            .findFirst()
            .orElseThrow(TransportFullyBookedException::new);
    availableTransport.reserveSeat(reservation.getTransportStop(), reservation);
  }
}
