package ca.ulaval.glo4003.trip.assembler;

import static ca.ulaval.glo4003.trip.TripStatus.FULL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4003.hiking.Hiking;
import ca.ulaval.glo4003.hiking.assembler.HikingResponseAssembler;
import ca.ulaval.glo4003.hiking.response.HikingResponse;
import ca.ulaval.glo4003.hosting.assembler.HostingResponseAssembler;
import ca.ulaval.glo4003.hosting.reservation.HostingReservation;
import ca.ulaval.glo4003.hosting.response.HostingResponse;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.trip.TripID;
import ca.ulaval.glo4003.trip.TripStatus;
import ca.ulaval.glo4003.trip.dto.TripDto;
import ca.ulaval.glo4003.trip.response.SearchTripResponse;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SearchTripResponseAssemblerTest {
  private static final TripID ANY_TRIP_ID = TripID.generate();
  private static final int ANY_AVAILABLE_SEATS = 2;
  private static final LocalDate ANY_DATE = LocalDate.now();
  private static final Hiking HIKING_DUMMY = mock(Hiking.class);
  private static final HikingResponse HIKING_RESPONSE_DUMMY = mock(HikingResponse.class);
  private static final Money ANY_MONEY = new Money("970.12");
  private static final HostingReservation HOSTING_RESERVATION_DUMMY =
      mock(HostingReservation.class);
  private static final HostingResponse HOSTING_RESPONSE_DUMMY = mock(HostingResponse.class);
  private static final TripStatus ANY_TRIP_STATUS = FULL;
  private TripDto anyTripDto;
  private SearchTripResponseAssembler assembler;

  @BeforeEach
  void setUp() {
    HikingResponseAssembler hikingResponseAssemblerStub = mock(HikingResponseAssembler.class);
    when(hikingResponseAssemblerStub.assemble(HIKING_DUMMY)).thenReturn(HIKING_RESPONSE_DUMMY);
    HostingResponseAssembler hostingResponseAssembler = mock(HostingResponseAssembler.class);
    when(hostingResponseAssembler.assemble(HOSTING_RESERVATION_DUMMY))
        .thenReturn(HOSTING_RESPONSE_DUMMY);
    this.anyTripDto =
        new TripDto(
            HIKING_DUMMY,
            ANY_DATE,
            ANY_AVAILABLE_SEATS,
            ANY_TRIP_ID,
            ANY_MONEY,
            FULL,
            HOSTING_RESERVATION_DUMMY);
    this.assembler = new SearchTripResponseAssembler(hikingResponseAssemblerStub);
  }

  @Test
  void givenTripList_whenAssembling_thenSearchTripResponseAssembleCorrectly() {
    List<SearchTripResponse> responseList = this.assembler.assemble(List.of(this.anyTripDto));

    SearchTripResponse response = responseList.getFirst();

    assertEquals(HIKING_RESPONSE_DUMMY, response.hikingResponse());
    assertEquals(ANY_AVAILABLE_SEATS, response.availableSeats());
    assertEquals(ANY_TRIP_STATUS.toString(), response.status());
    assertEquals(ANY_DATE.toString(), response.startDate());
    assertEquals(ANY_MONEY.toString(), response.price());
    assertEquals(ANY_TRIP_ID.toString(), response.id());
  }
}
