package ca.ulaval.glo4003.transport;

import static ca.ulaval.glo4003.transport.TransportType.CARPOOL;
import static jakarta.ws.rs.core.Response.Status.OK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4003.transport.assembler.ReservableTransportResponseAssembler;
import ca.ulaval.glo4003.transport.assembler.TransportOptionResponseAssembler;
import ca.ulaval.glo4003.transport.dto.ReservableTransportDto;
import ca.ulaval.glo4003.transport.response.ReservableTransportResponse;
import ca.ulaval.glo4003.transport.response.TransportOptionResponse;
import ca.ulaval.glo4003.trip.TripID;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TransportResourceImplTest {
  private static final TransportType ANY_TRANSPORT_TYPE = CARPOOL;
  private static final TripID ANY_TRIP_ID = TripID.generate();
  private static final List<TransportOptionResponse> TRANSPORT_OPTIONS_RESPONSE_FROM_SERVICE =
      new ArrayList<>();
  private ReservableTransportService transportServiceMock;
  private TransportOptionResponseAssembler transportOptionResponseAssemblerMock;
  private ReservableTransportResponseAssembler reservableTransportResponseAssemblerMock;
  private TransportResourceImpl transportResource;

  @BeforeEach
  void setUp() {
    this.transportServiceMock = mock(ReservableTransportService.class);
    this.transportOptionResponseAssemblerMock = mock(TransportOptionResponseAssembler.class);
    this.reservableTransportResponseAssemblerMock =
        mock(ReservableTransportResponseAssembler.class);

    List<TransportOption> serviceResult = new ArrayList<>();
    when(this.transportServiceMock.listTransportOptions(ANY_TRIP_ID)).thenReturn(serviceResult);
    when(this.transportOptionResponseAssemblerMock.assemble(serviceResult))
        .thenReturn(TRANSPORT_OPTIONS_RESPONSE_FROM_SERVICE);

    this.transportResource =
        new TransportResourceImpl(
            this.transportServiceMock,
            this.transportOptionResponseAssemblerMock,
            this.reservableTransportResponseAssemblerMock);
  }

  @Test
  void whenListingTransportOptionsOfTrip_thenReturnListAssembledFromService() {

    Response response = this.transportResource.listTransportOptionsOfTrip(ANY_TRIP_ID);

    assertSame(TRANSPORT_OPTIONS_RESPONSE_FROM_SERVICE, response.getEntity());
  }

  @Test
  void whenListingTransportOptionsOfTrip_thenResponseIsWithStatusOK() {
    Response response = this.transportResource.listTransportOptionsOfTrip(ANY_TRIP_ID);

    assertEquals(OK, response.getStatusInfo());
  }

  @Test
  void whenGettingTransportDetails_thenReturnListAssembledFromService() {
    List<ReservableTransportDto> anyTransportDetailsList =
        List.of(mock(ReservableTransportDto.class));
    when(this.transportServiceMock.getDetailsOfTransports(ANY_TRIP_ID, ANY_TRANSPORT_TYPE))
        .thenReturn(anyTransportDetailsList);

    List<ReservableTransportResponse> expectedResponses =
        List.of(mock(ReservableTransportResponse.class));
    when(this.reservableTransportResponseAssemblerMock.assemble(anyTransportDetailsList))
        .thenReturn(expectedResponses);

    Response response =
        this.transportResource.getTransportDetails(ANY_TRIP_ID, ANY_TRANSPORT_TYPE.toString());

    assertSame(expectedResponses, response.getEntity());
  }

  @Test
  void whenGettingTransportDetails_thenResponseIsWithStatusOK() {
    Response response =
        this.transportResource.getTransportDetails(ANY_TRIP_ID, ANY_TRANSPORT_TYPE.toString());

    assertEquals(OK, response.getStatusInfo());
  }
}
