package ca.ulaval.glo4003.reservation;

import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.invoice.InvoiceID;
import ca.ulaval.glo4003.invoice.assembler.InvoiceResponseAssembler;
import ca.ulaval.glo4003.invoice.dto.InvoiceDto;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.payment.PaymentTrace;
import ca.ulaval.glo4003.traveler.TravelerID;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ReservationResourceImplTest {
  private static final TravelerID ANY_TRAVELER_ID = new TravelerID("ANY_TRAVELER_ID");
  private static final ReservationID ANY_RESERVATION_ID = ReservationID.generate();
  private static final InvoiceID ANY_INVOICE_ID = InvoiceID.generate();
  private static final InvoiceDto ANY_INVOICE_DTO = createAnyInvoiceDto();

  private CancelReservationService cancelReservationServiceMock;
  private InvoiceResponseAssembler invoiceResponseAssemblerMock;

  private ReservationResourceImpl reservationResource;

  @BeforeEach
  void setUp() {
    this.cancelReservationServiceMock = mock(CancelReservationService.class);
    this.invoiceResponseAssemblerMock = mock(InvoiceResponseAssembler.class);

    this.reservationResource =
        new ReservationResourceImpl(
            this.cancelReservationServiceMock, this.invoiceResponseAssemblerMock);
  }

  @Test
  void whenCancelReservation_thenCancelReservation() {
    this.reservationResource.cancelReservation(ANY_TRAVELER_ID, ANY_RESERVATION_ID);

    verify(this.cancelReservationServiceMock)
        .cancelReservation(ANY_TRAVELER_ID, ANY_RESERVATION_ID);
  }

  @Test
  void whenCancelReservation_thenAssembleResponse() {
    when(this.cancelReservationServiceMock.cancelReservation(ANY_TRAVELER_ID, ANY_RESERVATION_ID))
        .thenReturn(ANY_INVOICE_DTO);

    this.reservationResource.cancelReservation(ANY_TRAVELER_ID, ANY_RESERVATION_ID);

    verify(this.invoiceResponseAssemblerMock).assemble(ANY_INVOICE_DTO);
  }

  private static InvoiceDto createAnyInvoiceDto() {
    Map<String, Money> anyItems = new HashMap<>();
    Map<String, Money> anyTaxes = new HashMap<>();
    Money anyTotal = new Money("200.50");
    List<PaymentTrace> anyPaymentTraces = new ArrayList<>();
    return new InvoiceDto(ANY_INVOICE_ID, anyItems, anyTaxes, anyTotal, anyTotal, anyPaymentTraces);
  }
}
