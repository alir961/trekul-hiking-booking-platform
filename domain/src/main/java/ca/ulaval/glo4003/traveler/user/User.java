package ca.ulaval.glo4003.traveler.user;

import ca.ulaval.glo4003.confirmationcode.ConfirmationCode;
import ca.ulaval.glo4003.confirmationcode.ValidationResult;
import ca.ulaval.glo4003.invoice.BillableItems;
import ca.ulaval.glo4003.invoice.InvoiceID;
import ca.ulaval.glo4003.invoice.TripInvoice;
import ca.ulaval.glo4003.invoice.exception.InvoiceNotFoundException;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.payment.PaymentInformation;
import ca.ulaval.glo4003.payment.PaymentProcessor;
import ca.ulaval.glo4003.reservation.Reservation;
import ca.ulaval.glo4003.reservation.ReservationID;
import ca.ulaval.glo4003.reservation.ReservationStatus;
import ca.ulaval.glo4003.reservation.exception.ReservationNotFoundException;
import ca.ulaval.glo4003.traveler.Contactable;
import ca.ulaval.glo4003.traveler.PersonName;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.user.profile.Profile;
import ca.ulaval.glo4003.traveler.user.role.Permission;
import ca.ulaval.glo4003.traveler.user.role.Role;
import ca.ulaval.glo4003.traveler.user.role.RolesCollection;
import ca.ulaval.glo4003.trip.TripID;
import java.time.LocalDate;
import java.util.*;

public class User implements Contactable {
  private final Profile profile;
  private final RolesCollection roles;
  private final Map<ReservationID, Reservation> reservations = new HashMap<>();
  private final List<TripID> tripNotifications = new ArrayList<>();
  private Money credits = new Money(0.);

  public User(Profile profile, RolesCollection roles) {
    this.profile = profile;
    this.roles = roles;
  }

  @Override
  public PersonName getName() {
    return this.profile.name();
  }

  public TravelerID getId() {
    return this.profile.idul();
  }

  public void validatePassword(String password) {
    this.profile.validatePassword(password);
  }

  @Override
  public String getEmail() {
    return this.profile.getEmail();
  }

  public Money getCredits() {
    return this.credits;
  }

  public void addReservation(Reservation reservation) {
    this.reservations.put(reservation.getReservationID(), reservation);
  }

  public Optional<TripInvoice> getTripInvoice(InvoiceID invoiceID) {
    return this.reservations.values().stream()
        .filter(reservation -> reservation.getInvoiceID().equals(invoiceID))
        .map(Reservation::getInvoice)
        .findFirst();
  }

  public Optional<Reservation> getReservationByInvoiceID(InvoiceID invoiceID) {
    return this.reservations.values().stream()
        .filter(reservation -> reservation.getInvoiceID().equals(invoiceID))
        .findFirst();
  }

  public Collection<Reservation> getReservations() {
    return this.reservations.values();
  }

  public Collection<TripInvoice> getInvoices() {
    return this.reservations.values().stream()
        .map(Reservation::getInvoice)
        .filter(Objects::nonNull)
        .toList();
  }

  public TripInvoice getInvoiceForTrip(TripID tripID) {
    return this.reservations.values().stream()
        .filter(reservation -> reservation.getTripID().equals(tripID))
        .findFirst()
        .orElseThrow(InvoiceNotFoundException::new)
        .getInvoice();
  }

  public ConfirmationCode retrieveConfirmationCodeForReservation(InvoiceID invoiceID) {

    TripInvoice tripInvoice =
        this.getTripInvoice(invoiceID).orElseThrow(InvoiceNotFoundException::new);

    return this.getReservations().stream()
        .filter(
            t ->
                t.getInvoiceID().equals(tripInvoice.getInvoiceID())
                    && t.getTripID().equals(tripInvoice.getTripID()))
        .findFirst()
        .orElseThrow(ReservationNotFoundException::new)
        .getConfirmationCode();
  }

  public void addRole(Role role) {
    this.roles.addRole(role);
  }

  public boolean hasPermission(Permission permission) {
    return this.roles.hasPermission(permission);
  }

  public RolesCollection getRoles() {
    return this.roles.copy();
  }

  public User copy() {
    User copied = new User(this.profile, this.roles.copy());
    copied.reservations.putAll(this.reservations);
    copied.credits = this.credits;

    return copied;
  }

  public void notifiedForTrip(TripID tripID) {
    this.tripNotifications.add(tripID);
  }

  public boolean hasBeenNotifiedForTrip(TripID tripID) {
    return this.tripNotifications.contains(tripID);
  }

  public boolean hasConfirmationCode(String confirmationCode) {
    return this.reservations.values().stream()
        .anyMatch(reservation -> reservation.validateConfirmationCode(confirmationCode));
  }

  public ValidationResult validateTripInvoiceForTrip(TripID tripID) {
    return this.getReservationByTripID(tripID).createValidationResult();
  }

  public boolean hasReservation(TripID tripID) {
    return this.reservations.values().stream()
        .anyMatch(reservation -> reservation.getTripID().equals(tripID));
  }

  public Optional<Reservation> findFirstReservationOfTrip(TripID tripID) {
    return this.reservations.values().stream()
        .filter(reservation -> reservation.getTripID().equals(tripID))
        .findFirst();
  }

  @Override
  public boolean equals(Object o) {
    boolean equal = false;

    if (o == this) {
      equal = true;
    } else if (o instanceof User otherUser) {
      equal = this.getId().equals(otherUser.getId());
    }

    return equal;
  }

  public void saveItemForTrip(TripID tripID, BillableItems billableItems, Money money) {
    TripInvoice tripInvoice = this.getInvoiceForTrip(tripID);
    tripInvoice.saveItem(billableItems, money);
  }

  public Collection<Reservation> fetchReservationsOfStatus(ReservationStatus status) {
    return this.reservations.values().stream()
        .filter(reservation -> reservation.hasStatus(status))
        .toList();
  }

  public Profile getProfile() {
    return this.profile;
  }

  public Reservation cancelReservation(ReservationID reservationID, LocalDate tripDate) {
    Reservation reservationToCancel = this.getReservationById(reservationID);
    reservationToCancel.cancel(tripDate);
    this.credits = reservationToCancel.calculateUserCredits();
    return reservationToCancel;
  }

  public Collection<Reservation> cancelExpiredReservations() {
    List<Reservation> expiredReservations =
        this.reservations.values().stream().filter(Reservation::isExpired).toList();
    expiredReservations.forEach(
        reservation -> this.reservations.remove(reservation.getReservationID()));

    return expiredReservations;
  }

  public TripID getTripIDForReservation(ReservationID reservationID) {
    Reservation reservation = this.getReservationById(reservationID);
    return reservation.getTripID();
  }

  private Reservation getReservationById(ReservationID reservationID) {
    Reservation reservation = this.reservations.get(reservationID);
    if (reservation == null) {
      throw new ReservationNotFoundException();
    }

    return reservation;
  }

  public Reservation payReservation(
      PaymentProcessor paymentProcessor,
      PaymentInformation paymentInformation,
      InvoiceID invoiceID,
      TravelerID payerID) {
    Reservation reservation =
        this.getReservationByInvoiceID(invoiceID).orElseThrow(ReservationNotFoundException::new);
    reservation.payReservation(paymentProcessor, paymentInformation, payerID);
    this.addReservation(reservation);
    return reservation;
  }

  private Reservation getReservationByTripID(TripID tripID) {
    return this.reservations.values().stream()
        .filter(reservation -> reservation.getTripID().equals(tripID))
        .findFirst()
        .orElseThrow(ReservationNotFoundException::new);
  }
}
