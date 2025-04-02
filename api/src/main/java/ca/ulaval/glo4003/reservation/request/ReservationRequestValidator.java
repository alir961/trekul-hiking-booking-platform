package ca.ulaval.glo4003.reservation.request;

import ca.ulaval.glo4003.transport.TransportType;
import ca.ulaval.glo4003.transport.exception.TransportInvalidRequestException;
import ca.ulaval.glo4003.transport.exception.TransportInvalidTypeException;
import java.util.Arrays;

public class ReservationRequestValidator {
  public void validate(ReservationRequest reservationRequest) {
    String receivedTransportType = reservationRequest.transportType().toUpperCase();
    if (Arrays.stream(TransportType.values())
        .noneMatch(
            transportType ->
                transportType.toString().toUpperCase().equals(receivedTransportType))) {
      throw new TransportInvalidTypeException();
    }
    if (receivedTransportType.equals(TransportType.ORGANIZED_BUS.toString())
        && reservationRequest.stop() == null) {
      throw new TransportInvalidRequestException();
    }
  }
}
