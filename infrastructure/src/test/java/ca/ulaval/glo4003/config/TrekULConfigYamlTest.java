package ca.ulaval.glo4003.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ca.ulaval.glo4003.backgroundtask.BackgroundTaskConfig;
import ca.ulaval.glo4003.backgroundtask.SchedulerConfig;
import ca.ulaval.glo4003.config.dto.*;
import ca.ulaval.glo4003.group.GroupConfig;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.reservation.ReservationConfig;
import ca.ulaval.glo4003.token.TokenConfig;
import ca.ulaval.glo4003.transport.TransportConfig;
import ca.ulaval.glo4003.trip.TripConfig;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TrekULConfigYamlTest {
  private static final String ANY_DEFAULT_ORGANIZED_BUS_COST = "15";
  private static final int ANY_DEFAULT_START_DATE_DAY = 1;
  private static final int ANY_DEFAULT_START_DATE_MONTH = 4;
  private static final int ANY_DEFAULT_START_DATE_YEAR = 2020;
  private static final String ANY_RESERVATION_DEFAULT_COST = "25";
  private static final int ANY_LOGIN_EXPIRATION_IN_SECONDS = 5;
  private static final int ANY_GUEST_2FA_EXPIRATION_IN_SECONDS = 402;
  private static final String ANY_TRIP_DEFAULT_COST = "5";
  private static final int ANY_TRIP_MAXIMUM_SEAT = 2;
  private static final int ANY_TASK_POOL_SIZE = 1;
  private static final int ANY_SHUTDOWN_TIME_IN_SECONDS = 14;
  private static final TrekULConfigDto ANY_TREKUL_CONFIG_DTO = createAnyTrekUlConfigDto();
  private static final Map<String, BackgroundTaskConfig> ANY_TASKS = new HashMap<>();
  private static final String ANY_TASK_NAME = "task1";
  private static final int ANY_ORGANIZED_BUS_SEATS = 2;
  private static final int ANY_TIME_IN_SECONDS_BEFORE_TRIP_BLOCK = 2;
  private static final int ANY_MAXIMUM_GROUP_SIZE = 2;
  private static final long ANY_NOTIFICATION_THRESHOLD = 5;

  private static final BackgroundTaskConfig ANY_BACKGROUND_TASK_CONFIG =
      new BackgroundTaskConfig(0, 0);

  private TrekULConfigYaml trekULConfigYaml;

  private static TrekULConfigDto createAnyTrekUlConfigDto() {
    TrekULConfigDto trekULConfigDto = new TrekULConfigDto();
    createAnyTrekUlConfigTokenDto(trekULConfigDto);
    createAnyTrekUlConfigTripDto(trekULConfigDto);
    createAnyTrekUlConfigReservationDto(trekULConfigDto);
    createAnyTrekUlConfigTransportDto(trekULConfigDto);
    createAnyTrekUlConfigGroupDto(trekULConfigDto);
    createAnyTrekUlConfigSchedulerDto(trekULConfigDto);
    return trekULConfigDto;
  }

  private static void createAnyTrekUlConfigTripDto(TrekULConfigDto trekULConfigDto) {
    trekULConfigDto.trip = new TripConfigDto();
    trekULConfigDto.trip.maximumSeats = ANY_TRIP_MAXIMUM_SEAT;
    trekULConfigDto.trip.defaultCost = ANY_TRIP_DEFAULT_COST;
    trekULConfigDto.trip.defaultStartDate = new DateConfigDto();
    trekULConfigDto.trip.defaultStartDate.year = ANY_DEFAULT_START_DATE_YEAR;
    trekULConfigDto.trip.defaultStartDate.month = ANY_DEFAULT_START_DATE_MONTH;
    trekULConfigDto.trip.defaultStartDate.day = ANY_DEFAULT_START_DATE_DAY;
    trekULConfigDto.trip.timeInSecondsBeforeTripToBlock = ANY_TIME_IN_SECONDS_BEFORE_TRIP_BLOCK;
    trekULConfigDto.trip.notificationThresholdInSeconds = ANY_NOTIFICATION_THRESHOLD;
  }

  private static void createAnyTrekUlConfigTokenDto(TrekULConfigDto trekULConfigDto) {
    trekULConfigDto.token = new TokenConfigDto();
    trekULConfigDto.token.loginExpirationInSeconds = ANY_LOGIN_EXPIRATION_IN_SECONDS;
    trekULConfigDto.token.guest2FAExpirationInSeconds = ANY_GUEST_2FA_EXPIRATION_IN_SECONDS;
  }

  private static void createAnyTrekUlConfigReservationDto(TrekULConfigDto trekULConfigDto) {
    trekULConfigDto.reservation = new ReservationConfigDto();
    trekULConfigDto.reservation.defaultCost = ANY_RESERVATION_DEFAULT_COST;
  }

  private static void createAnyTrekUlConfigTransportDto(TrekULConfigDto trekULConfigDto) {
    trekULConfigDto.transport = new TransportConfigDto();
    trekULConfigDto.transport.defaultOrganizedBusCost = ANY_DEFAULT_ORGANIZED_BUS_COST;
    trekULConfigDto.transport.defaultOrganizedBusMaximumSeats = ANY_ORGANIZED_BUS_SEATS;
  }

  private static void createAnyTrekUlConfigGroupDto(TrekULConfigDto trekULConfigDto) {
    trekULConfigDto.group = new GroupConfigDto();
    trekULConfigDto.group.maximumGroupSize = ANY_MAXIMUM_GROUP_SIZE;
  }

  private static void createAnyTrekUlConfigSchedulerDto(TrekULConfigDto trekULConfigDto) {
    trekULConfigDto.scheduler = new SchedulerConfigDto();
    trekULConfigDto.scheduler.poolSize = ANY_TASK_POOL_SIZE;
    trekULConfigDto.scheduler.shutdownTimeoutInSeconds = ANY_SHUTDOWN_TIME_IN_SECONDS;
  }

  @BeforeEach
  void setUp() {
    ANY_TASKS.put(ANY_TASK_NAME, ANY_BACKGROUND_TASK_CONFIG);
    this.trekULConfigYaml = new TrekULConfigYaml(ANY_TREKUL_CONFIG_DTO, ANY_TASKS);
  }

  @Test
  void whenGetTokenConfig_thenReturnExpectedTokenConfig() {
    Duration defaultLoginExpiration =
        Duration.of(ANY_LOGIN_EXPIRATION_IN_SECONDS, ChronoUnit.SECONDS);
    Duration defaultGuest2FAExpiration =
        Duration.of(ANY_GUEST_2FA_EXPIRATION_IN_SECONDS, ChronoUnit.SECONDS);
    TokenConfig expectedConfig = new TokenConfig(defaultLoginExpiration, defaultGuest2FAExpiration);

    TokenConfig tokenConfig = this.trekULConfigYaml.getTokenConfig();

    assertEquals(expectedConfig, tokenConfig);
  }

  @Test
  void whenGetReservationConfig_thenReturnExpectedReservationConfig() {
    Money defaultCost = new Money(ANY_TREKUL_CONFIG_DTO.reservation.defaultCost);
    Duration defaultExpiration =
        Duration.of(ANY_TREKUL_CONFIG_DTO.reservation.expirationInSeconds, ChronoUnit.SECONDS);

    ReservationConfig obtainedConfig = this.trekULConfigYaml.getReservationConfig();

    ReservationConfig expectedConfig = new ReservationConfig(defaultCost, defaultExpiration);
    assertEquals(expectedConfig, obtainedConfig);
  }

  @Test
  void whenGetTripConfig_thenReturnExpectedTripConfig() {
    TripConfig obtainedTripConfig = this.trekULConfigYaml.getTripConfig();

    this.assertObtainedTripConfigIsExpected(obtainedTripConfig);
  }

  private void assertObtainedTripConfigIsExpected(TripConfig obtainedTripConfig) {
    Money defaultCost = new Money(ANY_TREKUL_CONFIG_DTO.trip.defaultCost);
    int maximumSeats = ANY_TREKUL_CONFIG_DTO.trip.maximumSeats;
    long timeInSecondsBeforeTripToBlock = ANY_TREKUL_CONFIG_DTO.trip.timeInSecondsBeforeTripToBlock;
    long notificationThresholdInSeconds = ANY_TREKUL_CONFIG_DTO.trip.notificationThresholdInSeconds;
    LocalDate defaultTripDate = ANY_TREKUL_CONFIG_DTO.trip.defaultStartDate.asLocalDate();
    TripConfig expectedTripConfig =
        new TripConfig(
            maximumSeats,
            defaultCost,
            timeInSecondsBeforeTripToBlock,
            notificationThresholdInSeconds,
            defaultTripDate);

    assertEquals(
        expectedTripConfig.getDurationBeforeBlockingTrip(),
        obtainedTripConfig.getDurationBeforeBlockingTrip());
    assertEquals(expectedTripConfig.getDefaultTripCost(), obtainedTripConfig.getDefaultTripCost());
    assertEquals(
        expectedTripConfig.getDefaultStartDate(), obtainedTripConfig.getDefaultStartDate());
    assertEquals(expectedTripConfig.getMaximumSeats(), obtainedTripConfig.getMaximumSeats());
  }

  @Test
  void whenGetTaskConfig_thenReturnExpectedTaskConfig() {
    BackgroundTaskConfig backgroundTaskConfig = this.trekULConfigYaml.getTaskConfig(ANY_TASK_NAME);

    assertEquals(ANY_BACKGROUND_TASK_CONFIG, backgroundTaskConfig);
  }

  @Test
  void whenGetSchedulerConfig_thenReturnExpectedSchedulerConfig() {
    SchedulerConfig obtainedSchedulerConfig = this.trekULConfigYaml.getSchedulerConfig();

    SchedulerConfig expectedConfig =
        new SchedulerConfig(
            ANY_TREKUL_CONFIG_DTO.scheduler.shutdownTimeoutInSeconds,
            ANY_TREKUL_CONFIG_DTO.scheduler.poolSize);
    assertEquals(expectedConfig, obtainedSchedulerConfig);
  }

  @Test
  void whenGetTransportConfig_thenReturnExpectedTransportConfig() {
    TransportConfig obtainedTransportConfig = this.trekULConfigYaml.getTransportConfig();

    TransportConfig expectedTransportConfig =
        new TransportConfig(
            new Money(ANY_TREKUL_CONFIG_DTO.transport.defaultOrganizedBusCost),
            ANY_TREKUL_CONFIG_DTO.transport.defaultOrganizedBusMaximumSeats);
    assertEquals(expectedTransportConfig, obtainedTransportConfig);
  }

  @Test
  void whenGetGroupConfig_thenReturnExpectedGroupConfig() {
    GroupConfig obtainedGroupConfig = this.trekULConfigYaml.getGroupConfig();

    GroupConfig expectedGroupConfig = new GroupConfig(ANY_TREKUL_CONFIG_DTO.group.maximumGroupSize);
    assertEquals(expectedGroupConfig, obtainedGroupConfig);
  }
}
