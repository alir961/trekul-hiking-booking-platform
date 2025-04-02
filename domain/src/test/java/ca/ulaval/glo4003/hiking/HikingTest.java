package ca.ulaval.glo4003.hiking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import ca.ulaval.glo4003.distance.Distance;
import org.junit.jupiter.api.Test;

class HikingTest {
  private static final String ANY_DISTANCE_STRING = "102";
  private static final Distance A_DISTANCE = new Distance(ANY_DISTANCE_STRING);
  private static final String A_LOCATION_NAME = "Parc Victoria";
  private static final HikingLocation A_LOCATION =
      new HikingLocation(HikingRegion.GASPESIE, A_LOCATION_NAME);
  private static final Hiking A_HIKING =
      new Hiking(A_LOCATION, HikingDifficulty.INTERMEDIATE, A_DISTANCE);

  @Test
  void givenSameObject_whenCallingEquals_thenObjectIsEqual() {
    assertEquals(A_HIKING, A_HIKING);
  }

  @Test
  void givenAnotherObjectWithSameProperties_whenCallingEquals_thenObjectIsEqual() {
    Hiking sameHiking = new Hiking(A_LOCATION, A_HIKING.getDifficulty(), A_HIKING.getDistance());

    assertEquals(A_HIKING, sameHiking);
  }

  @Test
  void givenAnotherObjectWithDifferentDistance_whenCallingEquals_thenObjectIsNotEqual() {
    String differentDistanceString = ANY_DISTANCE_STRING + "03052";
    Distance differentDistance = new Distance(differentDistanceString);
    Hiking differentHiking = new Hiking(A_LOCATION, A_HIKING.getDifficulty(), differentDistance);

    assertNotEquals(A_HIKING, differentHiking);
  }

  @Test
  void givenAnotherObjectWithDifferentDifficulty_whenCallingEquals_thenObjectIsNotEqual() {
    HikingDifficulty differentHikingDifficulty = HikingDifficulty.HARD;
    Hiking differentHiking =
        new Hiking(A_LOCATION, differentHikingDifficulty, A_HIKING.getDistance());

    assertNotEquals(A_HIKING, differentHiking);
  }

  @Test
  void givenAnotherObjectWithDifferentLocation_whenCallingEquals_thenObjectIsNotEqual() {
    HikingRegion hikingRegion = HikingRegion.SAGLAC;
    HikingLocation differentHikingLocation = new HikingLocation(hikingRegion, A_LOCATION_NAME);
    Hiking differentHiking =
        new Hiking(differentHikingLocation, A_HIKING.getDifficulty(), A_HIKING.getDistance());

    assertNotEquals(A_HIKING, differentHiking);
  }
}
