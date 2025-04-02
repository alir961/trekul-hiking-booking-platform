package ca.ulaval.glo4003.transport.assembler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import ca.ulaval.glo4003.transport.dto.ReservableTransportStopDto;
import ca.ulaval.glo4003.transport.response.ReservableTransportStopResponse;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TransportStopResponseAssemblerTest {
  private static final String ANY_LOCATION = "this is a random location";
  private static final LocalDateTime ANY_DEPARTURE_DATE = LocalDateTime.now();
  private static final ReservableTransportStopDto ANY_ORGANIZED_BUS_STOP_DTO =
      new ReservableTransportStopDto(ANY_LOCATION, ANY_DEPARTURE_DATE);
  private ReservableTransportStopResponseAssembler reservableTransportStopResponseAssembler;

  @BeforeEach
  void setUp() {
    this.reservableTransportStopResponseAssembler = new ReservableTransportStopResponseAssembler();
  }

  @Test
  void whenAssembling_thenOrganizedBusStopsAreAssembledCorrectly() {
    List<ReservableTransportStopResponse> responses =
        this.reservableTransportStopResponseAssembler.assemble(List.of(ANY_ORGANIZED_BUS_STOP_DTO));

    assertFalse(responses.isEmpty());
    ReservableTransportStopResponse response = responses.getFirst();

    assertEquals(ANY_DEPARTURE_DATE, response.departure());
    assertEquals(ANY_LOCATION, response.location());
  }
}
