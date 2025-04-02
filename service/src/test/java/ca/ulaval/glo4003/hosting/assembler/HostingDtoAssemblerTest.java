package ca.ulaval.glo4003.hosting.assembler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ca.ulaval.glo4003.hiking.HikingRegion;
import ca.ulaval.glo4003.hosting.Hosting;
import ca.ulaval.glo4003.hosting.HostingID;
import ca.ulaval.glo4003.hosting.dto.HostingDto;
import ca.ulaval.glo4003.money.Money;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HostingDtoAssemblerTest {

  private static final HostingID ANY_HOSTING_ID = new HostingID(123);
  private static final String ANY_HOSTING_NAME = "AUBERGE DU LAC-COMMANDANT";
  private static final int ANY_MAX_CAPACITY = 4;
  private static final HikingRegion ANY_REGION = HikingRegion.SAGLAC;
  private static final Map<LocalDate, LocalDate> ANY_AVAILABLE_PERIOD =
      Map.of(LocalDate.of(2024, 6, 1), LocalDate.of(2024, 9, 1));
  private static final Map<Money, Money> ANY_AVAILABLE_AMOUNT =
      Map.of(new Money("100"), new Money("200"));
  private Hosting anyHosting;
  private HostingDtoAssembler assembler;

  @BeforeEach
  void setUp() {
    this.anyHosting =
        new Hosting(
            ANY_HOSTING_ID,
            ANY_HOSTING_NAME,
            ANY_REGION,
            ANY_AVAILABLE_AMOUNT,
            ANY_MAX_CAPACITY,
            ANY_AVAILABLE_PERIOD);
    this.assembler = new HostingDtoAssembler();
  }

  @Test
  void givenHostingList_whenAssembling_thenHostingDtoAssembledCorrectly() {
    List<HostingDto> hostingDtoList = this.assembler.assemble(List.of(this.anyHosting));

    HostingDto result = hostingDtoList.getFirst();

    assertEquals(ANY_HOSTING_ID, result.id());
    assertEquals(ANY_HOSTING_NAME, result.name());
    assertEquals(ANY_MAX_CAPACITY, result.maxCapacity());
    assertEquals(ANY_REGION, result.region());
    assertEquals(ANY_AVAILABLE_PERIOD, result.availabilityPeriod());
    assertEquals(ANY_AVAILABLE_AMOUNT, result.priceRange());
  }
}
