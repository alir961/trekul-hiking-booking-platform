package ca.ulaval.glo4003.transport.organizedbus;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TransportStopTest {
  private static final String ANY_BUS_STOP_LOCATION_NAME = "ANY_NAME";
  private static final TransportStopLocation ANY_LOCATION =
      new TransportStopLocation(ANY_BUS_STOP_LOCATION_NAME);
  private static final LocalDateTime ANY_DEPARTURE_TIME = LocalDateTime.now();
  private TransportStop stop;

  @BeforeEach
  void setUp() {
    this.stop = new TransportStop(ANY_LOCATION, ANY_DEPARTURE_TIME);
  }

  @Test
  void givenNameIsTheSameAsDepartureLocation_whenVerifyName_thenReturnTrue() {
    String sameName = "ANY_NAME";

    boolean same = this.stop.isNamed(sameName);

    assertTrue(same);
  }

  @Test
  void givenNameIsDifferentAsDepartureLocation_whenVerifyName_thenReturnFalse() {
    String differentName = "PEPS";

    boolean same = this.stop.isNamed(differentName);

    assertFalse(same);
  }
}
