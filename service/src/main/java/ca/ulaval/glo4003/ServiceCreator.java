package ca.ulaval.glo4003;

import ca.ulaval.glo4003.clock.Clock;
import ca.ulaval.glo4003.confirmationcode.ConfirmationCodeVerificationService;
import ca.ulaval.glo4003.group.GroupConfig;
import ca.ulaval.glo4003.guest.GuestTripService;
import ca.ulaval.glo4003.hosting.HostingSearchFilterer;
import ca.ulaval.glo4003.hosting.HostingService;
import ca.ulaval.glo4003.hosting.filter.SearchHostingAvailableCapacityFilter;
import ca.ulaval.glo4003.hosting.filter.SearchHostingAvailablePeriodFilter;
import ca.ulaval.glo4003.hosting.filter.SearchHostingFilter;
import ca.ulaval.glo4003.hosting.filter.SearchHostingRegionFilter;
import ca.ulaval.glo4003.hosting.reservation.HostingNotificationSender;
import ca.ulaval.glo4003.hosting.reservation.HostingReservationService;
import ca.ulaval.glo4003.invoice.TripInvoiceService;
import ca.ulaval.glo4003.login.LoginService;
import ca.ulaval.glo4003.notification.*;
import ca.ulaval.glo4003.notification.filters.*;
import ca.ulaval.glo4003.notification.types.Code2FANotificationFactory;
import ca.ulaval.glo4003.notification.types.HostingConfirmedNotificationFactory;
import ca.ulaval.glo4003.notification.types.ReservationConfirmationNotificationFactory;
import ca.ulaval.glo4003.notification.types.trip.upcoming.UpcomingTripNotificationBuilder;
import ca.ulaval.glo4003.notification.types.trip.upcoming.UpcomingTripNotificationDirector;
import ca.ulaval.glo4003.notification.types.trip.upcoming.UpcomingTripNotificationProcessor;
import ca.ulaval.glo4003.payment.*;
import ca.ulaval.glo4003.profile.ProfileService;
import ca.ulaval.glo4003.profile.ProfileTripsService;
import ca.ulaval.glo4003.profile.assembler.ProfileTripDtoAssembler;
import ca.ulaval.glo4003.register.RegisterService;
import ca.ulaval.glo4003.reservation.*;
import ca.ulaval.glo4003.tax.TaxStrategy;
import ca.ulaval.glo4003.token.*;
import ca.ulaval.glo4003.transport.ReservableTransportService;
import ca.ulaval.glo4003.traveler.user.UserRepository;
import ca.ulaval.glo4003.traveler.user.credential.EmailValidator;
import ca.ulaval.glo4003.traveler.user.credential.PasswordValidator;
import ca.ulaval.glo4003.traveler.user.credential.UserCredentialValidator;
import ca.ulaval.glo4003.traveler.user.role.UserAccessValidator;
import ca.ulaval.glo4003.trip.*;
import ca.ulaval.glo4003.user.UserPermissionService;
import java.time.Duration;
import java.util.List;

public class ServiceCreator {
  private static final UserAccessValidator USER_ACCESS_VALIDATOR = new UserAccessValidator();
  private static final int MINIMUM_CHARACTER_REQUIRED_FOR_PASSWORD = 10;
  private static final UserCredentialValidator USER_CREDENTIAL_VALIDATOR =
      new UserCredentialValidator(
          new EmailValidator(), new PasswordValidator(MINIMUM_CHARACTER_REQUIRED_FOR_PASSWORD));
  private final RepositoryCreator repositoryCreator;
  private final FactoryCreator factoryCreator;
  private final AssemblerCreator assemblerCreator;

  public ServiceCreator(
      RepositoryCreator repositoryCreator,
      FactoryCreator factoryCreator,
      AssemblerCreator assemblerCreator) {
    this.repositoryCreator = repositoryCreator;
    this.factoryCreator = factoryCreator;
    this.assemblerCreator = assemblerCreator;
  }

  public RegisterService createRegisterService() {
    return new RegisterService(
        this.factoryCreator.createUserFactory(),
        this.repositoryCreator.createUserRepository(),
        USER_CREDENTIAL_VALIDATOR);
  }

