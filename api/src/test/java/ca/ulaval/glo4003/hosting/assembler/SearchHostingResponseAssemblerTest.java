package ca.ulaval.glo4003.hosting.assembler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ca.ulaval.glo4003.hiking.HikingRegion;
import ca.ulaval.glo4003.hosting.HostingID;
import ca.ulaval.glo4003.hosting.dto.HostingDto;
import ca.ulaval.glo4003.hosting.response.SearchHostingResponse;
import ca.ulaval.glo4003.money.Money;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SearchHostingResponseAssemblerTest {
  private static final HostingID ANY_HOSTING_ID = new HostingID(Integer.valueOf("12345"));
  private static final String ANY_HOSTING_NAME = "AUBERGE DU LAC-COMMANDANT";
  private static final HikingRegion ANY_REGION = HikingRegion.GASPESIE;
  private static final int ANY_MAX_CAPACITY = 23;
  private static final int ANY_AVAILABLE_CAPACITY = 14;
  private static final Map<LocalDate, LocalDate> ANY_AVAILABILITY_PERIOD =
      Map.of(LocalDate.of(2024, 10, 3), LocalDate.of(2024, 12, 5));
  private static final Money MIN_PRICE = new Money("50");
  private static final Money MAX_PRICE = new Money("130");
  private static final Map<Money, Money> ANY_PRICE_RANGE = Map.of(MIN_PRICE, MAX_PRICE);
  private HostingDto anyHostingDto;
  private SearchHostingResponseAssembler assembler;

  @BeforeEach
  void setUp() {
    this.anyHostingDto =
        new HostingDto(
            ANY_HOSTING_ID,
            ANY_HOSTING_NAME,
            ANY_REGION,
            ANY_PRICE_RANGE,
            ANY_MAX_CAPACITY,
            ANY_AVAILABLE_CAPACITY,
            ANY_AVAILABILITY_PERIOD);
    this.assembler = new SearchHostingResponseAssembler();
  }

  @Test
  void givenHostingList_whenAssembling_thenSearchHostingResponseAssembleCorrectly() {
    List<SearchHostingResponse> responseList = this.assembler.assemble(List.of(this.anyHostingDto));

    SearchHostingResponse response = responseList.getFirst();

    assertEquals(ANY_HOSTING_ID.toString(), response.hostingID());
    assertEquals(ANY_HOSTING_NAME, response.hostingName());
    assertEquals(ANY_REGION.toString(), response.region());
    assertEquals(ANY_AVAILABLE_CAPACITY, response.availableCapacity());
    assertEquals(ANY_PRICE_RANGE.toString(), response.priceRange());
    assertEquals(ANY_AVAILABILITY_PERIOD.toString(), response.availabilityPeriod());
  }
}
