package ca.ulaval.glo4003.hosting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.hosting.assembler.SearchHostingResponseAssembler;
import ca.ulaval.glo4003.hosting.dto.HostingDto;
import ca.ulaval.glo4003.hosting.reservation.HostingReservationService;
import ca.ulaval.glo4003.hosting.reservation.assembler.CreateHostingReservationResponseAssembler;
import ca.ulaval.glo4003.hosting.reservation.dto.CreatedHostingReservationDto;
import ca.ulaval.glo4003.hosting.reservation.response.HostingReservationResponse;
import ca.ulaval.glo4003.hosting.response.SearchHostingResponse;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.trip.TripID;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HostingResourceImplTest {
  private static final String ANY_HOSTING_NAME = "anyHostingName";
  private static final TripID ANY_TRIP_ID = TripID.generate();
  private static final String ANY_HOSTING_ID_STRING = "1";
  private static final Map<Money, Money> ANY_PRICE_RANGE = new HashMap<>();
  private static final String ANY_PRICE_RANGE_STRING = "anyPriceRange";
  private static final HostingID ANY_HOSTING_ID =
      new HostingID(Integer.valueOf(ANY_HOSTING_ID_STRING));
  private static final List<HostingDto> ANY_HOSTING_LIST = new ArrayList<>();
  private static final List<SearchHostingResponse> ANY_HOSTING_RESPONSE_LIST = new ArrayList<>();
  private static final CreatedHostingReservationDto ANY_CREATED_HOSTING_RESERVATION_DTO =
      new CreatedHostingReservationDto(
          ANY_HOSTING_ID, ANY_HOSTING_NAME, ANY_TRIP_ID, ANY_PRICE_RANGE);
  private static final HostingReservationResponse ANY_HOSTING_RESERVATION_RESPONSE =
      new HostingReservationResponse(
          ANY_HOSTING_ID_STRING, ANY_TRIP_ID.toString(), ANY_PRICE_RANGE_STRING);
  private HostingService hostingServiceMock;
  private SearchHostingResponseAssembler hostingResponseAssemblerMock;
  private HostingReservationService hostingReservationServiceMock;
  private CreateHostingReservationResponseAssembler hostingReservationResponseAssemblerMock;
  private HostingResourceImpl hostingResource;

  @BeforeEach
  void setUp() {
    this.hostingServiceMock = mock(HostingService.class);
    this.hostingResponseAssemblerMock = mock(SearchHostingResponseAssembler.class);
    this.hostingReservationServiceMock = mock(HostingReservationService.class);
    this.hostingReservationResponseAssemblerMock =
        mock(CreateHostingReservationResponseAssembler.class);
    this.hostingResource =
        new HostingResourceImpl(
            this.hostingServiceMock,
            this.hostingResponseAssemblerMock,
            this.hostingReservationServiceMock,
            this.hostingReservationResponseAssemblerMock);
    when(this.hostingServiceMock.searchCompatibleHosts(ANY_TRIP_ID)).thenReturn(ANY_HOSTING_LIST);
    when(this.hostingResponseAssemblerMock.assemble(ANY_HOSTING_LIST))
        .thenReturn(ANY_HOSTING_RESPONSE_LIST);
    when(this.hostingReservationServiceMock.bookHosting(ANY_TRIP_ID, ANY_HOSTING_ID))
        .thenReturn(ANY_CREATED_HOSTING_RESERVATION_DTO);
    when(this.hostingReservationResponseAssemblerMock.assemble(ANY_CREATED_HOSTING_RESERVATION_DTO))
        .thenReturn(ANY_HOSTING_RESERVATION_RESPONSE);
  }

  @Test
  void whenFindingHostsInRegion_thenServiceSearchesForCompatibleHosts() {
    this.hostingResource.findHostsInRegion(ANY_TRIP_ID);

    verify(this.hostingServiceMock).searchCompatibleHosts(ANY_TRIP_ID);
  }

  @Test
  void whenFindingHostsInRegion_thenAssemblerAssemblesResponse() {
    this.hostingResource.findHostsInRegion(ANY_TRIP_ID);

    verify(this.hostingResponseAssemblerMock).assemble(ANY_HOSTING_LIST);
  }

  @Test
  void whenFindingHostsInRegion_thenReturnCorrectResponse() {
    Response returnedResponse = this.hostingResource.findHostsInRegion(ANY_TRIP_ID);

    assertTrue(returnedResponse.hasEntity());
    assertEquals(returnedResponse.getEntity(), ANY_HOSTING_RESPONSE_LIST);
  }

  @Test
  void whenBookingHosting_thenServiceBooksHosting() {
    this.hostingResource.bookHosting(ANY_TRIP_ID, ANY_HOSTING_ID_STRING);

    verify(this.hostingReservationServiceMock).bookHosting(ANY_TRIP_ID, ANY_HOSTING_ID);
  }

  @Test
  void whenBookingHosting_thenHostingReservationResponseAssemblerAssemblesResponse() {
    this.hostingResource.bookHosting(ANY_TRIP_ID, ANY_HOSTING_ID_STRING);

    verify(this.hostingReservationResponseAssemblerMock)
        .assemble(ANY_CREATED_HOSTING_RESERVATION_DTO);
  }

  @Test
  void whenBookingHosting_thenReturnCorrectResponse() {
    Response returnedResponse =
        this.hostingResource.bookHosting(ANY_TRIP_ID, ANY_HOSTING_ID_STRING);

    assertTrue(returnedResponse.hasEntity());
    assertEquals(returnedResponse.getEntity(), ANY_HOSTING_RESERVATION_RESPONSE);
  }
}
