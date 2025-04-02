package ca.ulaval.glo4003.hosting.reservation;

import static org.junit.jupiter.api.Assertions.*;

import ca.ulaval.glo4003.hosting.HostingID;
import ca.ulaval.glo4003.money.Money;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HostingReservationTest {
  private static final HostingID ANY_HOSTING_ID = new HostingID(1);
  private static final HostingID ANY_OTHER_HOSTING_ID = new HostingID(489);
  private static final String ANY_HOSTING_NAME = "AUBERGE DU LAC-COMMANDANT";
  private static final String ANY_OTHER_HOSTING_NAME = "AUBERGE DU LAC";
  private static final Money MIN_PRICE = new Money("100");
  private static final Money OTHER_MIN_PRICE = new Money("140");
  private static final Money MAX_PRICE = new Money("200");
  private static final Map<Money, Money> ANY_PRICE_RANGE = Map.of(MIN_PRICE, MAX_PRICE);
  private static final HostingReservation ANY_HOSTING_RESERVATION =
      new HostingReservation(ANY_HOSTING_ID, ANY_HOSTING_NAME, ANY_PRICE_RANGE);

  @BeforeEach
  void setUp() {}

  @Test
  void givenSameObject_whenCallingEquals_thenObjectIsEqual() {
    assertEquals(ANY_HOSTING_RESERVATION, ANY_HOSTING_RESERVATION);
  }

  @Test
  void
      givenAnotherObjectWithSamePropertiesButDifferentHostingId_whenCallingEquals_thenObjectIsNotEqual() {
    HostingReservation sameHostingReservationDifferentHostingID =
        new HostingReservation(ANY_OTHER_HOSTING_ID, ANY_HOSTING_NAME, ANY_PRICE_RANGE);

    assertNotEquals(ANY_HOSTING_RESERVATION, sameHostingReservationDifferentHostingID);
  }

  @Test
  void
      givenAnotherObjectWithDifferentPropertiesButSameHostingIDAndTripID_whenCallingEquals_thenObjectIsEqual() {
    Map<Money, Money> otherPriceRange = Map.of(OTHER_MIN_PRICE, MAX_PRICE);
    HostingReservation differentHostingReservationSameIDs =
        new HostingReservation(ANY_HOSTING_ID, ANY_OTHER_HOSTING_NAME, otherPriceRange);

    assertEquals(ANY_HOSTING_RESERVATION, differentHostingReservationSameIDs);
  }

  @Test
  void whenCopying_thenCopyIsEqualToOriginal() {
    HostingReservation copied = ANY_HOSTING_RESERVATION.copy();

    assertEquals(ANY_HOSTING_RESERVATION, copied);
  }

  @Test
  void whenCopying_thenCopyIsNotSameInstanceThanOriginal() {
    HostingReservation copied = ANY_HOSTING_RESERVATION.copy();

    assertNotSame(ANY_HOSTING_RESERVATION, copied);
  }
}
