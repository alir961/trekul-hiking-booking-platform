package ca.ulaval.glo4003.trip.assembler;

import static ca.ulaval.glo4003.trip.TripStatus.BLOCKED;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ca.ulaval.glo4003.distance.Distance;
import ca.ulaval.glo4003.hiking.Hiking;
import ca.ulaval.glo4003.hiking.HikingDifficulty;
import ca.ulaval.glo4003.hiking.HikingLocation;
import ca.ulaval.glo4003.hiking.HikingRegion;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.transport.ReservableTransport;
import ca.ulaval.glo4003.transport.TransportCollection;
import ca.ulaval.glo4003.transport.TransportType;
import ca.ulaval.glo4003.trip.Trip;
import ca.ulaval.glo4003.trip.TripID;
import ca.ulaval.glo4003.trip.TripStatus;
import ca.ulaval.glo4003.trip.dto.TripDto;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TripDtoAssemblerTest {
  private static final Map<TransportType, List<ReservableTransport>> ANY_TRANSPORT_MAP =
      new HashMap<>();
  private static final TransportCollection ANY_TRANSPORT_COLLECTION =
      new TransportCollection(ANY_TRANSPORT_MAP);
  private static final TripID ANY_TRIP_ID = TripID.generate();
  private static final int ANY_MAXIMUM_SEATS = 2;
  private static final LocalDate ANY_DATE = LocalDate.now();
  private static final HikingRegion ANY_REGION = HikingRegion.GASPESIE;
  private static final Money ANY_MONEY = new Money("1004");
  private static final HikingDifficulty ANY_HIKING_DIFFICULTY = HikingDifficulty.HARD;
  private static final Distance ANY_HIKING_DISTANCE = new Distance("103");
  private static final HikingLocation ANY_HIKING_LOCATION =
      new HikingLocation(ANY_REGION, "Any location name");
  private static final Hiking ANY_HIKING =
      new Hiking(ANY_HIKING_LOCATION, ANY_HIKING_DIFFICULTY, ANY_HIKING_DISTANCE);
  private static final TripStatus ANY_TRIP_STATUS = BLOCKED;
  private Trip anyTrip;
  private TripDtoAssembler assembler;

  @BeforeEach
  void setUp() {
    this.anyTrip =
        new Trip(
            ANY_TRIP_ID,
            ANY_MAXIMUM_SEATS,
            ANY_DATE,
            ANY_HIKING,
            ANY_MONEY,
            ANY_TRIP_STATUS,
            ANY_TRANSPORT_COLLECTION);
    this.assembler = new TripDtoAssembler();
  }

  @Test
  void givenTripList_whenAssembling_thenSearchTripResponseAssembleCorrectly() {
    List<TripDto> responseList = this.assembler.assemble(List.of(this.anyTrip));

    TripDto result = responseList.getFirst();

    assertEquals(ANY_HIKING, result.hiking());
    assertEquals(this.anyTrip.getAvailableSeats(), result.availableSeats());
    assertEquals(ANY_DATE, result.date());
    assertEquals(ANY_MONEY, result.cost());
    assertEquals(ANY_TRIP_ID, result.tripID());
    assertEquals(ANY_TRIP_STATUS, result.status());
  }
}
