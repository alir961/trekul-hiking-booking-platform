package ca.ulaval.glo4003.transport;

import static ca.ulaval.glo4003.transport.TransportType.CARPOOL;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4003.transport.assembler.ReservableTransportDtoAssembler;
import ca.ulaval.glo4003.transport.dto.ReservableTransportDto;
import ca.ulaval.glo4003.trip.Trip;
import ca.ulaval.glo4003.trip.TripID;
import ca.ulaval.glo4003.trip.TripRepository;
import ca.ulaval.glo4003.trip.exception.TripNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ReservableTransportServiceTest {
  private static final TripID ANY_TRIP_ID = TripID.generate();
  private static final TransportType ANY_TRANSPORT_TYPE = CARPOOL;
  private static final List<TransportOption> TRANSPORT_PROCESSOR_OPTIONS = new ArrayList<>();
  private static final TripID EXISTING_TRIP_ID = TripID.generate();
  private static final List<ReservableTransport> EXISTING_RESERVABLE_TRANSPORT_LIST =
      List.of(Mockito.mock(ReservableTransport.class));
  private static final Trip EXISTING_TRIP = mock(Trip.class);
  private TripRepository tripRepositoryMock;
  private ReservableTransportDtoAssembler reservableTransportDtoAssemblerMock;
  private ReservableTransportService reservableTransportService;

  @BeforeEach
  void setUp() {
    this.tripRepositoryMock = mock(TripRepository.class);
    this.reservableTransportDtoAssemblerMock = mock(ReservableTransportDtoAssembler.class);

    this.reservableTransportService =
        new ReservableTransportService(
            this.tripRepositoryMock, this.reservableTransportDtoAssemblerMock);
    when(this.tripRepositoryMock.get(ANY_TRIP_ID)).thenReturn(Optional.of(EXISTING_TRIP));
    when(EXISTING_TRIP.readCurrentTransportOptions()).thenReturn(TRANSPORT_PROCESSOR_OPTIONS);
  }

  @Test
  void givenValidTripId_whenGettingDetailsOfTransport_thenReturnDtoListOfReservableTransport() {
    List<ReservableTransportDto> expectedDtoList = List.of(mock(ReservableTransportDto.class));
    when(this.tripRepositoryMock.get(EXISTING_TRIP_ID)).thenReturn(Optional.of(EXISTING_TRIP));
    when(EXISTING_TRIP.getReservableTransports(ANY_TRANSPORT_TYPE))
        .thenReturn(EXISTING_RESERVABLE_TRANSPORT_LIST);
    when(this.reservableTransportDtoAssemblerMock.assemble(EXISTING_RESERVABLE_TRANSPORT_LIST))
        .thenReturn(expectedDtoList);

    List<ReservableTransportDto> result =
        this.reservableTransportService.getDetailsOfTransports(
            EXISTING_TRIP_ID, ANY_TRANSPORT_TYPE);

    assertSame(expectedDtoList, result);
  }

  @Test
  void givenInvalidTripId_whenGettingDetailsOfTransport_thenThrowTripNotFoundException() {
    assertThrows(
        TripNotFoundException.class,
        () ->
            this.reservableTransportService.getDetailsOfTransports(
                EXISTING_TRIP_ID, ANY_TRANSPORT_TYPE));
  }

  @Test
  void whenListingTransportOptions_thenReturnOptionsFromTransportProcessor() {
    List<TransportOption> result =
        this.reservableTransportService.listTransportOptions(ANY_TRIP_ID);

    assertSame(TRANSPORT_PROCESSOR_OPTIONS, result);
  }
}