  public LoginService createLoginService(AuthTokenFactory authTokenFactory) {
    return new LoginService(
        this.repositoryCreator.createUserRepository(),
        this.repositoryCreator.createAuthTokenRepository(),
        authTokenFactory);
  }

  public ReservationService createReservationService(
      GroupConfig groupConfig, ReservationConfig reservationConfig, TaxStrategy taxStrategy) {
    return new ReservationService(
        this.repositoryCreator.createUserRepository(),
        this.repositoryCreator.createTripRepository(),
        this.factoryCreator.createReservationFactory(reservationConfig, taxStrategy),
        this.factoryCreator.createGroupFactory(groupConfig),
        this.assemblerCreator.createCreatedReservationDtoAssembler());
  }

  public PaymentService createPaymentService(
      EmailNotificationSender emailNotificationSender, PaymentApi paymentApi) {
    return new PaymentService(
        this.assemblerCreator.createPaymentConfirmationDtoAssembler(),
        this.repositoryCreator.createUserRepository(),
        this.createPaymentProcessor(paymentApi),
        this.createNotificationProcessorForReservation(
            this.factoryCreator.createReservationConfirmationNotificationFactory(),
            emailNotificationSender));
  }

  private ReservationConfirmationNotificationProcessor createNotificationProcessorForReservation(
      ReservationConfirmationNotificationFactory notificationFactory,
      EmailNotificationSender emailNotificationSender) {
    return new ReservationConfirmationNotificationProcessor(
        notificationFactory, emailNotificationSender);
  }

  private PaymentProcessor createPaymentProcessor(PaymentApi paymentApi) {
    return new PaymentProcessor(new PaymentQuantityValidator(), paymentApi);
  }

  public ReservationExpirationService createReservationExpirationService() {
    return new ReservationExpirationService(
        this.repositoryCreator.createUserRepository(),
        this.repositoryCreator.createTripRepository());
  }

  public ProfileService createProfileService() {
    UserRepository userRepository = this.repositoryCreator.createUserRepository();

    return new ProfileService(userRepository);
  }

  public TripService createTripService() {
    return new TripService(
        this.repositoryCreator.createTripRepository(),
        new TripSearchFilterer(),
        this.assemblerCreator.createTripDtoAssembler());
  }

  public ConfirmationCodeVerificationService createConfirmationCodeVerificationService() {
    return new ConfirmationCodeVerificationService(
        this.repositoryCreator.createTripRepository(),
        this.repositoryCreator.createUserRepository());
  }

  public ProfileTripsService createProfileTripsService() {
    return new ProfileTripsService(
        this.repositoryCreator.createTripRepository(),
        this.repositoryCreator.createUserRepository(),
        this.assemblerCreator.createProfileTripDtoAssembler());
  }

  public TripBlockingService createTripBlockingService(Duration durationBeforeBlockingTrip) {
    return new TripBlockingService(
        this.repositoryCreator.createTripRepository(), durationBeforeBlockingTrip);
  }

  public TripInvoiceService createTripInvoiceService() {
    return new TripInvoiceService(
        this.repositoryCreator.createUserRepository(),
        this.assemblerCreator.createInvoiceDtoAssembler());
  }

  public HostingService createHostingService() {
    SearchHostingRegionFilter searchHostingRegionFilter = new SearchHostingRegionFilter();
    SearchHostingAvailableCapacityFilter searchHostingAvailableCapacityFilter =
        new SearchHostingAvailableCapacityFilter();
    SearchHostingAvailablePeriodFilter searchHostingAvailablePeriodFilter =
        new SearchHostingAvailablePeriodFilter();
    List<SearchHostingFilter> hostingFilterList =
        List.of(
            searchHostingRegionFilter,
            searchHostingAvailableCapacityFilter,
            searchHostingAvailablePeriodFilter);
    return new HostingService(
        this.repositoryCreator.createHostingRepository(),
        this.repositoryCreator.createTripRepository(),
        new HostingSearchFilterer(hostingFilterList),
        this.assemblerCreator.createHostingDtoAssembler());
  }

