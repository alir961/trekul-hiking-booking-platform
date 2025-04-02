package ca.ulaval.glo4003.hosting.reservation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.hosting.Hosting;
import ca.ulaval.glo4003.hosting.reservation.exception.HostingReservationExistingForTripException;
import ca.ulaval.glo4003.hosting.reservation.exception.HostingReservationImpossibleException;
import ca.ulaval.glo4003.hosting.reservation.exception.HostingReservationUnavailableException;
import ca.ulaval.glo4003.trip.Trip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HostingReservationValidatorTest {
  private Trip tripMock;
  private Hosting hostingMock;
  private HostingReservationValidator hostingReservationValidator;

  @BeforeEach
  void setUp() {
    this.tripMock = mock(Trip.class);
    this.hostingMock = mock(Hosting.class);
    this.hostingReservationValidator = new HostingReservationValidator();
  }

  @Test
  void
      givenExistingHostingReservation_whenValidatingIfReservationValid_thenHostingReservationExistingForTripException() {
    when(this.tripMock.hasHostingReservation()).thenReturn(true);

    assertThrows(
        HostingReservationExistingForTripException.class,
        () -> this.hostingReservationValidator.validate(this.hostingMock, this.tripMock));
  }

  @Test
  void
      givenHostingIncompatibleWithTrip_whenValidatingIfReservationValid_thenHostingReservationImpossibleException() {
    when(this.tripMock.hasHostingReservation()).thenReturn(false);
    when(this.hostingMock.isCompatibleWithTrip(this.tripMock)).thenReturn(false);

    assertThrows(
        HostingReservationImpossibleException.class,
        () -> this.hostingReservationValidator.validate(this.hostingMock, this.tripMock));
  }

  @Test
  void
      givenHostingAndTripCompatibleButTripNotBlocked_whenValidatingIfReservationValid_thenHostingReservationUnavailableException() {
    when(this.tripMock.hasHostingReservation()).thenReturn(false);
    when(this.hostingMock.isCompatibleWithTrip(this.tripMock)).thenReturn(true);
    when(this.tripMock.isBlocked()).thenReturn(false);

    assertThrows(
        HostingReservationUnavailableException.class,
        () -> this.hostingReservationValidator.validate(this.hostingMock, this.tripMock));
  }

  @Test
  void
      givenHostingAndTripCompatibleAndTripBlocked_whenValidatingIfReservationValid_thenDoNothing() {
    when(this.tripMock.hasHostingReservation()).thenReturn(false);
    when(this.hostingMock.isCompatibleWithTrip(this.tripMock)).thenReturn(true);
    when(this.tripMock.isBlocked()).thenReturn(true);

    assertDoesNotThrow(
        () -> this.hostingReservationValidator.validate(this.hostingMock, this.tripMock));
  }
}
