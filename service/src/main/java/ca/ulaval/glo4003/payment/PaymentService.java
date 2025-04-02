package ca.ulaval.glo4003.payment;

import ca.ulaval.glo4003.invoice.InvoiceID;
import ca.ulaval.glo4003.notification.ReservationConfirmationNotificationProcessor;
import ca.ulaval.glo4003.payment.assembler.PaymentConfirmationDtoAssembler;
import ca.ulaval.glo4003.payment.dto.PaymentConfirmationDto;
import ca.ulaval.glo4003.reservation.Reservation;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.user.User;
import ca.ulaval.glo4003.traveler.user.UserRepository;
import ca.ulaval.glo4003.traveler.user.exception.UserNotFoundException;

public class PaymentService {
  private final PaymentConfirmationDtoAssembler paymentConfirmationDtoAssembler;
  private final UserRepository userRepository;
  private final PaymentProcessor paymentProcessor;
  private final ReservationConfirmationNotificationProcessor
      reservationConfirmationNotificationProcessor;

  public PaymentService(
      PaymentConfirmationDtoAssembler paymentConfirmationDtoAssembler,
      UserRepository userRepository,
      PaymentProcessor paymentProcessor,
      ReservationConfirmationNotificationProcessor reservationConfirmationNotificationProcessor) {
    this.paymentConfirmationDtoAssembler = paymentConfirmationDtoAssembler;
    this.userRepository = userRepository;
    this.paymentProcessor = paymentProcessor;
    this.reservationConfirmationNotificationProcessor =
        reservationConfirmationNotificationProcessor;
  }

  public PaymentConfirmationDto payInvoice(
      PaymentInformation paymentInformation,
      InvoiceID invoiceID,
      TravelerID ownerID,
      TravelerID payerID) {
    User user = this.userRepository.getById(ownerID).orElseThrow(UserNotFoundException::new);
    Reservation reservation =
        user.payReservation(this.paymentProcessor, paymentInformation, invoiceID, payerID);
    this.reservationConfirmationNotificationProcessor.notify(reservation, user);
    this.userRepository.save(user);
    return this.paymentConfirmationDtoAssembler.assemble(
        paymentInformation, reservation.getInvoice(), payerID);
  }
}
