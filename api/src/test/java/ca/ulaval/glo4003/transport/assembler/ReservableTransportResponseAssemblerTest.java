package ca.ulaval.glo4003.transport.assembler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.transport.TransportID;
import ca.ulaval.glo4003.transport.dto.ReservableTransportDto;
import ca.ulaval.glo4003.transport.dto.ReservableTransportStopDto;
import ca.ulaval.glo4003.transport.response.ReservableTransportResponse;
import ca.ulaval.glo4003.transport.response.ReservableTransportStopResponse;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ReservableTransportResponseAssemblerTest {
  private static final TransportID ANY_TRANSPORT_ID = TransportID.generate();
  private static final String ANY_COST_STRING = "7102.30";
  private static final int ANY_AVAILABLE_SEATS = 301;
  private static final List<ReservableTransportStopDto> ORGANIZED_BUS_STOP_DTO_LIST =
      new ArrayList<>();
  private static final ReservableTransportDto ANY_ORGANIZED_BUS_DTO =
      new ReservableTransportDto(
          ANY_TRANSPORT_ID,
          ANY_AVAILABLE_SEATS,
          new Money(ANY_COST_STRING),
          ORGANIZED_BUS_STOP_DTO_LIST);
  private static final List<ReservableTransportStopResponse> ORGANIZED_BUS_STOP_RESPONSE_LIST =
      new ArrayList<>();
  private ReservableTransportResponseAssembler reservableTransportResponseAssembler;

  @BeforeEach
  void setUp() {
    ReservableTransportStopResponseAssembler reservableTransportStopResponseAssemblerMock =
        mock(ReservableTransportStopResponseAssembler.class);
    when(reservableTransportStopResponseAssemblerMock.assemble(ORGANIZED_BUS_STOP_DTO_LIST))
        .thenReturn(ORGANIZED_BUS_STOP_RESPONSE_LIST);

    this.reservableTransportResponseAssembler =
        new ReservableTransportResponseAssembler(reservableTransportStopResponseAssemblerMock);
  }

  @Test
  void whenAssembling_thenOrganizedBusResponseIsAssembledCorrectly() {
    List<ReservableTransportDto> organizedBusDtoList = List.of(ANY_ORGANIZED_BUS_DTO);

    List<ReservableTransportResponse> response =
        this.reservableTransportResponseAssembler.assemble(organizedBusDtoList);

    assertSame(ORGANIZED_BUS_STOP_RESPONSE_LIST, response.getFirst().stops());
    assertEquals(ANY_TRANSPORT_ID.toString(), response.getFirst().transportID());
    assertEquals(ANY_COST_STRING, response.getFirst().cost());
    assertEquals(ANY_AVAILABLE_SEATS, response.getFirst().availableSeats());
  }
}
