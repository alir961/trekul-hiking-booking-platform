package ca.ulaval.glo4003.reservation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4003.confirmationcode.ConfirmationCodeFactory;
import ca.ulaval.glo4003.group.Group;
import ca.ulaval.glo4003.invoice.TripInvoice;
import ca.ulaval.glo4003.invoice.TripInvoiceFactory;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.transport.TransportType;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.trip.Trip;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReservationFactoryTest {
  private static final TransportType ANY_TRANSPORT_TYPE = TransportType.ORGANIZED_BUS;
  private static final String ANY_BUS_STOP = "anyBusStop";
  private static final TravelerID ANY_USER_ID = new TravelerID("123456");
  private static final Money ANY_COST = new Money(125.);
  private static final List<TravelerID> ANY_GUESTS_LIST = new ArrayList<>();
  private Trip tripMock;
  private Group groupMock;
  private ReservationConfig reservationConfigMock;
  private TripInvoiceFactory tripInvoiceFactoryMock;
  private ConfirmationCodeFactory confirmationCodeFactoryMock;
  private TripInvoice tripInvoiceMock;
  private ReservationFactory reservationFactory;

  @BeforeEach
  void setUp() {
    this.tripMock = mock(Trip.class);
    this.groupMock = mock(Group.class);
    this.reservationConfigMock = mock(ReservationConfig.class);
    this.tripInvoiceFactoryMock = mock(TripInvoiceFactory.class);
    this.confirmationCodeFactoryMock = mock(ConfirmationCodeFactory.class);
    this.tripInvoiceMock = mock(TripInvoice.class);
    when(this.tripInvoiceFactoryMock.create(ANY_USER_ID, this.tripMock, ANY_GUESTS_LIST))
        .thenReturn(this.tripInvoiceMock);
    when(this.reservationConfigMock.reservationCost()).thenReturn(ANY_COST);
    when(this.reservationConfigMock.reservationExpiration()).thenReturn(Duration.ZERO);

    this.reservationFactory =
        new ReservationFactory(
            this.tripInvoiceFactoryMock,
            this.confirmationCodeFactoryMock,
            this.reservationConfigMock);
  }

  @Test
  void whenCreating_thenReservationWithCorrectFieldsIsReturned() {
    Reservation createdReservation =
        this.reservationFactory.create(
            ANY_USER_ID, this.tripMock, this.groupMock, ANY_BUS_STOP, ANY_TRANSPORT_TYPE);

    assertEquals(this.groupMock, createdReservation.getGroup());
    assertEquals(this.tripInvoiceMock, createdReservation.getInvoice());
    assertEquals(ANY_TRANSPORT_TYPE, createdReservation.getTransportType());
  }

  @Test
  void givenMultipleCreation_whenCreating_thenReservationIDAreNotSame() {
    Reservation first =
        this.reservationFactory.create(
            ANY_USER_ID, this.tripMock, this.groupMock, ANY_BUS_STOP, ANY_TRANSPORT_TYPE);
    Reservation second =
        this.reservationFactory.create(
            ANY_USER_ID, this.tripMock, this.groupMock, ANY_BUS_STOP, ANY_TRANSPORT_TYPE);

    assertNotEquals(first.getReservationID(), second.getReservationID());
  }
}
