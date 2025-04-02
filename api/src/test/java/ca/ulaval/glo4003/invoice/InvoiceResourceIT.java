package ca.ulaval.glo4003.invoice;

import static ca.ulaval.glo4003.ListResponseMapper.mapListResponse;
import static jakarta.ws.rs.client.ClientBuilder.newClient;
import static jakarta.ws.rs.client.Entity.json;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static jakarta.ws.rs.core.Response.Status.*;
import static jakarta.ws.rs.core.Response.Status.BAD_REQUEST;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4003.ApplicationMocker;
import ca.ulaval.glo4003.auth.AuthBinder;
import ca.ulaval.glo4003.auth.authentication.AuthenticationRequestFilterMocker;
import ca.ulaval.glo4003.exception.*;
import ca.ulaval.glo4003.invoice.assembler.InvoiceResponseAssembler;
import ca.ulaval.glo4003.invoice.dto.InvoiceDto;
import ca.ulaval.glo4003.invoice.exception.InvoiceNotFoundException;
import ca.ulaval.glo4003.invoice.response.ConfirmationCodeResponse;
import ca.ulaval.glo4003.invoice.response.InvoiceResponse;
import ca.ulaval.glo4003.invoice.response.InvoiceShortResponse;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.payment.exception.PaymentCheckoutException;
import ca.ulaval.glo4003.payment.request.PaymentRequest;
import ca.ulaval.glo4003.payment.response.PaymentResponse;
import ca.ulaval.glo4003.payment.usecase.PaymentUseCase;
import ca.ulaval.glo4003.reservation.exception.ReservationNotFoundException;
import ca.ulaval.glo4003.token.exception.UserAuthenticationException;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.user.exception.UserNotFoundException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InvoiceResourceIT {

  private static final ApplicationMocker APP = new ApplicationMocker();
  private static final String ANY_AUTH_TOKEN = "2gig2";
  private static final TravelerID ANY_AUTHENTICATED_USER_ID = new TravelerID("userId");
  private static final TravelerID ANY_OWNER_USER_ID = new TravelerID("anyUserId");
  private static final InvoiceID INVOICE_ID = InvoiceID.generate();
  private static final String BASE_INVOICES_URL =
      String.format(
          "http://localhost:8080/api/trekul/users/%s/invoices", ANY_OWNER_USER_ID.getValue());
  private static final String SPECIFIC_INVOICE_URL =
      String.format("%s/%s", BASE_INVOICES_URL, INVOICE_ID);
  private static final Map<String, Money> MONEYS = Map.of("default", new Money("100"));
  private static final Map<String, String> STRING_MONEYS = Map.of("default", "100");
  private static final InvoiceDto INVOICE_DTO =
      new InvoiceDto(INVOICE_ID, MONEYS, MONEYS, new Money("200"), new Money("200"), emptyList());
  private static final List<InvoiceDto> INVOICES = List.of(INVOICE_DTO);
  private static final InvoiceShortResponse INVOICE_SHORT_RESPONSE =
      new InvoiceShortResponse(INVOICE_ID.toString(), "200", "200");
  private static final InvoiceResponse INVOICE_RESPONSE =
      new InvoiceResponse(
          INVOICE_ID.toString(), STRING_MONEYS, STRING_MONEYS, "200", "200", emptyList());
  private static final Money MONEY = new Money("100");
  private static final String CARD_NUMBER = "0123456789";
  private static final String EXPIRATION_DATE = "0828";
  private static final Integer CCV = 420;
  private static final String CARD_HOLDER_NAME = "John DOE";
  private static final Double PAYMENT = 1000.0D;
  private static final String ANY_VALID_CONFIRMATION_CODE = "CONFIRM-12345";

  private static final PaymentRequest PAYMENT_REQUEST =
      new PaymentRequest(CARD_NUMBER, EXPIRATION_DATE, CCV, CARD_HOLDER_NAME, PAYMENT);
  private static final PaymentResponse PAYMENT_RESPONSE =
      new PaymentResponse(MONEY.toString(), MONEY.toString(), MONEY.toString(), INVOICE_RESPONSE);

  private TripInvoiceService tripInvoiceServiceMock;
  private PaymentUseCase paymentUseCaseMock;
  private InvoiceResponseAssembler invoiceResponseAssembler;
  private AuthenticationRequestFilterMocker authenticationRequestFilterMocker;

  private Client client;

  @BeforeEach
  void setUp() throws Exception {
    this.tripInvoiceServiceMock = mock(TripInvoiceService.class);
    this.invoiceResponseAssembler = mock(InvoiceResponseAssembler.class);
    this.paymentUseCaseMock = mock(PaymentUseCase.class);
    this.authenticationRequestFilterMocker =
        new AuthenticationRequestFilterMocker(ANY_AUTHENTICATED_USER_ID);

    InvoiceResource invoiceResource =
        new InvoiceResourceImpl(
            this.tripInvoiceServiceMock, this.invoiceResponseAssembler, this.paymentUseCaseMock);

    APP.startServer(
        new TrekULSystemExceptionMapper(),
        new TrekULAuthenticationExceptionMapper(),
        new TrekULExceptionMapper(),
        new TrekULNotFoundExceptionMapper(),
        new AuthBinder(),
        this.authenticationRequestFilterMocker,
        invoiceResource);
    this.client = newClient();
  }

  @AfterEach
  void tearDown() throws Exception {
    APP.stopServer();
    this.client.close();
  }

  @Test
  void whenGetInvoices_thenSuccess() {
    // GIVEN
    WebTarget target = this.client.target(BASE_INVOICES_URL);

    when(this.tripInvoiceServiceMock.listInvoices(ANY_AUTHENTICATED_USER_ID)).thenReturn(INVOICES);
    when(this.invoiceResponseAssembler.assembleShort(INVOICES))
        .thenReturn(List.of(INVOICE_SHORT_RESPONSE));

    // WHEN
    Response response = target.request(APPLICATION_JSON_TYPE).get();

    // THEN
    List<HashMap<String, Object>> invoices = mapListResponse(response);
    HashMap<String, Object> invoice = invoices.getFirst();

    assertEquals(OK.getStatusCode(), response.getStatus());
    assertEquals(1, invoices.size());
    assertEquals(INVOICE_ID.toString(), invoice.get("invoiceID").toString());
  }

  @Test
  void whenGetInvoices_thenUnauthorized() {
    // GIVEN
    WebTarget target = this.client.target(BASE_INVOICES_URL);
    this.authenticationRequestFilterMocker.asUnauthorized();

    // WHEN
    Response response = target.request(APPLICATION_JSON_TYPE).get();

    // THEN
    assertEquals(UNAUTHORIZED.getStatusCode(), response.getStatus());
  }

  @Test
  void whenGetInvoice_thenSuccess() {
    // GIVEN
    WebTarget target = this.client.target(SPECIFIC_INVOICE_URL);

    when(this.tripInvoiceServiceMock.getInvoice(
            ANY_OWNER_USER_ID, ANY_AUTHENTICATED_USER_ID, INVOICE_ID))
        .thenReturn(INVOICE_DTO);
    when(this.invoiceResponseAssembler.assemble(INVOICE_DTO)).thenReturn(INVOICE_RESPONSE);

    // WHEN
    Response response = target.request(APPLICATION_JSON_TYPE).get();

    // THEN
    InvoiceResponse invoice = response.readEntity(InvoiceResponse.class);

    assertEquals(INVOICE_ID.toString(), invoice.invoiceID());
  }

  @Test
  void givenOtherTravelerID_whenGetInvoice_thenSuccess() {
    // GIVEN
    WebTarget target = this.client.target(SPECIFIC_INVOICE_URL + "?2FA=" + ANY_AUTH_TOKEN);

    when(this.tripInvoiceServiceMock.getInvoice(
            ANY_OWNER_USER_ID, ANY_AUTHENTICATED_USER_ID, INVOICE_ID))
        .thenReturn(INVOICE_DTO);
    when(this.invoiceResponseAssembler.assemble(INVOICE_DTO)).thenReturn(INVOICE_RESPONSE);

    // WHEN
    Response response = target.request(APPLICATION_JSON_TYPE).get();

    // THEN
    InvoiceResponse invoice = response.readEntity(InvoiceResponse.class);

    assertEquals(INVOICE_ID.toString(), invoice.invoiceID());
  }

  @Test
  void givenUserNotAuthenticated_whenGetInvoice_thenUnauthorized() {
    // GIVEN
    WebTarget target = this.client.target(SPECIFIC_INVOICE_URL);
    this.authenticationRequestFilterMocker.asUnauthorized();

    // WHEN
    Response response = target.request(APPLICATION_JSON_TYPE).get();

    // THEN
    assertEquals(UNAUTHORIZED.getStatusCode(), response.getStatus());
  }

  @Test
  void whenPayingInvoice_thenSuccess() {
    // GIVEN
    WebTarget target = this.client.target(SPECIFIC_INVOICE_URL + ":pay");

    when(this.paymentUseCaseMock.payInvoice(
            PAYMENT_REQUEST, INVOICE_ID, ANY_OWNER_USER_ID, ANY_AUTHENTICATED_USER_ID))
        .thenReturn(PAYMENT_RESPONSE);

    // WHEN
    Response response = target.request(APPLICATION_JSON_TYPE).post(json(PAYMENT_REQUEST));

    // THEN
    assertEquals(OK.getStatusCode(), response.getStatus());
  }

  @Test
  void given2FACode_whenPayingInvoice_thenSuccess() {
    // GIVEN
    WebTarget target = this.client.target(SPECIFIC_INVOICE_URL + ":pay?2FA=" + ANY_AUTH_TOKEN);

    when(this.paymentUseCaseMock.payInvoice(
            PAYMENT_REQUEST, INVOICE_ID, ANY_OWNER_USER_ID, ANY_AUTHENTICATED_USER_ID))
        .thenReturn(PAYMENT_RESPONSE);

    // WHEN
    Response response = target.request(APPLICATION_JSON_TYPE).post(json(PAYMENT_REQUEST));

    // THEN
    assertEquals(OK.getStatusCode(), response.getStatus());
  }

  @Test
  void givenBadUserId_whenPayingInvoice_thenBadRequest() {
    // GIVEN
    WebTarget target = this.client.target(SPECIFIC_INVOICE_URL + ":pay");

    when(this.paymentUseCaseMock.payInvoice(
            PAYMENT_REQUEST, INVOICE_ID, ANY_OWNER_USER_ID, ANY_AUTHENTICATED_USER_ID))
        .thenThrow(UserNotFoundException.class);

    // WHEN
    Response response = target.request(APPLICATION_JSON_TYPE).post(json(PAYMENT_REQUEST));

    // THEN
    assertEquals(NOT_FOUND.getStatusCode(), response.getStatus());
  }

  @Test
  void givenNoReservation_whenPayingInvoice_thenNotFound() {
    // GIVEN
    WebTarget target = this.client.target(SPECIFIC_INVOICE_URL + ":pay");

    when(this.paymentUseCaseMock.payInvoice(
            PAYMENT_REQUEST, INVOICE_ID, ANY_OWNER_USER_ID, ANY_AUTHENTICATED_USER_ID))
        .thenThrow(ReservationNotFoundException.class);

    // WHEN
    Response response = target.request(APPLICATION_JSON_TYPE).post(json(PAYMENT_REQUEST));

    // THEN
    assertEquals(NOT_FOUND.getStatusCode(), response.getStatus());
  }

  @Test
  void givenPaymentFailure_whenPayingInvoice_thenBadRequest() {
    // GIVEN
    WebTarget target = this.client.target(SPECIFIC_INVOICE_URL + ":pay");

    when(this.paymentUseCaseMock.payInvoice(
            PAYMENT_REQUEST, INVOICE_ID, ANY_OWNER_USER_ID, ANY_AUTHENTICATED_USER_ID))
        .thenThrow(PaymentCheckoutException.class);

    // WHEN
    Response response = target.request(APPLICATION_JSON_TYPE).post(json(PAYMENT_REQUEST));

    // THEN
    assertEquals(BAD_REQUEST.getStatusCode(), response.getStatus());
  }

  @Test
  void givenCheckoutFailure_whenPayingInvoice_thenBadRequest() {
    // GIVEN
    WebTarget target = this.client.target(SPECIFIC_INVOICE_URL + ":pay");

    when(this.paymentUseCaseMock.payInvoice(
            PAYMENT_REQUEST, INVOICE_ID, ANY_OWNER_USER_ID, ANY_AUTHENTICATED_USER_ID))
        .thenThrow(PaymentCheckoutException.class);

    // WHEN
    Response response = target.request(APPLICATION_JSON_TYPE).post(json(PAYMENT_REQUEST));

    // THEN
    assertEquals(BAD_REQUEST.getStatusCode(), response.getStatus());
  }

  @Test
  void givenUserNotAuthenticated_whenPayingInvoice_thenUnauthorized() {
    // GIVEN
    WebTarget target = this.client.target(SPECIFIC_INVOICE_URL + ":pay");

    when(this.paymentUseCaseMock.payInvoice(
            PAYMENT_REQUEST, INVOICE_ID, ANY_OWNER_USER_ID, ANY_AUTHENTICATED_USER_ID))
        .thenThrow(UserAuthenticationException.class);

    // WHEN
    Response response = target.request(APPLICATION_JSON_TYPE).post(json(PAYMENT_REQUEST));

    // THEN
    assertEquals(UNAUTHORIZED.getStatusCode(), response.getStatus());
  }

  @Test
  void whenGetConfirmationCode_thenSuccess() {
    // GIVEN
    WebTarget target = this.client.target(SPECIFIC_INVOICE_URL + "/code");

    when(this.tripInvoiceServiceMock.getConfirmationCode(ANY_AUTHENTICATED_USER_ID, INVOICE_ID))
        .thenReturn(ANY_VALID_CONFIRMATION_CODE);

    // WHEN
    Response response = target.request(APPLICATION_JSON_TYPE).get();

    // THEN
    ConfirmationCodeResponse confirmationCodeResponse =
        response.readEntity(ConfirmationCodeResponse.class);
    assertEquals(OK.getStatusCode(), response.getStatus());
    assertEquals(ANY_VALID_CONFIRMATION_CODE, confirmationCodeResponse.confirmationCode());
  }

  @Test
  void whenGetConfirmationCode_thenUnauthorized() {
    // GIVEN
    WebTarget target = this.client.target(SPECIFIC_INVOICE_URL + "/code");
    this.authenticationRequestFilterMocker.asUnauthorized();

    // WHEN
    Response response = target.request(APPLICATION_JSON_TYPE).get();

    // THEN
    assertEquals(UNAUTHORIZED.getStatusCode(), response.getStatus());
  }

  @Test
  void givenInvalidInvoice_whenGetConfirmationCode_thenReturnsNotFoundResponse() {
    // GIVEN
    WebTarget target = this.client.target(SPECIFIC_INVOICE_URL + "/code");

    when(this.tripInvoiceServiceMock.getConfirmationCode(ANY_AUTHENTICATED_USER_ID, INVOICE_ID))
        .thenThrow(new InvoiceNotFoundException());

    // WHEN
    Response response = target.request(APPLICATION_JSON_TYPE).get();

    // THEN
    // Assert the HTTP status
    assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());

    // Assert the error response body
    ErrorResponse errorResponse = response.readEntity(ErrorResponse.class);
    assertEquals("INVOICE_NOT_FOUND", errorResponse.message());
    assertEquals("Invoice not found", errorResponse.details());
  }

  @Test
  void whenGetConfirmationCodeAndServiceFails_thenInternalServerError() {
    // GIVEN
    WebTarget target = this.client.target(SPECIFIC_INVOICE_URL + "/code");

    when(this.tripInvoiceServiceMock.getConfirmationCode(ANY_AUTHENTICATED_USER_ID, INVOICE_ID))
        .thenThrow(new RuntimeException("Unexpected error"));

    // WHEN
    Response response = target.request(APPLICATION_JSON_TYPE).get();

    // THEN
    assertEquals(INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
  }
}
