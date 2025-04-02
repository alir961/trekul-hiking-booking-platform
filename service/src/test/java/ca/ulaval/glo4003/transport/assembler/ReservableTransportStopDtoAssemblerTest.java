package ca.ulaval.glo4003.transport.assembler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4003.transport.dto.ReservableTransportStopDto;
import ca.ulaval.glo4003.transport.organizedbus.TransportStop;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ReservableTransportStopDtoAssemblerTest {
  private static final LocalDateTime STOP_DEPARTURE_TIME = LocalDateTime.now();
  private static final String STOP_LOCATION_NAME = "any name";
  private TransportStop transportStopMock;
  private ReservableTransportStopDtoAssembler reservableTransportStopDtoAssembler;

  @BeforeEach
  void setUp() {
    this.reservableTransportStopDtoAssembler = new ReservableTransportStopDtoAssembler();

    this.transportStopMock = mock(TransportStop.class);
    when(this.transportStopMock.getDepartureTime()).thenReturn(STOP_DEPARTURE_TIME);
    when(this.transportStopMock.getLocationName()).thenReturn(STOP_LOCATION_NAME);
  }

  @Test
  void whenAssembling_thenReservableTransportStopDtoListAssembleCorrectly() {
    List<ReservableTransportStopDto> result =
        this.reservableTransportStopDtoAssembler.assemble(List.of(this.transportStopMock));

    assertFalse(result.isEmpty());
    ReservableTransportStopDto firstResult = result.getFirst();

    assertEquals(STOP_DEPARTURE_TIME, firstResult.departureTime());
    assertEquals(STOP_LOCATION_NAME, firstResult.location());
  }
}