  public HostingReservationService createHostingReservationService(
      EmailNotificationSender emailNotificationSender) {
    UserRepository userRepository = this.repositoryCreator.createUserRepository();
    HostingConfirmedNotificationFactory hostingConfirmedNotificationFactory =
        this.factoryCreator.createHostingConfirmedNotificationFactory();
    return new HostingReservationService(
        this.repositoryCreator.createUserRepository(),
        this.repositoryCreator.createHostingRepository(),
        this.repositoryCreator.createTripRepository(),
        this.assemblerCreator.createCreatedHostingReservationDtoAssembler(),
        new HostingNotificationSender(
            userRepository, hostingConfirmedNotificationFactory, emailNotificationSender),
        this.factoryCreator.createTripTravelerFetcherFactory());
  }

  public ReservableTransportService createReservableTransportService() {
    return new ReservableTransportService(
        this.repositoryCreator.createTripRepository(),
        this.assemblerCreator.createOrganizedBusDtoAssembler());
  }

  public TripNotificationService createTripNotificationService(
      UpcomingTripNotificationBuilder builder,
      EmailNotificationSender emailNotificationSender,
      Clock clock,
      Duration notificationThresholdInSeconds) {

    UpcomingTripNotificationDirector director = new UpcomingTripNotificationDirector(builder);
    UpcomingTripNotificationProcessor processor =
        this.createNotificationProcessor(emailNotificationSender, director);
    TripThresholdFinder tripThresholdFinder =
        new TripThresholdFinder(
            clock, this.repositoryCreator.createTripRepository(), notificationThresholdInSeconds);
    return new TripNotificationService(
        this.repositoryCreator.createUserRepository(),
        new TripNotificationFilters(this.createUserNotificationFilters()),
        tripThresholdFinder,
        processor);
  }

  private List<TripUserNotificationFilter> createUserNotificationFilters() {
    return List.of(
        new TripNotificationFilterUnpaidReservation(), new TripNotificationFilterAlreadyNotified());
  }

  private UpcomingTripNotificationProcessor createNotificationProcessor(
      EmailNotificationSender emailNotificationSender,
      UpcomingTripNotificationDirector upcomingTripNotificationDirector) {
    return new UpcomingTripNotificationProcessor(
        emailNotificationSender, upcomingTripNotificationDirector);
  }

  public UserPermissionService createUserPermissionService() {
    return new UserPermissionService(
        this.repositoryCreator.createUserRepository(), USER_ACCESS_VALIDATOR);
  }

  public GuestTripService createGuestTripService() {
    UserRepository userRepository = this.repositoryCreator.createUserRepository();
    TripRepository tripRepository = this.repositoryCreator.createTripRepository();
    ProfileTripDtoAssembler profileTripDtoAssembler =
        this.assemblerCreator.createProfileTripDtoAssembler();

    return new GuestTripService(tripRepository, userRepository, profileTripDtoAssembler);
  }

  public AuthTokenService createAuthTokenService() {
    AuthTokenRepository authTokenRepository = this.repositoryCreator.createAuthTokenRepository();
    AuthTokenValidator authTokenValidator = new AuthTokenValidator();

    return new AuthTokenService(authTokenRepository, authTokenValidator);
  }

  public Guest2FAService createGuest2FAService(
      AuthTokenFactory authTokenFactory, EmailNotificationSender emailNotificationSender) {
    TripRepository tripRepository = this.repositoryCreator.createTripRepository();
    AuthTokenRepository authTokenRepository = this.repositoryCreator.createAuthTokenRepository();
    Code2FANotificationFactory code2FANotificationFactory =
        this.factoryCreator.createCode2FANotificationFactory();

    return new Guest2FAService(
        tripRepository,
        authTokenRepository,
        authTokenFactory,
        emailNotificationSender,
        code2FANotificationFactory);
  }

  public CancelReservationService createCancelReservationService() {
    return new CancelReservationService(
        this.repositoryCreator.createUserRepository(),
        this.repositoryCreator.createTripRepository(),
        this.assemblerCreator.createInvoiceDtoAssembler());
  }
}
