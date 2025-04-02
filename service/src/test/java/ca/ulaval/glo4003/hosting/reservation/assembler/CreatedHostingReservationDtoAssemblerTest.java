package ca.ulaval.glo4003.hosting.reservation.assembler;

import static org.junit.jupiter.api.Assertions.*;

import ca.ulaval.glo4003.hosting.HostingID;
import ca.ulaval.glo4003.hosting.reservation.HostingReservation;
import ca.ulaval.glo4003.hosting.reservation.dto.CreatedHostingReservationDto;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.trip.TripID;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CreatedHostingReservationDtoAssemblerTest {
  private static final HostingID ANY_HOSTING_ID = new HostingID(Integer.valueOf("12345"));
  private static final String ANY_HOSTING_NAME = "AUBERGE DU LAC-COMMANDANT";
  private static final TripID ANY_TRIP_ID = TripID.generate();
  private static final Money ANY_LOWER_PRICE = new Money(45.4);
  private static final Money ANY_HIGHER_PRICE = new Money(201.);
  private static final Map<Money, Money> ANY_PRICE_RANGE =
      Map.of(ANY_LOWER_PRICE, ANY_HIGHER_PRICE);
  private HostingReservation anyHostingReservation;
  private CreatedHostingReservationDtoAssembler assembler;

  @BeforeEach
  void setUp() {
    this.anyHostingReservation =
        new HostingReservation(ANY_HOSTING_ID, ANY_HOSTING_NAME, ANY_PRICE_RANGE);
    this.assembler = new CreatedHostingReservationDtoAssembler();
  }

  @Test
  void
      givenHostingReservation_whenAssembling_thenCreatedHostingReservationResponseAssembleCorrectly() {
    CreatedHostingReservationDto result =
        this.assembler.assemble(ANY_TRIP_ID, this.anyHostingReservation);

    assertEquals(ANY_HOSTING_ID, result.hostingID());
    assertEquals(ANY_HOSTING_NAME, result.hostingName());
    assertEquals(ANY_TRIP_ID, result.tripID());
    assertEquals(ANY_PRICE_RANGE, result.priceRange());
  }
}
