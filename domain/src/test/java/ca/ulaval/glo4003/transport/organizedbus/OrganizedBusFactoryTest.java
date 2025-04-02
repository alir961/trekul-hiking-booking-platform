package ca.ulaval.glo4003.transport.organizedbus;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.transport.TransportConfig;
import java.util.ArrayList;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OrganizedBusFactoryTest {
  private static final Collection<TransportStop> ANY_STOPS = new ArrayList<>();
  private TransportConfig transportConfigMock;
  private OrganizedBusFactory organizedBusFactory;

  @BeforeEach
  void setUp() {
    this.transportConfigMock = mock(TransportConfig.class);
    this.organizedBusFactory = new OrganizedBusFactory(this.transportConfigMock);
  }

  @Test
  void givenMultipleCreation_whenCreatingBus_thenDoNotProduceSameID() {
    OrganizedBus first = this.organizedBusFactory.createOrganizedBus(ANY_STOPS);
    OrganizedBus second = this.organizedBusFactory.createOrganizedBus(ANY_STOPS);

    assertNotEquals(first.getTransportID(), second.getTransportID());
  }

  @Test
  void whenCreatingBus_thenGetMaximumSeatFromConfig() {
    this.organizedBusFactory.createOrganizedBus(ANY_STOPS);

    verify(this.transportConfigMock).getMaximumSeats();
  }

  @Test
  void whenCreatingBus_thenGetCostFromConfig() {
    this.organizedBusFactory.createOrganizedBus(ANY_STOPS);

    verify(this.transportConfigMock).getCost();
  }
}
