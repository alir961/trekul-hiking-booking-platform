package ca.ulaval.glo4003.clock;

import java.time.Instant;
import java.time.LocalDate;

public class SystemClock implements Clock {
  @Override
  public Instant now() {
    return Instant.now();
  }

  @Override
  public LocalDate today() {
    return LocalDate.now();
  }
}
