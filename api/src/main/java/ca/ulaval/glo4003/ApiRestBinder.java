package ca.ulaval.glo4003;

import ca.ulaval.glo4003.group.assembler.GroupResponseAssembler;
import ca.ulaval.glo4003.guest.assembler.GuestRequestAssembler;
import ca.ulaval.glo4003.guest.assembler.GuestResponseAssembler;
import ca.ulaval.glo4003.hiking.assembler.HikingResponseAssembler;
import ca.ulaval.glo4003.hosting.assembler.HostingResponseAssembler;
import ca.ulaval.glo4003.hosting.assembler.SearchHostingResponseAssembler;
import ca.ulaval.glo4003.hosting.reservation.assembler.CreateHostingReservationResponseAssembler;
import ca.ulaval.glo4003.invoice.assembler.InvoiceResponseAssembler;
import ca.ulaval.glo4003.invoice.assembler.PaymentTraceResponseAssembler;
import ca.ulaval.glo4003.login.assembler.LoginResponseAssembler;
import ca.ulaval.glo4003.payment.assembler.PaymentRequestAssembler;
import ca.ulaval.glo4003.payment.assembler.PaymentResponseAssembler;
import ca.ulaval.glo4003.payment.usecase.PaymentUseCase;
import ca.ulaval.glo4003.profile.assembler.ProfileTripResponseAssembler;
import ca.ulaval.glo4003.reservation.assembler.ReservationRequestAssembler;
import ca.ulaval.glo4003.reservation.assembler.ReservationResponseAssembler;
import ca.ulaval.glo4003.reservation.request.ReservationRequestValidator;
import ca.ulaval.glo4003.reservation.usecase.ReservationCreationUseCase;
import ca.ulaval.glo4003.transport.assembler.ReservableTransportResponseAssembler;
import ca.ulaval.glo4003.transport.assembler.ReservableTransportStopResponseAssembler;
import ca.ulaval.glo4003.transport.assembler.TransportOptionResponseAssembler;
import ca.ulaval.glo4003.trip.assembler.SearchTripRequestAssembler;
import ca.ulaval.glo4003.trip.assembler.SearchTripResponseAssembler;
import ca.ulaval.glo4003.trip.usecase.BlockTripUseCase;
import ca.ulaval.glo4003.trip.usecase.SearchTripUnauthenticatedUseCase;
import ca.ulaval.glo4003.trip.usecase.SearchTripUseCase;
import ca.ulaval.glo4003.trip.usecase.VerifyTripUseCase;
import ca.ulaval.glo4003.user.assembler.CreateUserRequestAssembler;
import ca.ulaval.glo4003.user.assembler.ProfileResponseAssembler;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class ApiRestBinder extends AbstractBinder {

  @Override
  protected void configure() {
    this.bindApiAuthentication();
    this.bindApiReservation();
    this.bindApiProfile();
    this.bindApiTrip();
    this.bindApiHiking();
    this.bindApiHosting();
    this.bindApiPayment();
    this.bindApiTransport();
  }

  private void bindApiAuthentication() {
    bind(LoginResponseAssembler.class).to(LoginResponseAssembler.class);
    bind(CreateUserRequestAssembler.class).to(CreateUserRequestAssembler.class);
  }

  private void bindApiReservation() {
    bind(ReservationRequestAssembler.class).to(ReservationRequestAssembler.class);
    bind(ReservationResponseAssembler.class).to(ReservationResponseAssembler.class);
    bind(ReservationRequestValidator.class).to(ReservationRequestValidator.class);
    bind(InvoiceResponseAssembler.class).to(InvoiceResponseAssembler.class);
    bind(ReservationCreationUseCase.class).to(ReservationCreationUseCase.class);
    bind(GroupResponseAssembler.class).to(GroupResponseAssembler.class);
    bind(GuestRequestAssembler.class).to(GuestRequestAssembler.class);
    bind(GuestResponseAssembler.class).to(GuestResponseAssembler.class);
  }

  private void bindApiProfile() {
    bind(ProfileResponseAssembler.class).to(ProfileResponseAssembler.class);
    bind(ProfileTripResponseAssembler.class).to(ProfileTripResponseAssembler.class);
  }

  private void bindApiTrip() {
    bind(SearchTripRequestAssembler.class).to(SearchTripRequestAssembler.class);
    bind(SearchTripResponseAssembler.class).to(SearchTripResponseAssembler.class);
    bind(BlockTripUseCase.class).to(BlockTripUseCase.class);
    bind(SearchTripUseCase.class).to(SearchTripUseCase.class);
    bind(SearchTripUnauthenticatedUseCase.class).to(SearchTripUnauthenticatedUseCase.class);
    bind(VerifyTripUseCase.class).to(VerifyTripUseCase.class);
  }

  private void bindApiHiking() {
    bind(HikingResponseAssembler.class).to(HikingResponseAssembler.class);
  }

  private void bindApiHosting() {
    bind(SearchHostingResponseAssembler.class).to(SearchHostingResponseAssembler.class);
    bind(CreateHostingReservationResponseAssembler.class)
        .to(CreateHostingReservationResponseAssembler.class);
    bind(HostingResponseAssembler.class).to(HostingResponseAssembler.class);
  }

  private void bindApiPayment() {
    bind(PaymentRequestAssembler.class).to(PaymentRequestAssembler.class);
    bind(PaymentResponseAssembler.class).to(PaymentResponseAssembler.class);
    bind(PaymentTraceResponseAssembler.class).to(PaymentTraceResponseAssembler.class);
    bind(PaymentUseCase.class).to(PaymentUseCase.class);
  }

  private void bindApiTransport() {
    bind(TransportOptionResponseAssembler.class).to(TransportOptionResponseAssembler.class);
    bind(ReservableTransportResponseAssembler.class).to(ReservableTransportResponseAssembler.class);
    bind(ReservableTransportStopResponseAssembler.class)
        .to(ReservableTransportStopResponseAssembler.class);
  }
}
