package ca.ulaval.glo4003.config;

import ca.ulaval.glo4003.backgroundtask.BackgroundTaskConfig;
import ca.ulaval.glo4003.backgroundtask.SchedulerConfig;
import ca.ulaval.glo4003.config.dto.BackgroundTaskConfigDto;
import ca.ulaval.glo4003.config.dto.EmailSmtpConfigDto;
import ca.ulaval.glo4003.config.dto.TrekULConfigDto;
import ca.ulaval.glo4003.config.exception.ConfigurationMissingException;
import ca.ulaval.glo4003.config.exception.ConfigurationNotFound;
import ca.ulaval.glo4003.config.exception.InvalidConfigurationException;
import ca.ulaval.glo4003.group.GroupConfig;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.notification.EmailSmtpConfig;
import ca.ulaval.glo4003.reservation.ReservationConfig;
import ca.ulaval.glo4003.token.TokenConfig;
import ca.ulaval.glo4003.transport.TransportConfig;
import ca.ulaval.glo4003.trip.TripConfig;
import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class TrekULConfigYaml {
  private final TrekULConfigDto basicData;
  private final Map<String, BackgroundTaskConfig> tasks = new HashMap<>();

  public TrekULConfigYaml(String filepath) {
    try (YamlReader reader = new YamlReader(new FileReader(filepath))) {
      this.basicData = reader.read(TrekULConfigDto.class);
    } catch (YamlException e) {
      throw new InvalidConfigurationException(e);
    } catch (IOException e) {
      throw new ConfigurationNotFound(filepath);
    }
    for (BackgroundTaskConfigDto task : this.basicData.scheduler.tasks) {
      this.tasks.put(
          task.name, new BackgroundTaskConfig(task.delayInSeconds, task.intervalInSeconds));
    }
  }

  public TrekULConfigYaml(TrekULConfigDto basicData, Map<String, BackgroundTaskConfig> tasks) {
    this.basicData = basicData;

    tasks.forEach(
        (key, value) ->
            this.tasks.put(
                key, new BackgroundTaskConfig(value.delayInSeconds(), value.intervalInSeconds())));
  }

  public ReservationConfig getReservationConfig() {
    Money defaultCost = new Money(this.basicData.reservation.defaultCost);
    Duration defaultExpiration =
        Duration.of(this.basicData.reservation.expirationInSeconds, ChronoUnit.SECONDS);

    return new ReservationConfig(defaultCost, defaultExpiration);
  }

  public TripConfig getTripConfig() {
    Money defaultCost = new Money(this.basicData.trip.defaultCost);
    int maximumSeats = this.basicData.trip.maximumSeats;
    long timeInSecondsBeforeTripToBlock = this.basicData.trip.timeInSecondsBeforeTripToBlock;
    long notificationThresholdInSeconds = this.basicData.trip.notificationThresholdInSeconds;
    LocalDate defaultTripDate = this.basicData.trip.defaultStartDate.asLocalDate();

    return new TripConfig(
        maximumSeats,
        defaultCost,
        timeInSecondsBeforeTripToBlock,
        notificationThresholdInSeconds,
        defaultTripDate);
  }

  public BackgroundTaskConfig getTaskConfig(String taskName) {
    BackgroundTaskConfig taskConfig = this.tasks.get(taskName);
    if (taskConfig == null) {
      throw new ConfigurationMissingException(
          String.format("Missing scheduler.tasks with name %s in yaml", taskName));
    }

    return taskConfig;
  }

  public SchedulerConfig getSchedulerConfig() {
    return new SchedulerConfig(
        this.basicData.scheduler.shutdownTimeoutInSeconds, this.basicData.scheduler.poolSize);
  }

  public TransportConfig getTransportConfig() {
    Money defaultCost = new Money(this.basicData.transport.defaultOrganizedBusCost);
    return new TransportConfig(
        defaultCost, this.basicData.transport.defaultOrganizedBusMaximumSeats);
  }

  public EmailSmtpConfig getEmailSmtpConfig() {
    EmailSmtpConfigDto smtp = this.basicData.smtp;

    return new EmailSmtpConfig(smtp.host, smtp.port, smtp.username, smtp.password);
  }

  public GroupConfig getGroupConfig() {
    return new GroupConfig(this.basicData.group.maximumGroupSize);
  }

  public TokenConfig getTokenConfig() {
    Duration loginExpiration =
        Duration.of(this.basicData.token.loginExpirationInSeconds, ChronoUnit.SECONDS);
    Duration guest2FAExpiration =
        Duration.of(this.basicData.token.guest2FAExpirationInSeconds, ChronoUnit.SECONDS);
    return new TokenConfig(loginExpiration, guest2FAExpiration);
  }
}
