package ca.ulaval.glo4003.traveler.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.confirmationcode.ConfirmationCode;
import ca.ulaval.glo4003.confirmationcode.ValidationResult;
import ca.ulaval.glo4003.invoice.InvoiceID;
import ca.ulaval.glo4003.invoice.TripInvoice;
import ca.ulaval.glo4003.invoice.exception.InvoiceNotFoundException;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.payment.PaymentInformation;
import ca.ulaval.glo4003.payment.PaymentProcessor;
import ca.ulaval.glo4003.reservation.Reservation;
import ca.ulaval.glo4003.reservation.ReservationID;
import ca.ulaval.glo4003.reservation.exception.ReservationNotFoundException;
import ca.ulaval.glo4003.traveler.PersonName;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.user.credential.UserCredential;
import ca.ulaval.glo4003.traveler.user.profile.Profile;
import ca.ulaval.glo4003.traveler.user.role.Role;
import ca.ulaval.glo4003.traveler.user.role.RolesCollection;
import ca.ulaval.glo4003.trip.TripID;
import java.time.LocalDate;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserTest {
  private static final String ANY_USER_FIRSTNAME = "Joey";
  private static final String ANY_USER_LASTNAME = "Any last name";
  private static final PersonName ANY_USER_FULLNAME =
      new PersonName(ANY_USER_FIRSTNAME, ANY_USER_LASTNAME);
  private static final Reservation ANY_RESERVATION_DUMMY = mock(Reservation.class);
  private static final TravelerID ANY_USER_ID = new TravelerID("id");
  private static final TravelerID ANY_OTHER_USER_ID = new TravelerID("id2");
  private static final InvoiceID VALID_INVOICE_ID = InvoiceID.generate();
  private static final TripID VALID_TRIP_ID = TripID.generate();
  private static final ReservationID ANY_RESERVATION_ID = ReservationID.generate();
  private static final ReservationID ANOTHER_RESERVATION_ID = ReservationID.generate();
  private static final ConfirmationCode EXPECTED_CONFIRMATION_CODE =
      new ConfirmationCode("CONFIRM-12345");
  private static final InvoiceID INVALID_INVOICE_ID = InvoiceID.generate();
  private static final LocalDate ANY_BIRTH_DATE = LocalDate.now();
  private static final LocalDate ANY_TRIP_DATE = LocalDate.of(2024, 11, 15);
  private static final Gender ANY_GENDER = Gender.GENDER_UNSPECIFIED;
  private static final Role ANY_ROLE = mock(Role.class);
  private static final Money ANY_CREDITS_AMOUNT = new Money(45.);
  private Profile anyUserProfile;
  private UserCredential anyUserCredential;
  private RolesCollection rolesCollectionSpy;
  private ValidationResult expectedResultMock;
  private TripInvoice tripInvoiceMock;
  private Reservation expiredReservationMock;
  private Reservation activeReservationMock;
  private PaymentProcessor paymentProcessorMock;
  private PaymentInformation paymentInformationMock;
  private Reservation payedReservationMock;
  private User anyUser;

  @BeforeEach
  void setUp() {
    this.anyUserCredential = mock(UserCredential.class);
    this.anyUserProfile =
        new Profile(
            ANY_USER_FULLNAME, ANY_BIRTH_DATE, ANY_GENDER, ANY_USER_ID, this.anyUserCredential);
    this.rolesCollectionSpy = mock(RolesCollection.class);
    this.expectedResultMock = mock(ValidationResult.class);
    this.tripInvoiceMock = mock(TripInvoice.class);
    this.expiredReservationMock = mock(Reservation.class);
    this.activeReservationMock = mock(Reservation.class);
    this.paymentProcessorMock = mock(PaymentProcessor.class);
    this.paymentInformationMock = mock(PaymentInformation.class);
    this.payedReservationMock = mock(Reservation.class);

    this.anyUser = new User(this.anyUserProfile, this.rolesCollectionSpy);
  }

  @Test
  void whenAddingReservation_thenReservationIsSaved() {
    this.anyUser.addReservation(ANY_RESERVATION_DUMMY);

    Collection<Reservation> userSavedReservation = this.anyUser.getReservations();
    assertTrue(userSavedReservation.contains(ANY_RESERVATION_DUMMY));
  }

  @Test
  void whenAddingRole_thenRoleIsSaved() {
    this.anyUser.addRole(ANY_ROLE);

    verify(this.rolesCollectionSpy).addRole(ANY_ROLE);
  }

  @Test
  void whenGettingEmailAddress_thenGetEmailAddressFromUserCredential() {
    this.anyUser.getEmail();

    verify(this.anyUserCredential).getEmailAddress();
  }

  @Test
  void givenReservationtHasBeenAdded_whenCopyingUser_thenCopiedUserContainsReservation() {
    this.anyUser.addReservation(ANY_RESERVATION_DUMMY);
    User copied = this.anyUser.copy();

    Collection<Reservation> userSavedReservations = copied.getReservations();
    assertTrue(userSavedReservations.contains(ANY_RESERVATION_DUMMY));
  }

  @Test
  void givenRoleHasBeenAdded_whenCopyingUser_thenCopiedUserHasCopiedRoles() {
    RolesCollection copiedRoles = mock(RolesCollection.class);
    RolesCollection copiedTwiceRoles = mock(RolesCollection.class);
    when(this.rolesCollectionSpy.copy()).thenReturn(copiedRoles);
    when(copiedRoles.copy()).thenReturn(copiedTwiceRoles);
    User copied = this.anyUser.copy();

    assertSame(copiedTwiceRoles, copied.getRoles());
  }

  @Test
  void givenSameObject_whenCallingEquals_thenObjectIsEqual() {
    assertEquals(this.anyUser, this.anyUser);
  }

  @Test
  void givenAnotherUserWithSameId_whenCallingEquals_thenObjectIsEqual() {
    User userWithSameProperties = new User(this.anyUserProfile, this.rolesCollectionSpy);
    assertEquals(userWithSameProperties, this.anyUser);
  }

  @Test
  void givenUserHasReservation_whenCallingHasReservation_thenReturnTrue() {
    TripID anyUserTripId = TripID.generate();
    when(ANY_RESERVATION_DUMMY.getTripID()).thenReturn(anyUserTripId);
    this.anyUser.addReservation(ANY_RESERVATION_DUMMY);

    boolean hasReservation = this.anyUser.hasReservation(anyUserTripId);

    assertTrue(hasReservation);
  }

  @Test
  void givenUserDoesNotHaveTheReservation_whenCallingHasReservation_thenReturnFalse() {
    TripID anyReservationNotInUser = TripID.generate();

    boolean hasReservation = this.anyUser.hasReservation(anyReservationNotInUser);

    assertFalse(hasReservation);
  }

  @Test
  void givenAnotherUserWithDifferentId_whenCallingEquals_thenObjectIsNotEqual() {
    Profile differentProfile =
        new Profile(
            ANY_USER_FULLNAME,
            ANY_BIRTH_DATE,
            ANY_GENDER,
            ANY_OTHER_USER_ID,
            this.anyUserCredential);
    User userWithDifferentId = new User(differentProfile, this.rolesCollectionSpy);

    assertNotEquals(userWithDifferentId, this.anyUser);
  }

  @Test
  void givenReservationExists_whenRetrievingConfirmationCode_thenReturnConfirmationCode() {
    Reservation reservation = mock(Reservation.class);
    TripInvoice tripInvoice = mock(TripInvoice.class);
    when(tripInvoice.getInvoiceID()).thenReturn(VALID_INVOICE_ID);
    when(tripInvoice.getTripID()).thenReturn(VALID_TRIP_ID);
    when(reservation.getInvoice()).thenReturn(tripInvoice);
    when(reservation.getInvoiceID()).thenReturn(VALID_INVOICE_ID);
    when(reservation.getTripID()).thenReturn(VALID_TRIP_ID);
    when(reservation.getConfirmationCode()).thenReturn(EXPECTED_CONFIRMATION_CODE);

    this.anyUser.addReservation(reservation);

    ConfirmationCode result = this.anyUser.retrieveConfirmationCodeForReservation(VALID_INVOICE_ID);

    assertSame(EXPECTED_CONFIRMATION_CODE, result);
  }

  @Test
  void
      givenTripInvoiceDoesNotExist_whenRetrievingConfirmationCode_thenThrowInvoiceNotFoundException() {
    assertThrows(
        InvoiceNotFoundException.class,
        () -> this.anyUser.retrieveConfirmationCodeForReservation(INVALID_INVOICE_ID));
  }

  @Test
  void givenUserWithReservations_whenGettingInvoices_thenReturnAllInvoices() {
    ReservationID anyReservationID = ReservationID.generate();
    ReservationID anotherReservationID = ReservationID.generate();
    TripInvoice anyTripInvoice = mock(TripInvoice.class);
    TripInvoice anotherTripInvoice = mock(TripInvoice.class);
    Reservation anyReservation = mock(Reservation.class);
    Reservation anotherReservation = mock(Reservation.class);

    when(anyReservation.getReservationID()).thenReturn(anyReservationID);
    when(anotherReservation.getReservationID()).thenReturn(anotherReservationID);
    when(anyReservation.getInvoice()).thenReturn(anyTripInvoice);
    when(anotherReservation.getInvoice()).thenReturn(anotherTripInvoice);

    this.anyUser.addReservation(anyReservation);
    this.anyUser.addReservation(anotherReservation);

    Collection<TripInvoice> invoices = this.anyUser.getInvoices();

    assertEquals(2, invoices.size());
    assertTrue(invoices.contains(anyTripInvoice));
    assertTrue(invoices.contains(anotherTripInvoice));
  }

  @Test
  void givenUserWithReservationsHavingNoInvoices_whenGettingInvoices_thenReturnEmptyCollection() {
    Reservation reservationWithoutInvoice = mock(Reservation.class);
    when(reservationWithoutInvoice.getInvoice()).thenReturn(null);

    this.anyUser.addReservation(reservationWithoutInvoice);

    Collection<TripInvoice> invoices = this.anyUser.getInvoices();

    assertTrue(invoices.isEmpty());
  }

  @Test
  void givenAnExistingReservation_whenValidateTripInvoiceForTrip_thenReturnValidationResult() {
    ValidationResult expectedResult = mock(ValidationResult.class);
    Reservation anyReservation = mock(Reservation.class);
    when(anyReservation.createValidationResult()).thenReturn(expectedResult);
    when(anyReservation.getTripID()).thenReturn(VALID_TRIP_ID);
    this.anyUser.addReservation(anyReservation);

    ValidationResult result = this.anyUser.validateTripInvoiceForTrip(VALID_TRIP_ID);

    assertSame(expectedResult, result);
  }

  @Test
  void
      givenUserWithExpiredAndActiveReservations_whenCancelExpiredReservations_thenReturnExpiredReservations() {
    this.userWithExpiredAndActiveReservations();
    Collection<Reservation> result = this.anyUser.cancelExpiredReservations();

    assertEquals(1, result.size());
    assertTrue(result.contains(this.expiredReservationMock));
  }

  @Test
  void
      givenUserWithExpiredReservation_whenCancelExpiredReservations_thenRemoveExpiredReservationsFromMap() {
    this.userWithExpiredAndActiveReservations();
    this.anyUser.cancelExpiredReservations();

    assertFalse(this.anyUser.getReservations().contains(this.expiredReservationMock));
  }

  @Test
  void
      givenUserWithActiveReservation_whenCancelExpiredReservations_thenActiveReservationsRemainInMap() {
    this.userWithExpiredAndActiveReservations();
    this.anyUser.cancelExpiredReservations();
    assertTrue(this.anyUser.getReservations().contains(this.activeReservationMock));
  }

  @Test
  void givenExistingReservation_whenPayReservation_thenReservationIsPaid() {
    when(this.payedReservationMock.getInvoiceID()).thenReturn(VALID_INVOICE_ID);
    this.anyUser.addReservation(this.payedReservationMock);

    this.anyUser.payReservation(
        this.paymentProcessorMock,
        this.paymentInformationMock,
        VALID_INVOICE_ID,
        ANY_OTHER_USER_ID);

    verify(this.payedReservationMock)
        .payReservation(this.paymentProcessorMock, this.paymentInformationMock, ANY_OTHER_USER_ID);
  }

  @Test
  void givenExistingReservation_whenPayReservation_thenReservationIsReAddedToUser() {
    when(this.payedReservationMock.getInvoiceID()).thenReturn(VALID_INVOICE_ID);
    this.anyUser.addReservation(this.payedReservationMock);

    Reservation returnedReservation =
        this.anyUser.payReservation(
            this.paymentProcessorMock,
            this.paymentInformationMock,
            VALID_INVOICE_ID,
            ANY_OTHER_USER_ID);

    assertSame(this.payedReservationMock, returnedReservation);
    assertTrue(this.anyUser.getReservations().contains(this.payedReservationMock));
  }

  @Test
  void givenNoReservationForInvoiceID_whenPayReservation_thenThrowReservationNotFoundException() {
    assertThrows(
        ReservationNotFoundException.class,
        () ->
            this.anyUser.payReservation(
                this.paymentProcessorMock,
                this.paymentInformationMock,
                VALID_INVOICE_ID,
                ANY_OTHER_USER_ID));
  }

  @Test
  void givenUserWithoutReservation_whenCancelsReservation_thenThrowsReservationNotFoundException() {
    assertThrows(
        ReservationNotFoundException.class,
        () -> this.anyUser.cancelReservation(ANY_RESERVATION_ID, ANY_TRIP_DATE));
  }

  @Test
  void givenUserWithReservation_whenUserCancelsReservation_thenReservationCancelsTrip() {
    when(this.activeReservationMock.getReservationID()).thenReturn(ANY_RESERVATION_ID);
    this.anyUser.addReservation(this.activeReservationMock);

    this.anyUser.cancelReservation(ANY_RESERVATION_ID, ANY_TRIP_DATE);

    verify(this.activeReservationMock).cancel(ANY_TRIP_DATE);
  }

  @Test
  void givenUserWithReservation_whenUserCancelsReservation_thenCreditsAreGivenToUser() {
    when(this.activeReservationMock.getReservationID()).thenReturn(ANY_RESERVATION_ID);
    when(this.activeReservationMock.calculateUserCredits()).thenReturn(ANY_CREDITS_AMOUNT);
    this.anyUser.addReservation(this.activeReservationMock);

    this.anyUser.cancelReservation(ANY_RESERVATION_ID, ANY_TRIP_DATE);

    assertEquals(ANY_CREDITS_AMOUNT, this.anyUser.getCredits());
  }

  private void userWithExpiredAndActiveReservations() {
    when(this.expiredReservationMock.getInvoiceID()).thenReturn(VALID_INVOICE_ID);
    when(this.activeReservationMock.getInvoiceID()).thenReturn(INVALID_INVOICE_ID);
    when(this.expiredReservationMock.getReservationID()).thenReturn(ANY_RESERVATION_ID);
    when(this.activeReservationMock.getReservationID()).thenReturn(ANOTHER_RESERVATION_ID);
    when(this.expiredReservationMock.isExpired()).thenReturn(true);
    when(this.activeReservationMock.isExpired()).thenReturn(false);

    this.anyUser.addReservation(this.expiredReservationMock);
    this.anyUser.addReservation(this.activeReservationMock);
  }
}
