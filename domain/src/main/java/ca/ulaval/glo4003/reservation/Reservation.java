package ca.ulaval.glo4003.reservation;

import static ca.ulaval.glo4003.reservation.ReservationStatus.CANCELLED;
import static ca.ulaval.glo4003.reservation.ReservationStatus.CONFIRMED;

import ca.ulaval.glo4003.confirmationcode.ConfirmationCode;
import ca.ulaval.glo4003.confirmationcode.ValidationResult;
import ca.ulaval.glo4003.group.Group;
import ca.ulaval.glo4003.invoice.BillableItems;
import ca.ulaval.glo4003.invoice.InvoiceID;
import ca.ulaval.glo4003.invoice.TripInvoice;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.payment.PaymentInformation;
import ca.ulaval.glo4003.payment.PaymentProcessor;
import ca.ulaval.glo4003.reservation.exception.ReservationInvalidStatusException;
import ca.ulaval.glo4003.transport.TransportType;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.guest.Guest;
import ca.ulaval.glo4003.trip.TripID;
import java.time.*;
import java.util.Collection;

public class Reservation {
  private final ReservationID reservationID;
  private final TripInvoice tripInvoice;
  private final Group group;
  private final ZonedDateTime createdAt;
  private ZonedDateTime expiresAt;
  private TransportType transportType;
  private String transportStop;
  private ReservationStatus status;
  private ConfirmationCode confirmationCode;

  public Reservation(
      ReservationID reservationID,
      TripInvoice tripInvoice,
      Group group,
      ZonedDateTime createdAt,
      ZonedDateTime expiresAt,
      ConfirmationCode confirmationCode,
      String transportStop,
      TransportType transportType) {
    this.reservationID = reservationID;
    this.tripInvoice = tripInvoice;
    this.group = group;
    this.createdAt = createdAt;
    this.expiresAt = expiresAt;
    this.confirmationCode = confirmationCode;
    this.status = ReservationStatus.PENDING;
    this.transportStop = transportStop;
    this.transportType = transportType;
  }

  public Group getGroup() {
    return this.group;
  }

  public Collection<Guest> listGuests() {
    return this.group.getGuests();
  }

  public boolean isExpired() {
    ZonedDateTime currentTime = ZonedDateTime.now();
    return this.status == ReservationStatus.PENDING && currentTime.isAfter(this.expiresAt);
  }

  public TravelerID getUserID() {
    return this.tripInvoice.getUserID();
  }

  public TripID getTripID() {
    return this.tripInvoice.getTripID();
  }

  public InvoiceID getInvoiceID() {
    return this.tripInvoice.getID();
  }

  public ReservationID getReservationID() {
    return this.reservationID;
  }

  public void setTransportCost(Money cost) {
    this.tripInvoice.saveItem(BillableItems.TRANSPORT, cost);
  }

  public TransportType getTransportType() {
    return this.transportType;
  }

  public void setStatus(ReservationStatus status) {
    this.status = status;
  }

  public String getTransportStop() {
    return this.transportStop;
  }

  public ReservationStatus getStatus() {
    return this.status;
  }

  public TripInvoice getInvoice() {
    return this.tripInvoice;
  }

  public ZonedDateTime getExpiration() {
    return this.expiresAt;
  }

  public ConfirmationCode getConfirmationCode() {
    return this.confirmationCode;
  }

  public boolean validateConfirmationCode(String code) {
    return this.confirmationCode.verifyConfirmationCode(code);
  }

  public ValidationResult createValidationResult() {
    return this.confirmationCode.createValidationResult(
        this.tripInvoice, this.transportType, this.status);
  }

  public Reservation copy() {
    Reservation copied =
        new Reservation(
            this.reservationID,
            this.tripInvoice,
            this.group,
            this.createdAt,
            this.expiresAt,
            this.confirmationCode,
            this.transportStop,
            this.transportType);
    copied.status = this.status;

    return copied;
  }

  @Override
  public boolean equals(Object o) {
    boolean equal = false;

    if (o == this) {
      equal = true;
    } else if (o instanceof Reservation otherReservation) {
      equal =
          this.getUserID().equals(otherReservation.getUserID())
              && this.getTripID().equals(otherReservation.getTripID());
    }

    return equal;
  }

  public int calculateTotalTravelers() {
    return this.group.calculateTotalTravelers();
  }

  public boolean hasStatus(ReservationStatus status) {
    return this.status.equals(status);
  }

  public void setExpirationDate(ZonedDateTime expirationDate) {
    this.expiresAt = expirationDate;
  }

  public void cancel(LocalDate tripDate) {
    if (this.status != CONFIRMED) {
      throw new ReservationInvalidStatusException(this.status);
    }
    this.tripInvoice.refund(tripDate);
    this.status = CANCELLED;
  }

  public void payReservation(
      PaymentProcessor paymentProcessor,
      PaymentInformation paymentInformation,
      TravelerID payerID) {
    paymentProcessor.processPayment(paymentInformation, this.getInvoice(), payerID);
    this.setStatus(ReservationStatus.CONFIRMED);
  }

  public Money calculateUserCredits() {
    return this.tripInvoice.calculateUserCredits();
  }
}
