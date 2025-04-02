package ca.ulaval.glo4003;

import ca.ulaval.glo4003.confirmationcode.ConfirmationCodeFactory;
import ca.ulaval.glo4003.group.GroupConfig;
import ca.ulaval.glo4003.group.GroupValidator;
import ca.ulaval.glo4003.guest.GuestFactory;
import ca.ulaval.glo4003.hosting.reservation.HostingReservationFactory;
import ca.ulaval.glo4003.hosting.reservation.TripTravelerFetcherFactory;
import ca.ulaval.glo4003.invoice.TripInvoiceFactory;
import ca.ulaval.glo4003.notification.types.Code2FANotificationFactory;
import ca.ulaval.glo4003.notification.types.HostingConfirmedNotificationFactory;
import ca.ulaval.glo4003.notification.types.ReservationConfirmationNotificationFactory;
import ca.ulaval.glo4003.register.UserCredentialFactory;
import ca.ulaval.glo4003.register.UserFactory;
import ca.ulaval.glo4003.reservation.*;
import ca.ulaval.glo4003.tax.TaxStrategy;
import ca.ulaval.glo4003.transport.TransportCollectionFactory;
import ca.ulaval.glo4003.transport.TransportConfig;
import ca.ulaval.glo4003.transport.organizedbus.OrganizedBusFactory;
import ca.ulaval.glo4003.traveler.user.ProfileValidator;
import ca.ulaval.glo4003.traveler.user.credential.EmailValidator;
import ca.ulaval.glo4003.trip.TripConfig;
import ca.ulaval.glo4003.trip.TripFactory;

public class FactoryCreator {
  private static final int MINIMUM_AGE_REQUIRED_FOR_USER = 18;
  private static final int MAXIMUM_AGE_REQUIRED_FOR_USER = 80;
  private static final ProfileValidator USER_VALIDATOR =
      new ProfileValidator(MINIMUM_AGE_REQUIRED_FOR_USER, MAXIMUM_AGE_REQUIRED_FOR_USER);
  private static final UserCredentialFactory USER_CREDENTIAL_FACTORY = new UserCredentialFactory();
  private final RepositoryCreator repositoryCreator;

  public FactoryCreator(RepositoryCreator repositoryCreator) {
    this.repositoryCreator = repositoryCreator;
  }

  public UserFactory createUserFactory() {
    return new UserFactory(USER_VALIDATOR, USER_CREDENTIAL_FACTORY);
  }

  public ReservationFactory createReservationFactory(
      ReservationConfig reservationConfig, TaxStrategy taxStrategy) {
    return new ReservationFactory(
        this.createTripInvoiceFactory(taxStrategy),
        this.createConfirmationCodeFactory(),
        reservationConfig);
  }

  public ConfirmationCodeFactory createConfirmationCodeFactory() {
    return new ConfirmationCodeFactory();
  }

  public TripFactory createTripFactory(TripConfig tripConfig) {
    return new TripFactory(tripConfig);
  }

  public TripInvoiceFactory createTripInvoiceFactory(TaxStrategy taxStrategy) {
    return new TripInvoiceFactory(taxStrategy);
  }

  public OrganizedBusFactory createOrganizedBusFactory(TransportConfig config) {
    return new OrganizedBusFactory(config);
  }

  public HostingReservationFactory createHostingReservationFactory() {
    return new HostingReservationFactory();
  }

  public HostingConfirmedNotificationFactory createHostingConfirmedNotificationFactory() {
    return new HostingConfirmedNotificationFactory();
  }

  public GroupFactory createGroupFactory(GroupConfig groupConfig) {
    GroupValidator groupValidator = new GroupValidator(groupConfig);
    return new GroupFactory(groupValidator, this.createGuestFactory());
  }

  public Code2FANotificationFactory createCode2FANotificationFactory() {
    return new Code2FANotificationFactory();
  }

  public ReservationConfirmationNotificationFactory
      createReservationConfirmationNotificationFactory() {
    return new ReservationConfirmationNotificationFactory();
  }

  public TripTravelerFetcherFactory createTripTravelerFetcherFactory() {
    return new TripTravelerFetcherFactory();
  }

  private GuestFactory createGuestFactory() {
    return new GuestFactory(new EmailValidator());
  }

  public TransportCollectionFactory createTransportCollectionFactory() {
    return new TransportCollectionFactory();
  }
}
