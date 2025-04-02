package ca.ulaval.glo4003.invoice;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.confirmationcode.ConfirmationCode;
import ca.ulaval.glo4003.invoice.assembler.InvoiceDtoAssembler;
import ca.ulaval.glo4003.invoice.dto.InvoiceDto;
import ca.ulaval.glo4003.invoice.exception.InvoiceNotFoundException;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.user.User;
import ca.ulaval.glo4003.traveler.user.UserRepository;
import ca.ulaval.glo4003.traveler.user.exception.UserNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TripInvoiceServiceTest {
  private static final TravelerID NON_EXISTING_USER_ID = new TravelerID("40fus");
  private static final TravelerID EXISTING_USER_ID = new TravelerID("0g9b");
  private static final TravelerID ACCESSOR_TRAVELER_ID = new TravelerID("hh3h");
  private static final User EXISTING_USER_MOCK = mock(User.class);
  private static final InvoiceID NON_EXISTING_INVOICE_ID = InvoiceID.generate();
  private static final InvoiceID EXISTING_INVOICE_ID = InvoiceID.generate();
  private static final TripInvoice EXISTING_TRIP_INVOICE_MOCK = mock(TripInvoice.class);
  private static final String ANY_EXCEPTED_CONFIRMATION_CODE = "CONFIRM-12345";
  private static final List<InvoiceDto> EXISTING_USER_INVOICES_LIST_DTO_RESULT = new ArrayList<>();
  private static final InvoiceDto EXISTING_INVOICE_DTO_RESULT = mock(InvoiceDto.class);

  private UserRepository userRepositoryMock;
  private InvoiceDtoAssembler invoiceDtoAssemblerMock;
  private TripInvoiceService tripInvoiceService;

  @BeforeEach
  void setUp() {
    this.userRepositoryMock = mock(UserRepository.class);
    this.invoiceDtoAssemblerMock = mock(InvoiceDtoAssembler.class);
    this.tripInvoiceService =
        new TripInvoiceService(this.userRepositoryMock, this.invoiceDtoAssemblerMock);
  }

  @Test
  void givenUserDoesNotExist_whenListingInvoices_thenThrowUserNotFoundException() {
    this.mockWithNonExistingUser();
    assertThrows(
        UserNotFoundException.class,
        () -> this.tripInvoiceService.listInvoices(NON_EXISTING_USER_ID));
  }

  @Test
  void givenUserExists_whenListingInvoices_thenReturnInvoicesOfUser() {
    this.mockWithExistingUser();
    List<InvoiceDto> result = this.tripInvoiceService.listInvoices(EXISTING_USER_ID);

    assertSame(EXISTING_USER_INVOICES_LIST_DTO_RESULT, result);
  }

  @Test
  void givenInvoiceDoesNotExist_whenGettingInvoice_thenThrowInvoiceNotFoundException() {
    this.mockWithNonExistingInvoice();
    assertThrows(
        InvoiceNotFoundException.class,
        () ->
            this.tripInvoiceService.getInvoice(
                EXISTING_USER_ID, ACCESSOR_TRAVELER_ID, NON_EXISTING_INVOICE_ID));
  }

  @Test
  void givenInvoiceExists_whenGettingInvoice_thenReturnInvoice() {
    this.mockWithExistingInvoice();
    InvoiceDto result =
        this.tripInvoiceService.getInvoice(
            EXISTING_USER_ID, ACCESSOR_TRAVELER_ID, EXISTING_INVOICE_ID);

    assertSame(EXISTING_INVOICE_DTO_RESULT, result);
  }

  @Test
  void givenUserDoesNotExist_whenGettingConfirmationCode_thenThrowUserNotFoundException() {
    this.mockWithNonExistingUser();
    assertThrows(
        UserNotFoundException.class,
        () ->
            this.tripInvoiceService.getConfirmationCode(NON_EXISTING_USER_ID, EXISTING_INVOICE_ID));
  }

  @Test
  void givenInvoiceDoesNotExist_whenGettingConfirmationCode_thenThrowInvoiceNotFoundException() {
    this.mockWithExistingUser();
    this.mockWithNonExistingInvoice();

    when(EXISTING_USER_MOCK.retrieveConfirmationCodeForReservation(NON_EXISTING_INVOICE_ID))
        .thenThrow(InvoiceNotFoundException.class);

    assertThrows(
        InvoiceNotFoundException.class,
        () ->
            this.tripInvoiceService.getConfirmationCode(EXISTING_USER_ID, NON_EXISTING_INVOICE_ID));
  }

  @Test
  void givenUserAndInvoiceExist_whenGettingConfirmationCode_thenReturnConfirmationCode() {
    this.mockWithExistingUser();
    this.mockWithExistingInvoice();
    ConfirmationCode expectedConfirmationCode =
        new ConfirmationCode(ANY_EXCEPTED_CONFIRMATION_CODE);
    when(EXISTING_USER_MOCK.retrieveConfirmationCodeForReservation(EXISTING_INVOICE_ID))
        .thenReturn(expectedConfirmationCode);

    String result =
        this.tripInvoiceService.getConfirmationCode(EXISTING_USER_ID, EXISTING_INVOICE_ID);

    assertSame(expectedConfirmationCode.getConfirmationCode(), result);
  }

  private void mockWithExistingUser() {
    Collection<TripInvoice> existingUserInvoicesIDs = new ArrayList<>();
    when(EXISTING_USER_MOCK.getInvoices()).thenReturn(existingUserInvoicesIDs);
    when(this.userRepositoryMock.getById(EXISTING_USER_ID))
        .thenReturn(Optional.of(EXISTING_USER_MOCK));

    List<TripInvoice> existingUserInvoices = new ArrayList<>();
    when(EXISTING_USER_MOCK.getInvoices()).thenReturn(existingUserInvoices);
    when(this.invoiceDtoAssemblerMock.assemble(existingUserInvoices))
        .thenReturn(EXISTING_USER_INVOICES_LIST_DTO_RESULT);
  }

  private void mockWithNonExistingUser() {
    when(this.userRepositoryMock.getById(NON_EXISTING_USER_ID)).thenReturn(Optional.empty());
  }

  private void mockWithNonExistingInvoice() {
    when(this.userRepositoryMock.getById(EXISTING_USER_ID))
        .thenReturn(Optional.ofNullable(EXISTING_USER_MOCK));
    assert EXISTING_USER_MOCK != null;
    when(EXISTING_USER_MOCK.getInvoices()).thenReturn(List.of());
    when(EXISTING_USER_MOCK.getTripInvoice(NON_EXISTING_INVOICE_ID)).thenReturn(Optional.empty());
  }

  private void mockWithExistingInvoice() {
    when(this.userRepositoryMock.getById(EXISTING_USER_ID))
        .thenReturn(Optional.ofNullable(EXISTING_USER_MOCK));
    assert EXISTING_USER_MOCK != null;
    when(EXISTING_USER_MOCK.getInvoices()).thenReturn(List.of(EXISTING_TRIP_INVOICE_MOCK));
    when(EXISTING_USER_MOCK.getTripInvoice(EXISTING_INVOICE_ID))
        .thenReturn(Optional.of(EXISTING_TRIP_INVOICE_MOCK));
    when(this.invoiceDtoAssemblerMock.assemble(EXISTING_TRIP_INVOICE_MOCK, ACCESSOR_TRAVELER_ID))
        .thenReturn(EXISTING_INVOICE_DTO_RESULT);
  }
}
