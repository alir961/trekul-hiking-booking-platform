package ca.ulaval.glo4003.config.dto;

import java.time.LocalDate;

public class DateConfigDto {
  public int year;
  public int month;
  public int day;

  public LocalDate asLocalDate() {
    return LocalDate.of(this.year, this.month, this.day);
  }
}
