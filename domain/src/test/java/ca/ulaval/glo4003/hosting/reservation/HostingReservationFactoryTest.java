package ca.ulaval.glo4003.hosting.reservation;

import static org.junit.jupiter.api.Assertions.*;

import ca.ulaval.glo4003.hiking.HikingRegion;
import ca.ulaval.glo4003.hosting.Hosting;
import ca.ulaval.glo4003.hosting.HostingID;
import ca.ulaval.glo4003.money.Money;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HostingReservationFactoryTest {
  private static final HostingID ANY_HOSTING_ID = new HostingID(12345);
  private static final String ANY_HOSTING_NAME = "AUBERGE DU LAC-COMMANDANT";
  private static final HikingRegion ANY_HIKING_REGION = HikingRegion.GASPESIE;
  private static final Map<Money, Money> ANY_PRICE_RANGE = new HashMap<>();
  private static final Map<LocalDate, LocalDate> ANY_AVAILABLE_PERIOD = new HashMap<>();
  private static final int ANY_MAX_CAPACITY = 2;
  private static final Hosting ANY_HOSTING =
      new Hosting(
          ANY_HOSTING_ID,
          ANY_HOSTING_NAME,
          ANY_HIKING_REGION,
          ANY_PRICE_RANGE,
          ANY_MAX_CAPACITY,
          ANY_AVAILABLE_PERIOD);
  private HostingReservationFactory hostingReservationFactory;

  @BeforeEach
  void setUp() {
    this.hostingReservationFactory = new HostingReservationFactory();
  }

  @Test
  void whenCreating_thenHostingReservationWithCorrectFieldsIsReturned() {
    HostingReservation createdHostingReservation =
        this.hostingReservationFactory.create(ANY_HOSTING);

    assertEquals(ANY_HOSTING_ID, createdHostingReservation.getHostingID());
    assertEquals(ANY_PRICE_RANGE, createdHostingReservation.getPriceRange());
  }
}
