package ca.ulaval.glo4003.clock;

import java.time.Instant;
import java.time.LocalDate;

public interface Clock {
  Instant now();

  LocalDate today();
}
