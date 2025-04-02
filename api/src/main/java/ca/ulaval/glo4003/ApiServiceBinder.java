package ca.ulaval.glo4003;

import ca.ulaval.glo4003.clock.Clock;
import ca.ulaval.glo4003.clock.SystemClock;
import ca.ulaval.glo4003.config.TrekULConfigYaml;
import ca.ulaval.glo4003.confirmationcode.ConfirmationCodeVerificationService;
import ca.ulaval.glo4003.guest.GuestTripService;
import ca.ulaval.glo4003.hosting.HostingService;
import ca.ulaval.glo4003.hosting.reservation.HostingReservationService;
import ca.ulaval.glo4003.invoice.TripInvoiceService;
import ca.ulaval.glo4003.login.LoginService;
import ca.ulaval.glo4003.payment.FakePaymentApi;
import ca.ulaval.glo4003.payment.PaymentApi;
import ca.ulaval.glo4003.payment.PaymentService;
import ca.ulaval.glo4003.profile.ProfileService;
import ca.ulaval.glo4003.profile.ProfileTripsService;
import ca.ulaval.glo4003.register.RegisterService;
import ca.ulaval.glo4003.reservation.CancelReservationService;
import ca.ulaval.glo4003.reservation.ReservationService;
import ca.ulaval.glo4003.tax.QuebecTaxStrategy;
import ca.ulaval.glo4003.tax.TaxStrategy;
import ca.ulaval.glo4003.token.AuthTokenFactory;
import ca.ulaval.glo4003.token.AuthTokenService;
import ca.ulaval.glo4003.token.UuidAuthTokenFactory;
import ca.ulaval.glo4003.transport.ReservableTransportService;
import ca.ulaval.glo4003.trip.TripBlockingService;
import ca.ulaval.glo4003.trip.TripService;
import ca.ulaval.glo4003.user.UserPermissionService;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class ApiServiceBinder extends AbstractBinder {
  private static final Clock SYSTEM_CLOCK = new SystemClock();
  private static final PaymentApi PAYMENT_API = new FakePaymentApi();
  private static final TaxStrategy TAX_STRATEGY = new QuebecTaxStrategy();
  private final ServiceCreator serviceCreator;
  private final InfraServiceCreator infraServiceCreator;
  private final TrekULConfigYaml trekULConfig;

  public ApiServiceBinder(
      ServiceCreator serviceCreator,
      InfraServiceCreator infraServiceCreator,
      TrekULConfigYaml trekULConfig) {
    this.serviceCreator = serviceCreator;
    this.infraServiceCreator = infraServiceCreator;
    this.trekULConfig = trekULConfig;
  }

  @Override
  protected void configure() {
    this.bindUserServices();
    this.bindTripServices();
    this.bindReservationServices();
    this.bindHostingServices();
  }

  private void bindUserServices() {
    bind(this.serviceCreator.createRegisterService()).to(RegisterService.class);
    bind(this.serviceCreator.createProfileService()).to(ProfileService.class);
    bind(this.serviceCreator.createUserPermissionService()).to(UserPermissionService.class);
    this.bindServiceWithTokens();
  }

  private void bindServiceWithTokens() {
    AuthTokenFactory userTokenFactory =
        new UuidAuthTokenFactory(
            SYSTEM_CLOCK, this.trekULConfig.getTokenConfig().loginTokenDuration());
    bind(this.serviceCreator.createAuthTokenService()).to(AuthTokenService.class);
    bind(this.serviceCreator.createLoginService(userTokenFactory)).to(LoginService.class);
    AuthTokenFactory guestTokenFactory =
        new UuidAuthTokenFactory(
            SYSTEM_CLOCK, this.trekULConfig.getTokenConfig().guest2FATokenDuration());
    bind(
        this.serviceCreator.createGuest2FAService(
            guestTokenFactory, this.infraServiceCreator.createEmailNotificationSender()));
  }

  private void bindTripServices() {
    bind(this.serviceCreator.createTripService()).to(TripService.class);
    bind(this.serviceCreator.createTripBlockingService(
            this.trekULConfig.getTripConfig().getDurationBeforeBlockingTrip()))
        .to(TripBlockingService.class);
    bind(this.serviceCreator.createReservableTransportService())
        .to(ReservableTransportService.class);
  }

  private void bindReservationServices() {
    bind(this.serviceCreator.createPaymentService(
            this.infraServiceCreator.createEmailNotificationSender(), PAYMENT_API))
        .to(PaymentService.class);
    bind(this.serviceCreator.createTripInvoiceService()).to(TripInvoiceService.class);

    bind(this.serviceCreator.createReservationService(
            this.trekULConfig.getGroupConfig(),
            this.trekULConfig.getReservationConfig(),
            TAX_STRATEGY))
        .to(ReservationService.class);
    bind(this.serviceCreator.createCancelReservationService()).to(CancelReservationService.class);

    bind(this.serviceCreator.createProfileTripsService()).to(ProfileTripsService.class);
    bind(this.serviceCreator.createGuestTripService()).to(GuestTripService.class);
    bind(this.serviceCreator.createConfirmationCodeVerificationService())
        .to(ConfirmationCodeVerificationService.class);
  }

  private void bindHostingServices() {
    bind(this.serviceCreator.createHostingService()).to(HostingService.class);
    bind(this.serviceCreator.createHostingReservationService(
            this.infraServiceCreator.createEmailNotificationSender()))
        .to(HostingReservationService.class);
  }
}
