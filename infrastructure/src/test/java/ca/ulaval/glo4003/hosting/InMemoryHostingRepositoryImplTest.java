package ca.ulaval.glo4003.hosting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.ulaval.glo4003.hiking.HikingRegion;
import ca.ulaval.glo4003.money.Money;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryHostingRepositoryImplTest {
  private static final HostingID A_NON_EXISTING_HOSTING_ID = new HostingID(999);
  private static final String A_HOSTING_NAME = "AUBERGE DU LAC-COMMANDANT";
  private static final HostingID AN_EXISTING_HOSTING_ID = new HostingID(101);
  private static final int DAY_TO_ADD = 7;
  private static final HostingID AN_OTHER_HOSTING_ID = new HostingID(102);
  private static final int A_MAX_CAPACITY = 50;
  private static final HikingRegion A_REGION = HikingRegion.GASPESIE;
  private static final Map<LocalDate, LocalDate> AVAILABLE_PERIOD =
      Map.of(LocalDate.now(), LocalDate.now().plusDays(DAY_TO_ADD));
  private static final Map<Money, Money> A_PRICE_RANGE = Map.of(new Money("100"), new Money("200"));
  private static final Hosting AN_EXISTING_HOSTING =
      new Hosting(
          AN_EXISTING_HOSTING_ID,
          A_HOSTING_NAME,
          A_REGION,
          A_PRICE_RANGE,
          A_MAX_CAPACITY,
          AVAILABLE_PERIOD);

  private InMemoryHostingRepositoryImpl hostingRepository;

  @BeforeEach
  void setUp() {
    this.hostingRepository = new InMemoryHostingRepositoryImpl();
    this.hostingRepository.save(AN_EXISTING_HOSTING);
  }

  @Test
  void givenHostingIsNotInRepository_whenGettingTheHosting_thenReceiveNoHosting() {
    Optional<Hosting> result = this.hostingRepository.get(A_NON_EXISTING_HOSTING_ID);

    assertTrue(result.isEmpty());
  }

  @Test
  void givenHostingIsInRepository_whenGettingTheHosting_thenReceiveTheHosting() {
    Optional<Hosting> result = this.hostingRepository.get(AN_EXISTING_HOSTING_ID);

    assertTrue(result.isPresent());
    assertEquals(AN_EXISTING_HOSTING, result.get());
  }

  @Test
  void givenHostingIsInRepository_whenGettingTheHosting_thenReceiveTheHostingAsDifferentInstance() {
    Optional<Hosting> result = this.hostingRepository.get(AN_EXISTING_HOSTING_ID);

    assertTrue(result.isPresent());
    assertNotSame(AN_EXISTING_HOSTING, result.get());
  }

  @Test
  void givenAllHostsInRepository_whenGettingAll_thenGetAllHosts() {
    List<Hosting> result = this.hostingRepository.getAll();

    assertEquals(1, result.size());
    assertEquals(AN_EXISTING_HOSTING, result.getFirst());
  }

  @Test
  void givenMultipleHostsInRepository_whenGettingAll_thenGetAllHosts() {
    Hosting existingHosting = AN_EXISTING_HOSTING;
    Hosting anotherHosting =
        new Hosting(
            AN_OTHER_HOSTING_ID,
            A_HOSTING_NAME,
            A_REGION,
            A_PRICE_RANGE,
            A_MAX_CAPACITY,
            AVAILABLE_PERIOD);
    this.hostingRepository.save(existingHosting);
    this.hostingRepository.save(anotherHosting);

    List<Hosting> result = this.hostingRepository.getAll();

    assertEquals(2, result.size());
    assertTrue(result.contains(existingHosting));
    assertTrue(result.contains(anotherHosting));
  }
}
