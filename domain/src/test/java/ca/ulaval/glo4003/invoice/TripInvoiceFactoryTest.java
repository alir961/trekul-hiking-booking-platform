package ca.ulaval.glo4003.invoice;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.tax.TaxStrategy;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.trip.Trip;
import ca.ulaval.glo4003.trip.TripID;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TripInvoiceFactoryTest {
  private static final InvoiceID ANY_INVOICE_ID = InvoiceID.generate();
  private static final Trip TRIP_STUB = mock(Trip.class);
  private static final TripID TRIP_STUB_ID = TripID.generate();
  private static final Money TRIP_STUB_COST = new Money("120421");
  private static final TravelerID ANY_USER_ID = new TravelerID("gd0svi");
  private static final TaxStrategy TAX_STRATEGY_DUMMY = mock(TaxStrategy.class);
  private static final List<TravelerID> ANY_TRAVELER_IDS = new ArrayList<>();
  private TripInvoiceFactory tripInvoiceFactory;

  @BeforeEach
  void setUp() {
    when(TRIP_STUB.getCost()).thenReturn(TRIP_STUB_COST);
    when(TRIP_STUB.getId()).thenReturn(TRIP_STUB_ID);

    this.tripInvoiceFactory = new TripInvoiceFactory(TAX_STRATEGY_DUMMY);
  }

  @Test
  void givenMultipleCreation_whenCreating_thenInvoicesDoNotHaveSameID() {
    TripInvoice first = this.tripInvoiceFactory.create(ANY_USER_ID, TRIP_STUB, ANY_TRAVELER_IDS);
    TripInvoice second = this.tripInvoiceFactory.create(ANY_USER_ID, TRIP_STUB, ANY_TRAVELER_IDS);

    assertNotEquals(first.getID(), second.getID());
  }

  @Test
  void whenCreating_thenInvoiceContainsTripItem() {
    TripInvoice result = this.tripInvoiceFactory.create(ANY_USER_ID, TRIP_STUB, ANY_TRAVELER_IDS);
    Map<BillableItems, Money> items = result.getItems(ANY_USER_ID);
    BillableItems tripKey = BillableItems.TRIP;

    assertTrue(items.containsKey(tripKey));
    assertEquals(TRIP_STUB_COST, items.get(tripKey));
  }
}
