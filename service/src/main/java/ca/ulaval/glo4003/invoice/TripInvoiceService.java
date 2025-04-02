package ca.ulaval.glo4003.invoice;

import ca.ulaval.glo4003.confirmationcode.ConfirmationCode;
import ca.ulaval.glo4003.invoice.assembler.InvoiceDtoAssembler;
import ca.ulaval.glo4003.invoice.dto.InvoiceDto;
import ca.ulaval.glo4003.invoice.exception.InvoiceNotFoundException;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.user.User;
import ca.ulaval.glo4003.traveler.user.UserRepository;
import ca.ulaval.glo4003.traveler.user.exception.UserNotFoundException;
import java.util.Collection;
import java.util.List;

public class TripInvoiceService {
  private final UserRepository userRepository;
  private final InvoiceDtoAssembler invoiceDtoAssembler;

  public TripInvoiceService(
      UserRepository userRepository, InvoiceDtoAssembler invoiceDtoAssembler) {
    this.userRepository = userRepository;
    this.invoiceDtoAssembler = invoiceDtoAssembler;
  }

  public List<InvoiceDto> listInvoices(TravelerID userID) {
    User user = this.userRepository.getById(userID).orElseThrow(UserNotFoundException::new);

    Collection<TripInvoice> invoices = user.getInvoices();

    return this.invoiceDtoAssembler.assemble(invoices);
  }

  public InvoiceDto getInvoice(TravelerID ownerID, TravelerID accessorID, InvoiceID invoiceID) {

    User user = this.userRepository.getById(ownerID).orElseThrow(UserNotFoundException::new);

    TripInvoice invoice = user.getTripInvoice(invoiceID).orElseThrow(InvoiceNotFoundException::new);

    return this.invoiceDtoAssembler.assemble(invoice, accessorID);
  }

  public String getConfirmationCode(TravelerID userID, InvoiceID invoiceID) {
    User user = this.userRepository.getById(userID).orElseThrow(UserNotFoundException::new);
    ConfirmationCode confirmationCode = user.retrieveConfirmationCodeForReservation(invoiceID);

    return confirmationCode.getConfirmationCode();
  }
}
