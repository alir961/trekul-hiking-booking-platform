package ca.ulaval.glo4003.payment;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.invoice.InvoiceID;
import ca.ulaval.glo4003.invoice.TripInvoice;
import ca.ulaval.glo4003.notification.ReservationConfirmationNotificationProcessor;
import ca.ulaval.glo4003.payment.assembler.PaymentConfirmationDtoAssembler;
import ca.ulaval.glo4003.payment.dto.PaymentConfirmationDto;
import ca.ulaval.glo4003.reservation.Reservation;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.user.User;
import ca.ulaval.glo4003.traveler.user.UserRepository;
import ca.ulaval.glo4003.traveler.user.exception.UserNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PaymentServiceTest {
  private static final String ANY_USER_ID_AS_STR = "user01";
  private static final String ANY_PAYER_USER_ID_AS_STR = "payer01";
  private static final TravelerID ANY_PAYER_USER_ID = new TravelerID(ANY_PAYER_USER_ID_AS_STR);
  private static final TravelerID ANY_USER_ID = new TravelerID(ANY_USER_ID_AS_STR);
  private static final InvoiceID ANY_INVOICE_ID = InvoiceID.generate();

  private PaymentConfirmationDtoAssembler paymentConfirmationDtoAssemblerMock;
  private PaymentInformation paymentInformationMock;
  private UserRepository userRepositoryMock;
  private User anyOwnerUserMock;
  private Reservation anyReservationMock;
  private TripInvoice anyTripInvoiceMock;
  private PaymentProcessor paymentProcessorMock;
  private ReservationConfirmationNotificationProcessor
      reservationConfirmationNotificationProcessorMock;
  private PaymentService paymentService;
  private PaymentConfirmationDto paymentConfirmationDtoMock;

  @BeforeEach
  void setUp() {
    this.paymentConfirmationDtoAssemblerMock = mock(PaymentConfirmationDtoAssembler.class);
    this.userRepositoryMock = mock(UserRepository.class);
    this.paymentProcessorMock = mock(PaymentProcessor.class);
    this.reservationConfirmationNotificationProcessorMock =
        mock(ReservationConfirmationNotificationProcessor.class);
    this.paymentInformationMock = mock(PaymentInformation.class);
    this.anyOwnerUserMock = mock(User.class);
    this.anyReservationMock = mock(Reservation.class);
    this.anyTripInvoiceMock = mock(TripInvoice.class);
    this.paymentConfirmationDtoMock = mock(PaymentConfirmationDto.class);

    this.paymentService =
        new PaymentService(
            this.paymentConfirmationDtoAssemblerMock,
            this.userRepositoryMock,
            this.paymentProcessorMock,
            this.reservationConfirmationNotificationProcessorMock);

    when(this.userRepositoryMock.getById(ANY_USER_ID))
        .thenReturn(Optional.of(this.anyOwnerUserMock));
    when(this.anyOwnerUserMock.payReservation(
            this.paymentProcessorMock,
            this.paymentInformationMock,
            ANY_INVOICE_ID,
            ANY_PAYER_USER_ID))
        .thenReturn(this.anyReservationMock);
    when(this.anyReservationMock.getInvoice()).thenReturn(this.anyTripInvoiceMock);
    when(this.paymentConfirmationDtoAssemblerMock.assemble(
            this.paymentInformationMock, this.anyTripInvoiceMock, ANY_PAYER_USER_ID))
        .thenReturn(this.paymentConfirmationDtoMock);
  }

  @Test
  void givenNonExistentUser_whenPayInvoice_thenThrowUserNotFoundException() {
    when(this.userRepositoryMock.getById(ANY_USER_ID)).thenReturn(Optional.empty());

    assertThrows(
        UserNotFoundException.class,
        () ->
            this.paymentService.payInvoice(
                this.paymentInformationMock, ANY_INVOICE_ID, ANY_USER_ID, ANY_PAYER_USER_ID));

    verify(this.userRepositoryMock).getById(ANY_USER_ID);
    verifyNoMoreInteractions(
        this.userRepositoryMock,
        this.paymentProcessorMock,
        this.reservationConfirmationNotificationProcessorMock,
        this.paymentConfirmationDtoAssemblerMock);
  }

  @Test
  void givenExistingUser_whenPayInvoice_thenUserIsRetrieved() {
    this.paymentService.payInvoice(
        this.paymentInformationMock, ANY_INVOICE_ID, ANY_USER_ID, ANY_PAYER_USER_ID);

    verify(this.userRepositoryMock).getById(ANY_USER_ID);
  }

  @Test
  void givenExistingUser_whenPayInvoice_thenPayReservationIsCalledOnUser() {

    this.paymentService.payInvoice(
        this.paymentInformationMock, ANY_INVOICE_ID, ANY_USER_ID, ANY_PAYER_USER_ID);

    verify(this.anyOwnerUserMock)
        .payReservation(
            this.paymentProcessorMock,
            this.paymentInformationMock,
            ANY_INVOICE_ID,
            ANY_PAYER_USER_ID);
  }

  @Test
  void givenExistingUser_whenPayInvoice_thenNotificationIsSent() {
    this.paymentService.payInvoice(
        this.paymentInformationMock, ANY_INVOICE_ID, ANY_USER_ID, ANY_PAYER_USER_ID);

    verify(this.reservationConfirmationNotificationProcessorMock)
        .notify(this.anyReservationMock, this.anyOwnerUserMock);
  }

  @Test
  void givenExistingUser_whenPayInvoice_thenUserIsSaved() {
    this.paymentService.payInvoice(
        this.paymentInformationMock, ANY_INVOICE_ID, ANY_USER_ID, ANY_PAYER_USER_ID);

    verify(this.userRepositoryMock).save(this.anyOwnerUserMock);
  }

  @Test
  void givenExistingUser_whenPayInvoice_thenPaymentConfirmationDtoIsAssembled() {

    this.paymentService.payInvoice(
        this.paymentInformationMock, ANY_INVOICE_ID, ANY_USER_ID, ANY_PAYER_USER_ID);

    verify(this.paymentConfirmationDtoAssemblerMock)
        .assemble(this.paymentInformationMock, this.anyTripInvoiceMock, ANY_PAYER_USER_ID);
  }
}
