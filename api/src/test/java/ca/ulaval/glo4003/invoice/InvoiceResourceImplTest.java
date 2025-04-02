package ca.ulaval.glo4003.invoice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.invoice.assembler.InvoiceResponseAssembler;
import ca.ulaval.glo4003.invoice.dto.InvoiceDto;
import ca.ulaval.glo4003.invoice.response.ConfirmationCodeResponse;
import ca.ulaval.glo4003.invoice.response.InvoiceResponse;
import ca.ulaval.glo4003.invoice.response.InvoiceShortResponse;
import ca.ulaval.glo4003.invoice.response.PaymentTraceResponse;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.payment.PaymentTrace;
import ca.ulaval.glo4003.payment.request.PaymentRequest;
import ca.ulaval.glo4003.payment.response.PaymentResponse;
import ca.ulaval.glo4003.payment.usecase.PaymentUseCase;
import ca.ulaval.glo4003.traveler.TravelerID;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InvoiceResourceImplTest {
  private static final InvoiceID ANY_INVOICE_ID = InvoiceID.generate();
  private static final TravelerID ANY_AUTHENTICATED_USER_ID = new TravelerID("123");
  private static final TravelerID ANY_TRAVELER_ID = new TravelerID("3262");
  private static final String VALID_CONFIRMATION_CODE = "CONFIRM-12345";

  private static final PaymentRequest ANY_PAYMENT_REQUEST = mock(PaymentRequest.class);
  private TripInvoiceService tripInvoiceServiceMock;
  private InvoiceResponseAssembler invoiceResponseAssemblerMock;
  private PaymentUseCase paymentUseCaseMock;
  private InvoiceResourceImpl invoiceResource;

  @BeforeEach
  void setUp() {
    this.tripInvoiceServiceMock = mock(TripInvoiceService.class);
    this.invoiceResponseAssemblerMock = mock(InvoiceResponseAssembler.class);
    this.paymentUseCaseMock = mock(PaymentUseCase.class);
    this.invoiceResource =
        new InvoiceResourceImpl(
            this.tripInvoiceServiceMock,
            this.invoiceResponseAssemblerMock,
            this.paymentUseCaseMock);
  }

  @Test
  void whenGetInvoices_thenGetInvoicesFromAuthenticatedUser() {
    this.invoiceResource.getInvoices(ANY_TRAVELER_ID);

    verify(this.tripInvoiceServiceMock).listInvoices(ANY_TRAVELER_ID);
  }

  @Test
  void givenAuthenticatedUser_whenGetInvoices_thenReturnAssembledShortResponses() {
    List<InvoiceDto> anyUserInvoices = new ArrayList<>();
    List<InvoiceShortResponse> anyAssembledShortResponses = new ArrayList<>();
    this.givenAuthenticatedUserRetrievesListInvoice(anyUserInvoices, anyAssembledShortResponses);

    Response returnedResponse = this.invoiceResource.getInvoices(ANY_TRAVELER_ID);

    assertTrue(returnedResponse.hasEntity());
    assertEquals(returnedResponse.getEntity(), anyAssembledShortResponses);
  }

  @Test
  void givenAuthenticatedUser_whenGetInvoices_thenReturnHttpOk() {
    List<InvoiceDto> anyUserInvoices = new ArrayList<>();
    List<InvoiceShortResponse> assembledShortResponses = new ArrayList<>();
    this.givenAuthenticatedUserRetrievesListInvoice(anyUserInvoices, assembledShortResponses);

    Response returnedResponse = this.invoiceResource.getInvoices(ANY_TRAVELER_ID);

    assertEquals(HttpStatus.OK_200, returnedResponse.getStatus());
  }

  private void givenAuthenticatedUserRetrievesListInvoice(
      List<InvoiceDto> userInvoices, List<InvoiceShortResponse> assembledShortResponses) {
    when(this.tripInvoiceServiceMock.listInvoices(ANY_TRAVELER_ID)).thenReturn(userInvoices);
    when(this.invoiceResponseAssemblerMock.assembleShort(userInvoices))
        .thenReturn(assembledShortResponses);
  }

  @Test
  void whenGetInvoice_thenReturnAssembledInvoiceResponses() {
    InvoiceResponse assembledInvoiceResponse = this.givenAnyUserRetrievesOneInvoice();

    Response returnedResponse =
        this.invoiceResource.getInvoice(ANY_TRAVELER_ID, ANY_INVOICE_ID, ANY_AUTHENTICATED_USER_ID);

    assertTrue(returnedResponse.hasEntity());
    assertEquals(returnedResponse.getEntity(), assembledInvoiceResponse);
  }

  @Test
  void givenAuthenticatedUser_whenGetInvoice_thenReturnHttpOk() {
    this.givenAnyUserRetrievesOneInvoice();

    Response returnedResponse =
        this.invoiceResource.getInvoice(ANY_TRAVELER_ID, ANY_INVOICE_ID, ANY_AUTHENTICATED_USER_ID);

    assertEquals(HttpStatus.OK_200, returnedResponse.getStatus());
  }

  private InvoiceResponse givenAnyUserRetrievesOneInvoice() {
    InvoiceDto anyInvoiceDto = this.createAnyInvoiceDto();
    InvoiceResponse assembledInvoiceResponse = this.createAnyInvoiceResponse();
    when(this.tripInvoiceServiceMock.getInvoice(
            ANY_TRAVELER_ID, ANY_AUTHENTICATED_USER_ID, ANY_INVOICE_ID))
        .thenReturn(anyInvoiceDto);
    when(this.invoiceResponseAssemblerMock.assemble(anyInvoiceDto))
        .thenReturn(assembledInvoiceResponse);
    return assembledInvoiceResponse;
  }

  private InvoiceResponse createAnyInvoiceResponse() {
    Map<String, String> anyItems = new HashMap<>();
    Map<String, String> anyTaxes = new HashMap<>();
    String anyTotal = "200.50";
    List<PaymentTraceResponse> anyPaymentTraceResponses = new ArrayList<>();
    return new InvoiceResponse(
        ANY_INVOICE_ID.toString(),
        anyItems,
        anyTaxes,
        anyTotal,
        anyTotal,
        anyPaymentTraceResponses);
  }

  private InvoiceDto createAnyInvoiceDto() {
    Map<String, Money> anyItems = new HashMap<>();
    Map<String, Money> anyTaxes = new HashMap<>();
    Money anyTotal = new Money("200.50");
    List<PaymentTrace> anyPaymentTraces = new ArrayList<>();
    return new InvoiceDto(ANY_INVOICE_ID, anyItems, anyTaxes, anyTotal, anyTotal, anyPaymentTraces);
  }

  @Test
  void whenPayingInvoice_thenReturnAssembledPaymentResponses() {
    PaymentResponse expectedResponse = this.mockPaymentResponse();

    Response returnedResponse =
        this.invoiceResource.payInvoice(
            ANY_TRAVELER_ID, ANY_INVOICE_ID, ANY_PAYMENT_REQUEST, ANY_AUTHENTICATED_USER_ID);

    assertTrue(returnedResponse.hasEntity());
    assertEquals(returnedResponse.getEntity(), expectedResponse);
  }

  @Test
  void whenPayingInvoice_thenReturnHttpOk() {
    Response returnedResponse =
        this.invoiceResource.payInvoice(
            ANY_TRAVELER_ID, ANY_INVOICE_ID, ANY_PAYMENT_REQUEST, ANY_AUTHENTICATED_USER_ID);

    assertEquals(HttpStatus.OK_200, returnedResponse.getStatus());
  }

  private PaymentResponse mockPaymentResponse() {
    PaymentResponse mockedPaymentResponse = mock(PaymentResponse.class);
    when(this.paymentUseCaseMock.payInvoice(
            ANY_PAYMENT_REQUEST, ANY_INVOICE_ID, ANY_TRAVELER_ID, ANY_AUTHENTICATED_USER_ID))
        .thenReturn(mockedPaymentResponse);

    return mockedPaymentResponse;
  }

  @Test
  void whenGetConfirmationCode_thenCallTripInvoiceServiceWithCorrectParameters() {
    this.invoiceResource.getConfirmationCode(ANY_TRAVELER_ID, ANY_INVOICE_ID);

    verify(this.tripInvoiceServiceMock).getConfirmationCode(ANY_TRAVELER_ID, ANY_INVOICE_ID);
  }

  @Test
  void givenValidUserAndInvoice_whenGetConfirmationCode_thenReturnsCodeSuccessfully() {
    when(this.tripInvoiceServiceMock.getConfirmationCode(ANY_TRAVELER_ID, ANY_INVOICE_ID))
        .thenReturn(VALID_CONFIRMATION_CODE);

    Response response = this.invoiceResource.getConfirmationCode(ANY_TRAVELER_ID, ANY_INVOICE_ID);
    ConfirmationCodeResponse responseBody = (ConfirmationCodeResponse) response.getEntity();

    assertTrue(response.hasEntity());
    assertEquals(HttpStatus.OK_200, response.getStatus());

    assertEquals(VALID_CONFIRMATION_CODE, responseBody.confirmationCode());
  }
}
