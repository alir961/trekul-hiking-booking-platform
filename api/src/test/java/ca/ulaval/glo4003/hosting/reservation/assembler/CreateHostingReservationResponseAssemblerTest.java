package ca.ulaval.glo4003.hosting.reservation.assembler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ca.ulaval.glo4003.hosting.HostingID;
import ca.ulaval.glo4003.hosting.reservation.dto.CreatedHostingReservationDto;
import ca.ulaval.glo4003.hosting.reservation.response.HostingReservationResponse;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.trip.TripID;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CreateHostingReservationResponseAssemblerTest {
  private static final HostingID ANY_HOSTING_ID = new HostingID(1);
  private static final String ANY_HOSTING_NAME = "anyName";
  private static final TripID ANY_TRIP_ID = TripID.generate();
  private static final Map<Money, Money> ANY_PRICE_RANGE = new HashMap<>();
  private static final CreatedHostingReservationDto ANY_HOSTING_RESERVATION_DTO =
      new CreatedHostingReservationDto(
          ANY_HOSTING_ID, ANY_HOSTING_NAME, ANY_TRIP_ID, ANY_PRICE_RANGE);
  private CreateHostingReservationResponseAssembler assembler;

  @BeforeEach
  void setUp() {
    this.assembler = new CreateHostingReservationResponseAssembler();
  }

  @Test
  void whenAssembling_thenCreateHostingReservationResponseAssembleCorrectly() {
    HostingReservationResponse response = this.assembler.assemble(ANY_HOSTING_RESERVATION_DTO);

    assertEquals(ANY_HOSTING_ID.toString(), response.hostingID());
    assertEquals(ANY_TRIP_ID.toString(), response.tripID());
    assertEquals(ANY_PRICE_RANGE.toString(), response.priceRange());
  }
}
