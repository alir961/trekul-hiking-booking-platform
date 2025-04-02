package ca.ulaval.glo4003.distance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

public class DistanceTest {
  private static final String A_DISTANCE_STRING = "1089";
  private static final Distance A_DISTANCE = new Distance(A_DISTANCE_STRING);

  @Test
  void givenSameObject_whenCallingEquals_thenObjectIsEqual() {
    assertEquals(A_DISTANCE, A_DISTANCE);
  }

  @Test
  void givenAnotherObjectWithSameProperties_whenCallingEquals_thenObjectIsEqual() {
    Distance sameDistance = new Distance(A_DISTANCE_STRING);

    assertEquals(A_DISTANCE, sameDistance);
  }

  @Test
  void givenAnotherObjectWithDifferentProperties_whenCallingEquals_thenObjectIsNotEqual() {
    Distance differentDistance = new Distance(A_DISTANCE_STRING + "603");
    assertNotEquals(A_DISTANCE, differentDistance);
  }
}
