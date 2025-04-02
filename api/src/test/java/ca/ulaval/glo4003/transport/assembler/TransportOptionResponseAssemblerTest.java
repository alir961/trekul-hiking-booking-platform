package ca.ulaval.glo4003.transport.assembler;

import static org.junit.jupiter.api.Assertions.*;

import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.transport.TransportOption;
import ca.ulaval.glo4003.transport.TransportType;
import ca.ulaval.glo4003.transport.response.TransportOptionResponse;
import java.util.List;
import org.junit.jupiter.api.Test;

public class TransportOptionResponseAssemblerTest {
  private static final TransportType ANY_TRANSPORT_TYPE = TransportType.CARPOOL;
  private static final int ANY_OPTIONS_QUANTITY = 203;
  private static final boolean ANY_AVAILABILITY = false;
  private static final String ANY_COST_STRING = "3012.40";
  private static final TransportOption ANY_TRANSPORT_OPTION =
      new TransportOption(
          ANY_TRANSPORT_TYPE, ANY_AVAILABILITY, ANY_OPTIONS_QUANTITY, new Money(ANY_COST_STRING));
  private final TransportOptionResponseAssembler transportOptionResponseAssembler =
      new TransportOptionResponseAssembler();

  @Test
  void whenAssembling_thenTransportOptionsAreAssembledCorrectly() {
    List<TransportOptionResponse> responses =
        this.transportOptionResponseAssembler.assemble(List.of(ANY_TRANSPORT_OPTION));

    assertFalse(responses.isEmpty());
    TransportOptionResponse response = responses.getFirst();

    assertEquals(ANY_TRANSPORT_TYPE.toString(), response.transportType());
    assertEquals(ANY_TRANSPORT_TYPE.getDescription(), response.description());
    assertEquals(ANY_OPTIONS_QUANTITY, response.optionsQuantity());
    assertEquals(ANY_AVAILABILITY, response.isAvailable());
    assertEquals(ANY_COST_STRING, response.minimumCost());
  }
}
