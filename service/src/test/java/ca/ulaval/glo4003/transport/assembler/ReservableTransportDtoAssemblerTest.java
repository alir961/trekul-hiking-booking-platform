package ca.ulaval.glo4003.transport.assembler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.transport.ReservableTransport;
import ca.ulaval.glo4003.transport.TransportID;
import ca.ulaval.glo4003.transport.dto.ReservableTransportDto;
import ca.ulaval.glo4003.transport.dto.ReservableTransportStopDto;
import ca.ulaval.glo4003.transport.organizedbus.OrganizedBus;
import ca.ulaval.glo4003.transport.organizedbus.TransportStop;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ReservableTransportDtoAssemblerTest {
  private static final TransportID ANY_TRANSPORT_ID = TransportID.generate();
  private static final int ANY_AVAILABLE_SEATS = 4203;
  private static final Money ANY_TRANSPORT_COST = new Money("503");
  private static final OrganizedBus ANY_ORGANIZED_BUS = mock(OrganizedBus.class);
  private static final List<ReservableTransportStopDto> RESERVABLE_TRANSPORT_STOP_DTO_LIST =
      new ArrayList<>();
  private ReservableTransportDtoAssembler reservableTransportDtoAssembler;

  @BeforeEach
  void setUp() {
    ReservableTransportStopDtoAssembler reservableTransportStopDtoAssembler =
        mock(ReservableTransportStopDtoAssembler.class);
    this.reservableTransportDtoAssembler =
        new ReservableTransportDtoAssembler(reservableTransportStopDtoAssembler);
    List<TransportStop> transportStops = new ArrayList<>();
    when(ANY_ORGANIZED_BUS.getBusStops()).thenReturn(transportStops);
    when(ANY_ORGANIZED_BUS.getCurrentSeatsAvailable()).thenReturn(ANY_AVAILABLE_SEATS);
    when(ANY_ORGANIZED_BUS.getTransportID()).thenReturn(ANY_TRANSPORT_ID);
    when(ANY_ORGANIZED_BUS.getCost()).thenReturn(ANY_TRANSPORT_COST);

    when(reservableTransportStopDtoAssembler.assemble(transportStops))
        .thenReturn(RESERVABLE_TRANSPORT_STOP_DTO_LIST);
  }

  @Test
  void whenAssembling_thenAssembleReservableTransportDtoCorrectly() {
    List<ReservableTransport> reservableTransportList = List.of(ANY_ORGANIZED_BUS);

    List<ReservableTransportDto> result =
        this.reservableTransportDtoAssembler.assemble(reservableTransportList);

    assertSame(RESERVABLE_TRANSPORT_STOP_DTO_LIST, result.getFirst().stops());
    assertEquals(ANY_AVAILABLE_SEATS, result.getFirst().availableSeats());
    assertEquals(ANY_TRANSPORT_COST, result.getFirst().cost());
    assertEquals(ANY_TRANSPORT_ID, result.getFirst().transportID());
  }
}
