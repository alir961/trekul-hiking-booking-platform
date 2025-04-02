package ca.ulaval.glo4003.reservation;

import static ca.ulaval.glo4003.invoice.BillableItems.RESERVATION;

import ca.ulaval.glo4003.confirmationcode.ConfirmationCode;
import ca.ulaval.glo4003.confirmationcode.ConfirmationCodeFactory;
import ca.ulaval.glo4003.group.Group;
import ca.ulaval.glo4003.invoice.TripInvoice;
import ca.ulaval.glo4003.invoice.TripInvoiceFactory;
import ca.ulaval.glo4003.transport.TransportType;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.trip.Trip;
import java.time.ZonedDateTime;

public class ReservationFactory {
  private final TripInvoiceFactory tripInvoiceFactory;
  private final ConfirmationCodeFactory confirmationCodeFactory;
  private final ReservationConfig reservationConfig;

  public ReservationFactory(
      TripInvoiceFactory tripInvoiceFactory,
      ConfirmationCodeFactory confirmationCodeFactory,
      ReservationConfig reservationConfig) {
    this.tripInvoiceFactory = tripInvoiceFactory;
    this.confirmationCodeFactory = confirmationCodeFactory;
    this.reservationConfig = reservationConfig;
  }

  public Reservation create(
      TravelerID userID,
      Trip trip,
      Group group,
      String transportStop,
      TransportType transportType) {
    TripInvoice tripInvoice = this.tripInvoiceFactory.create(userID, trip, group.getGuestsIDs());
    tripInvoice.saveItem(RESERVATION, this.reservationConfig.reservationCost());
    tripInvoice.updateInvoicesWithItemMinimumToPay(RESERVATION);
    ZonedDateTime reservationCreation = ZonedDateTime.now();
    ZonedDateTime reservationExpiration =
        reservationCreation.plusMinutes(this.reservationConfig.reservationExpiration().toMinutes());
    ConfirmationCode confirmationCode = this.confirmationCodeFactory.createConfirmationCode();
    return new Reservation(
        ReservationID.generate(),
        tripInvoice,
        group,
        reservationCreation,
        reservationExpiration,
        confirmationCode,
        transportStop,
        transportType);
  }
}
