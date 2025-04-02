package ca.ulaval.glo4003.profile.assembler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4003.hiking.Hiking;
import ca.ulaval.glo4003.hiking.assembler.HikingResponseAssembler;
import ca.ulaval.glo4003.hiking.response.HikingResponse;
import ca.ulaval.glo4003.hosting.assembler.HostingResponseAssembler;
import ca.ulaval.glo4003.hosting.reservation.HostingReservation;
import ca.ulaval.glo4003.hosting.response.HostingResponse;
import ca.ulaval.glo4003.invoice.assembler.InvoiceResponseAssembler;
import ca.ulaval.glo4003.invoice.dto.InvoiceDto;
import ca.ulaval.glo4003.invoice.response.InvoiceShortResponse;
import ca.ulaval.glo4003.profile.dto.ProfileTripDto;
import ca.ulaval.glo4003.profile.response.ProfileTripResponse;
import ca.ulaval.glo4003.reservation.assembler.ReservationResponseAssembler;
import ca.ulaval.glo4003.reservation.dto.CreatedReservationDto;
import ca.ulaval.glo4003.reservation.response.ReservationStatusResponse;
import ca.ulaval.glo4003.trip.TripID;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProfileTripResponseAssemblerTest {
  private static final TripID ANY_TRIP_ID = TripID.generate();
  private static final LocalDate ANY_DATE = LocalDate.now();
  private static final Hiking HIKING_DUMMY = mock(Hiking.class);
  private static final HikingResponse HIKING_RESPONSE_DUMMY = mock(HikingResponse.class);
  private static final InvoiceDto INVOICE_DTO_DUMMY = mock(InvoiceDto.class);
  private static final InvoiceShortResponse INVOICE_SHORT_RESPONSE_DUMMY =
      mock(InvoiceShortResponse.class);
  private static final CreatedReservationDto CREATED_RESERVATION_DTO_DUMMY =
      mock(CreatedReservationDto.class);
  private static final ReservationStatusResponse RESERVATION_RESPONSE_DUMMY =
      mock(ReservationStatusResponse.class);
  private static final HostingReservation HOSTING_RESERVATION_DUMMY =
      mock(HostingReservation.class);
  private static final HostingResponse HOSTING_RESPONSE_DUMMY = mock(HostingResponse.class);
  private ProfileTripDto anyProfileTripDto;
  private ProfileTripResponseAssembler assembler;

  @BeforeEach
  void setUp() {
    HikingResponseAssembler hikingResponseAssemblerStub = mock(HikingResponseAssembler.class);
    when(hikingResponseAssemblerStub.assemble(HIKING_DUMMY)).thenReturn(HIKING_RESPONSE_DUMMY);

    InvoiceResponseAssembler invoiceResponseAssemblerStub = mock(InvoiceResponseAssembler.class);
    when(invoiceResponseAssemblerStub.assembleShort(INVOICE_DTO_DUMMY))
        .thenReturn(INVOICE_SHORT_RESPONSE_DUMMY);

    ReservationResponseAssembler essentialReservationElementsResponseAssembler =
        mock(ReservationResponseAssembler.class);
    when(essentialReservationElementsResponseAssembler.assembleStatus(
            CREATED_RESERVATION_DTO_DUMMY))
        .thenReturn(RESERVATION_RESPONSE_DUMMY);

    HostingResponseAssembler hostingResponseAssemblerStub = mock(HostingResponseAssembler.class);
    when(hostingResponseAssemblerStub.assemble(HOSTING_RESERVATION_DUMMY))
        .thenReturn(HOSTING_RESPONSE_DUMMY);

    this.anyProfileTripDto =
        new ProfileTripDto(
            HIKING_DUMMY,
            ANY_TRIP_ID,
            ANY_DATE,
            INVOICE_DTO_DUMMY,
            CREATED_RESERVATION_DTO_DUMMY,
            HOSTING_RESERVATION_DUMMY);
    this.assembler =
        new ProfileTripResponseAssembler(
            invoiceResponseAssemblerStub,
            hikingResponseAssemblerStub,
            essentialReservationElementsResponseAssembler,
            hostingResponseAssemblerStub);
  }

  @Test
  void givenTripList_whenAssembling_thenProfileTripResponseAssembleCorrectly() {
    List<ProfileTripResponse> responseList =
        this.assembler.assemble(List.of(this.anyProfileTripDto));

    ProfileTripResponse response = responseList.getFirst();

    assertSame(HIKING_RESPONSE_DUMMY, response.hiking());
    assertSame(INVOICE_SHORT_RESPONSE_DUMMY, response.invoice());
    assertSame(RESERVATION_RESPONSE_DUMMY, response.reservation());
    assertEquals(ANY_DATE.toString(), response.startDate());
    assertEquals(ANY_TRIP_ID.toString(), response.tripId());
  }
}
